package app.snapshot.qure.facilities.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.snapshot.qure.facilities.dto.*;
import app.snapshot.qure.facilities.repository.IFacilityTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.snapshot.qure.facilities.repository.IFacilitiesRepository;

@Service
public class FacilitiesService implements IFacilitiesService {

    @Autowired
    IFacilitiesRepository facilitiesRepository;

    @Autowired
    IFacilityTagRepository facilityTagRepository;

    @Autowired
    GeocodingService geocodingService; // ← 주소→좌표 변환용 주입


    @Value("${app.public-domain}")
    private String publicDomain;


    public FacilityTagDto findActiveTagByFacilityId(Long facilityId) {
        return facilityTagRepository.findActiveTagByFacilityId(facilityId);
    }

    public FacilityTagDto findTagById(Long tagId) {
        return facilityTagRepository.findById(tagId);
    }

    @Override
    public List<FacilitiesDto> getFacilitiesListByManager(Long managerId) {
        return facilitiesRepository.getFacilitiesListByManager(managerId);
    }

    @Override
    public List<FacilitiesDto> searchFacilitiesByManager(Long managerId, String q) {
        return facilitiesRepository.searchFacilitiesByManager(managerId, q);
    }

    @Override
    public FacilitiesDto getFacilitiesInfoByManager(Long facilityId, Long managerId) {
        return null;
    }

    @Override
    public FacilitiesDto findByfacilityIdAndManager(Long facilityId, Long managerId) {
        return facilitiesRepository.getFacilitiesInfoByManager(facilityId, managerId);
    }

    @Override
    public int updateFacilitiesByManager(FacilitiesDto facilities, Long managerId) {
        return facilitiesRepository.updateFacilitiesByManager(facilities, managerId);
    }

    @Override
    public int deleteFacilitiesByManager(Long facilityId, Long managerId) {
        return facilitiesRepository.deleteFacilitiesByManager(facilityId, managerId);
    }

    @Override
    public List<ChecklistTemplateDto> findTemplatesByFacilityIdAndManager(Long facilityId, Long managerId) {
        return facilitiesRepository.findTemplatesByFacilityIdAndManager(facilityId, managerId);
    }

    @Override
    public List<InspectionDto> findInspectionByFacilityIdAndManager(Long facilityId, Long managerId) {
        return facilitiesRepository.findInspectionByFacilityIdAndManager(facilityId, managerId);
    }

    @Override
    public List<InspectionDto> findInspectionByFacilityIdAndPeriodAndManager(Long facilityId, LocalDate s, LocalDate e, Long managerId) {
        return facilitiesRepository.findInspectionByFacilityIdAndPeriodAndManager(facilityId, s, e, managerId);
    }


    @Override
    public List<FacilitiesDto> findFacilitiesPaged(Long managerId, String q, int startRow, int endRow) {
        Map<String, Object> p = new HashMap<>();
        p.put("managerId", managerId);
        p.put("q", q);
        p.put("startRow", startRow);
        p.put("endRow", endRow);
        return facilitiesRepository.findFacilitiesPaged(p);
    }

    @Override
    public int countFacilities(Long managerId, String q) {
        Map<String, Object> p = new HashMap<>();
        p.put("managerId", managerId);
        p.put("q", q);
        return facilitiesRepository.countFacilities(p);
    }

    /* 등록 시에도 로그인한 관리자 ID 주입 */
    /** 설비 등록 + QR 태그 발급 (원샷) */
    @Transactional
    public Long createFacilityWithQr(FacilitiesDto dto, Long managerId) {
        // 1) 설비 생성 (managerId는 SQL에서 바인딩)
        facilitiesRepository.insertFacilitiesByManager(dto, managerId);
        // 이 시점에 dto.facilityId 가 selectKey로 세팅되어 있어야 함

        // 2) 새 TAG_ID 확보
        Long nextTagId = facilityTagRepository.getNextTagId();

        // 3) URL 생성 후 INSERT
        FacilityTagDto tag = new FacilityTagDto();
        tag.setTagId(nextTagId);
        tag.setFacilityId(dto.getFacilityId());
        tag.setCode(publicDomain + "/mobile/main/" + nextTagId);

        facilityTagRepository.insertFacilityTagWithGivenId(tag);
        return dto.getFacilityId();
    }

    @Override
    public void createByManager(FacilitiesDto facility, Long managerId) {
        facilitiesRepository.insertFacilitiesByManager(facility, managerId);
    }

    // 지도 마커 찍기
    public List<FacilityMarkerDto> findMarkersForManager(Long managerId) {
        return facilitiesRepository.findMarkersByManagerId(managerId);
    }

    @Transactional
    public int backfillMissingCoordsForManager(Long managerId) {
        List<FacilitiesDto> targets =
                facilitiesRepository.findByManagerWithNullCoords(managerId);

        int updated = 0;
        for (FacilitiesDto f : targets) {
            var ll = geocodingService.geocode(f.getAddress());
            if (ll != null) {
                double lat = round(ll.lat(), 6);
                double lng = round(ll.lng(), 6);
                facilitiesRepository.updateCoords(f.getFacilityId(), lat, lng);
                updated++;
                // (선택) 과도한 호출 방지: Thread.sleep(50);
            }
        }
        return updated;

    }

    private static double round(double v, int scale) {
        double p = Math.pow(10, scale);
        return Math.round(v * p) / p;
    }

    @Transactional
    public void attachTemplatesToFacility(Long facilityId, List<Long> templateIds, Long managerId) {
        if (templateIds == null || templateIds.isEmpty()) return;
        facilitiesRepository.attachTemplatesToFacility(facilityId, templateIds , managerId);
    }

    /**
     * 점검표 변경사항 처리 (삭제된 것들 연결 해제, 추가된 것들 연결)
     */
    @Transactional
    public void updateFacilityTemplates(Long facilityId,
                                        List<Long> removedTemplateIds,
                                        List<Long> addedTemplateIds,
                                        Long managerId) {

        // 1. 삭제된 점검표들 연결 해제
        if (removedTemplateIds != null && !removedTemplateIds.isEmpty()) {
            facilitiesRepository.detachTemplatesFromFacility(facilityId, removedTemplateIds, managerId);
            System.out.println("연결 해제된 점검표: " + removedTemplateIds);
        }

        // 2. 추가된 점검표들 연결
        if (addedTemplateIds != null && !addedTemplateIds.isEmpty()) {
            facilitiesRepository.attachTemplatesToFacility(facilityId, addedTemplateIds, managerId);
            System.out.println("새로 연결된 점검표: " + addedTemplateIds);
        }
    }

    /**
     * 전체 점검표 교체 (기존 방식 대체)
     */
    @Transactional
    public void replaceAllFacilityTemplates(Long facilityId, List<Long> templateIds, Long managerId) {
        // 1. 기존 연결 모두 해제
        facilitiesRepository.detachAllTemplatesFromFacility(facilityId, managerId);

        // 2. 새로운 연결 생성
        if (templateIds != null && !templateIds.isEmpty()) {
            facilitiesRepository.attachTemplatesToFacility(facilityId, templateIds, managerId);
        }
    }


}

