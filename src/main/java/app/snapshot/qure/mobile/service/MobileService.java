package app.snapshot.qure.mobile.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import app.snapshot.qure.checklist.model.ChecklistType;
import app.snapshot.qure.mobile.dto.ChecklistItemDto;
import app.snapshot.qure.mobile.dto.ChecklistSubmitForm;
import app.snapshot.qure.mobile.dto.ChecklistTemplateDto;
import app.snapshot.qure.mobile.dto.CitizenReportDto;
import app.snapshot.qure.mobile.dto.ImageMetaInsertDto;
import app.snapshot.qure.mobile.dto.InspectionInsertDto;
import app.snapshot.qure.mobile.dto.InspectionItemResultInsertDto;
import app.snapshot.qure.mobile.dto.SubmitIdDto;
import app.snapshot.qure.mobile.dto.TagSummaryDto;
import app.snapshot.qure.mobile.repository.MobileMapper;
import jakarta.annotation.PostConstruct;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;

@Service
public class MobileService implements IMobileService {

    @Autowired private MobileMapper mobileMapper;
    @Autowired private S3Client s3;

    @Value("${aws.region}") private String region;
    @Value("${s3.bucket}")  private String bucket;

    @PostConstruct
    void init(){
      s3.headBucket(b -> b.bucket(bucket));
    }

    @Transactional(readOnly = true)
    @Override
    public TagSummaryDto getTagInfoByTagId(int tagId) {
        TagSummaryDto dto = mobileMapper.selectTagSummaryById(tagId);
        if (dto == null) throw new NoSuchElementException("태그 없음: " + tagId);
        return dto;
    }

    @Override
    public boolean validateInspectorPin(int tagId, int pin) {
        String pin4 = String.format("%04d", pin);
        String expectedPin4 = mobileMapper.selectPin4byTagId(tagId);
        return pin4.equals(expectedPin4);
    }

    @Override
    public ChecklistTemplateDto getTemplateByTagId(int tagId) {
        ChecklistTemplateDto checklistTemplateDto = mobileMapper.selectTemplateByTagId(tagId);
        if (checklistTemplateDto == null) throw new NoSuchElementException("템플릿 없음: " + tagId);
        return checklistTemplateDto;
    }

