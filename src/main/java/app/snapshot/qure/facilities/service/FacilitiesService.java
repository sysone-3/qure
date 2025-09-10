package app.snapshot.qure.facilities.service;

import java.security.Timestamp;
import java.time.LocalDate;
import java.util.List;

import app.snapshot.qure.facilities.dto.ChecklistTemplateDto;
import app.snapshot.qure.facilities.dto.FacilityTagDto;
import app.snapshot.qure.facilities.dto.InspectionDto;
import app.snapshot.qure.facilities.repository.IFacilityTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.snapshot.qure.facilities.dto.FacilitiesDto;
import app.snapshot.qure.facilities.repository.IFacilitiesRepository;

@Service
public class FacilitiesService implements IFacilitiesService {

    @Autowired
    IFacilitiesRepository facilitiesRepository;

    @Autowired
    IFacilityTagRepository facilityTagRepository;

    @Value("${app.public-domain}")
    private String publicDomain;

    /** 설비 등록 + QR 태그 발급 (원샷) */
    @Transactional
    public int createFacilityWithQr(FacilitiesDto dto) {
        // 1) 설비 생성 (selectKey로 facilityId 세팅)
        facilitiesRepository.insertFacilities(dto);

        // (선택) 기존 활성 태그 비활성화: 신규 등록이라면 보통 불필요
        // facilityTagRepository.deactivateActiveTags(dto.getFacilityId());

        // 2) 새 TAG_ID 확보
        int nextTagId = facilityTagRepository.getNextTagId();

        // 3) URL 생성 후 INSERT
        FacilityTagDto tag = new FacilityTagDto();
        tag.setTagId(nextTagId);
        tag.setFacilityId(dto.getFacilityId());
        tag.setCode(publicDomain + "/mobile/main/" + nextTagId);

        facilityTagRepository.insertFacilityTagWithGivenId(tag);

        return dto.getFacilityId();
    }

    public FacilityTagDto findActiveTagByFacilityId(int facilityId) {
        return facilityTagRepository.findActiveTagByFacilityId(facilityId);
    }

    public FacilityTagDto findTagById(int tagId) {
        return facilityTagRepository.findById(tagId);
    }

    @Override
    public List<FacilitiesDto> getFacilitiesList() {
        return facilitiesRepository.getFacilitiesList();
    }

    @Override
    public List<FacilitiesDto> searchFacilities(String q) {
        return facilitiesRepository.searchFacilities(q);
    }

    @Override
    public FacilitiesDto getFacilitiesInfo(int facilityId) {
        return facilitiesRepository.getFacilitiesInfo(facilityId);
    }

    @Override
    public int insertFacilities(FacilitiesDto facilities) {
        return facilitiesRepository.insertFacilities(facilities);
    }

    @Override
    public int updateFacilities(FacilitiesDto facilities) {
        return facilitiesRepository.updateFacilities(facilities);
    }

    @Override
    @Transactional(readOnly = true)
    public FacilitiesDto findById(int facilityId) {
        return facilitiesRepository.getFacilitiesInfo(facilityId); // 없으면 null
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChecklistTemplateDto> findTemplatesByFacilityId(int facilityId) {
        return facilitiesRepository.findTemplatesByFacilityId(facilityId);
    }



    @Override
    @Transactional(readOnly = true)
    public List<InspectionDto> findInspectionByFacilityId(int facilityId) {
        return facilitiesRepository.findInspectionByFacilityId(facilityId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InspectionDto> findInspectionByFacilityIdAndPeriod(int facilityId, LocalDate startD, LocalDate endD) {
        return facilitiesRepository.findInspectionByFacilityIdAndPeriod(facilityId, startD, endD);
    }

    @Override
    @Transactional
    public int deleteFacilities(int facilityId, String email) {
        return facilitiesRepository.deleteFacilities(facilityId, email);
    }

}

