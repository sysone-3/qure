package app.snapshot.qure.facilities.service;

import java.time.LocalDate;
import java.util.List;

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


    public FacilityTagDto findActiveTagByFacilityId(int facilityId) {
        return facilityTagRepository.findActiveTagByFacilityId(facilityId);
    }

    public FacilityTagDto findTagById(int tagId) {
        return facilityTagRepository.findById(tagId);
    }

    @Override
    public List<FacilitiesDto> getFacilitiesListByManager(int managerId) {
        return facilitiesRepository.getFacilitiesListByManager(managerId);
    }

    @Override
    public List<FacilitiesDto> searchFacilitiesByManager(int managerId, String q) {
        return facilitiesRepository.searchFacilitiesByManager(managerId, q);
    }

    @Override
    public FacilitiesDto getFacilitiesInfoByManager(int facilityId, int managerId) {
        return null;
    }

    @Override
    public FacilitiesDto findByfacilityIdAndManager(int facilityId, int managerId) {
        return facilitiesRepository.getFacilitiesInfoByManager(facilityId, managerId);
    }

    @Override
    public int updateFacilitiesByManager(FacilitiesDto facilities, int managerId) {
        return facilitiesRepository.updateFacilitiesByManager(facilities, managerId);
    }

    @Override
    public int deleteFacilitiesByManager(int facilityId, int managerId) {
        return facilitiesRepository.deleteFacilitiesByManager(facilityId, managerId);
    }

    @Override
    public List<ChecklistTemplateDto> findTemplatesByFacilityIdAndManager(int facilityId, int managerId) {
        return facilitiesRepository.findTemplatesByFacilityIdAndManager(facilityId, managerId);
    }

    @Override
    public List<InspectionDto> findInspectionByFacilityIdAndManager(int facilityId, int managerId) {
        return facilitiesRepository.findInspectionByFacilityIdAndManager(facilityId, managerId);
    }

    @Override
    public List<InspectionDto> findInspectionByFacilityIdAndPeriodAndManager(int facilityId, LocalDate s, LocalDate e, int managerId) {
        return facilitiesRepository.findInspectionByFacilityIdAndPeriodAndManager(facilityId, s, e, managerId);
    }

    /* 등록 시에도 로그인한 관리자 ID 주입 */
    /** 설비 등록 + QR 태그 발급 (원샷) */
    @Transactional
    public int createFacilityWithQr(FacilitiesDto dto, int managerId) {
        // 1) 설비 생성 (managerId는 SQL에서 바인딩)
        facilitiesRepository.insertFacilitiesByManager(dto, managerId);
        // 이 시점에 dto.facilityId 가 selectKey로 세팅되어 있어야 함

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

    @Override
    public void createByManager(FacilitiesDto facility, int managerId) {
        facilitiesRepository.insertFacilitiesByManager(facility, managerId);
    }

    // 지도 마커 찍기
    public List<FacilityMarkerDto> findMarkersForManager(int managerId) {
        return facilitiesRepository.findMarkersByManagerId(managerId);
    }

    @Transactional
    public int backfillMissingCoordsForManager(int managerId) {
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


}