    @Override
    public List<ChecklistItemDto> getChecklistItemByTemplateId(int templateId) {
        return mobileMapper.selectAllChecklistByTemplateId(templateId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long saveChecklistSubmission(int tagId, ChecklistSubmitForm form) {

        SubmitIdDto submitIdDto = mobileMapper.selectSubmitIdByTagId(tagId);
        if (submitIdDto == null || submitIdDto.getInspectorId() == 0)
            throw new NoSuchElementException("inspector 없음");

        int inspectorId = submitIdDto.getInspectorId();
        int facilityId  = submitIdDto.getFacilityId();

        boolean hasFail = false;
        if (form.getResults() != null) {
            for (ChecklistSubmitForm.ResultRow r : form.getResults()) {
                String v = (r.getValue() == null) ? "" : r.getValue().trim();
                if ("N".equalsIgnoreCase(v)) { hasFail = true; break; }
            }
        }
        String finalResult = hasFail ? "FAIL" : "PASS";

        // inspections INSERT: submitted_at = SYSTIMESTAMP (매퍼에서 처리)
        InspectionInsertDto dto = new InspectionInsertDto();
        dto.setResult(finalResult);
        dto.setFacilityId(facilityId);
        dto.setInspectorId(inspectorId);
        mobileMapper.insertInspection(dto);
        Long inspectionId = dto.getInspectionId();

        if (form.getResults() != null) {
            for (ChecklistSubmitForm.ResultRow r : form.getResults()) {
                if (r == null || r.getItemId() == null) continue;

                ChecklistType type = mobileMapper.selectItemTypeById(r.getItemId());
                String raw = r.getValue() == null ? "" : r.getValue().trim();

                InspectionItemResultInsertDto rd = new InspectionItemResultInsertDto();
                rd.setInspectionId(inspectionId);
                rd.setItemId(r.getItemId());
                rd.setCreatedAt(java.time.LocalDateTime.now());
                rd.setImageId(null);

                switch (type) {
                    case BOOL:
                        if ("Y".equalsIgnoreCase(raw) || "N".equalsIgnoreCase(raw)) {
                            rd.setValueBool(raw.toUpperCase());
                        }
                        break;
                    case NUM:
                        rd.setValueNum(raw.isEmpty() ? null : Double.valueOf(raw));
                        break;
                    case TEXT:
                        rd.setValueText(raw.isEmpty() ? null : raw);
                        break;
                    case IMAGE:
                        break;
                }

                mobileMapper.insertInspectionItemResult(rd);
                Long resultId = rd.getResultId();

                if (type == ChecklistType.IMAGE) {
                    MultipartFile[] files = r.getPhotos();
                    if (files != null && files.length > 0) {
                        for (MultipartFile f : files) {
                            if (f == null || f.isEmpty()) continue;

                            String ct = (f.getContentType() == null) ? "image/jpeg" : f.getContentType();
                            String ext = ct.contains("/") ? ct.substring(ct.indexOf('/') + 1).toLowerCase() : "jpg";
                            if ("jpeg".equals(ext)) ext = "jpg";

                            String name = UUID.randomUUID().toString().replace("-", "") + "." + ext;
                            String key  = "inspection/%d/%d/%s".formatted(inspectionId, r.getItemId(), name);

                            try (var in = f.getInputStream()) {
                                s3.putObject(b -> b.bucket(bucket).key(key).contentType(ct),
                                             RequestBody.fromInputStream(in, f.getSize()));
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }

                            ImageMetaInsertDto im = new ImageMetaInsertDto();
                            im.setFilePath(key);
                            im.setMimeType(ct);
                            im.setResultId(resultId);
                            mobileMapper.insertImageMeta(im);
                        }
                    }
                }
            }
        }

        // 다음 예정일 갱신: 기준시각 = SYSTIMESTAMP (매퍼에서 계산)
        ChecklistTemplateDto tpl = mobileMapper.selectTemplateByTagId(tagId);
        String unit = tpl.getCycleUnit().name();
        if (!unit.equals("DAY") && !unit.equals("WEEK") && !unit.equals("MONTH") && !unit.equals("YEAR"))
            throw new IllegalStateException("invalid cycleUnit: " + unit);
        mobileMapper.updateNextScheduledAtByFacility(facilityId, tpl.getCycle(), unit);

        return inspectionId;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ImageMetaInsertDto> listLatestImages(int limit) {
        return mobileMapper.selectLatestImages(limit);
    }

    @Override
    public long submitComplain(int tagId, String category, String description, String email) {
      String cat  = category == null ? "" : category.trim();
      String desc = description == null ? "" : description.trim();
      if (cat.isEmpty() || desc.isEmpty()) {
        throw new IllegalArgumentException("category/description required");
      }

      var tag = mobileMapper.selectTagSummaryById(tagId);
      if (tag == null) throw new IllegalStateException("invalid tagId");
      int facilityId = tag.getFacilityId();
      if (facilityId <= 0) throw new IllegalStateException("invalid facilityId");

      String mail = (email == null || email.isBlank()) ? null : email.trim().toLowerCase();
      if (mail != null && mail.length() > 254) {
        throw new IllegalArgumentException("email too long");
      }

      Integer managerId = mobileMapper.selectManagerIdByFacilityId(facilityId);

      CitizenReportDto dto = new CitizenReportDto();
      dto.setFacilityId(facilityId);
      dto.setCategory(cat);
      dto.setDescription(desc);
      dto.setEmail(mail);
      dto.setResolvedBy(managerId);

      int rows = mobileMapper.insertComplain(dto);
      if (rows != 1 || dto.getCitizenReportId() == null) {
        throw new IllegalStateException("insert failed");
      }
      return dto.getCitizenReportId();
    }
}
