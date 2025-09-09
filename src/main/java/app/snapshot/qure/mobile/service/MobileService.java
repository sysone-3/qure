// src/main/java/app/snapshot/qure/mobile/service/MobileService.java
package app.snapshot.qure.mobile.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import app.snapshot.qure.checklist.dto.ChecklistType;            // [CHANGED] import 추가
import app.snapshot.qure.mobile.dto.ChecklistItemDto;
import app.snapshot.qure.mobile.dto.ChecklistSubmitForm;
import app.snapshot.qure.mobile.dto.ChecklistTemplateDto;
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
      s3.headBucket(b -> b.bucket(bucket)); // 연결 확인만 유지
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
        int facilityId = submitIdDto.getFacilityId();

        boolean hasFail = false;
        if (form.getResults() != null) {
            for (ChecklistSubmitForm.ResultRow r : form.getResults()) {
                String v = (r.getValue() == null) ? "" : r.getValue().trim();
                if ("N".equalsIgnoreCase(v)) { hasFail = true; break; }
            }
        }
        String finalResult = hasFail ? "FAIL" : "PASS";

        InspectionInsertDto dto = new InspectionInsertDto();
        dto.setSubmittedAt(java.time.LocalDateTime.now());
        dto.setResult(finalResult);
        dto.setFacilityId(facilityId);
        dto.setInspectorId(inspectorId);
        mobileMapper.insertInspection(dto);

        Long inspectionId = dto.getInspectionId();

        if (form.getResults() != null) {
            for (ChecklistSubmitForm.ResultRow r : form.getResults()) {
                if (r == null || r.getItemId() == null) continue;

                ChecklistType type = mobileMapper.selectItemTypeById(r.getItemId()); // [CHANGED] Enum 사용
                String raw  = r.getValue() == null ? "" : r.getValue().trim();

                InspectionItemResultInsertDto rd = new InspectionItemResultInsertDto();
                rd.setInspectionId(inspectionId);
                rd.setItemId(r.getItemId());
                rd.setCreatedAt(java.time.LocalDateTime.now());
                rd.setImageId(null);

                switch (type) {                                              // [CHANGED] switch로 분기
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
                        // 값 셋업 없음. 아래 사진 업로드 처리
                        break;
                }

                mobileMapper.insertInspectionItemResult(rd);
                Long resultId = rd.getResultId();

                if (type == ChecklistType.IMAGE) {                           // [CHANGED] Enum 비교
                    var files = r.getPhotos();
                    if (files != null && !files.isEmpty()) {
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

                            var im = new ImageMetaInsertDto();
                            im.setFilePath(key);
                            im.setMimeType(ct);
                            im.setResultId(resultId);
                            mobileMapper.insertImageMeta(im);
                        }
                    }
                }
            }
        }
        return inspectionId;
    }
    
 // [TEST] 최근 이미지 조회 구현
    @Transactional(readOnly = true)
    @Override
    public List<ImageMetaInsertDto> listLatestImages(int limit) {
        return mobileMapper.selectLatestImages(limit);
    }

}
