package app.snapshot.qure.facilities.service;
import java.time.LocalDate;
import java.util.List;

import app.snapshot.qure.facilities.dto.ChecklistTemplateDto;
import app.snapshot.qure.facilities.dto.FacilitiesDto;
import app.snapshot.qure.facilities.dto.FacilityTagDto;
import app.snapshot.qure.facilities.dto.InspectionDto;

public interface IFacilitiesService {

    /* 목록/검색: 로그인한 관리자 소유만 */
    List<FacilitiesDto> getFacilitiesListByManager(Long managerId);
    List<FacilitiesDto> searchFacilitiesByManager(Long managerId, String q);

    /* 단건 조회: 소유 확인 포함 */
    FacilitiesDto getFacilitiesInfoByManager(Long facilityId, Long managerId);
    FacilitiesDto findByfacilityIdAndManager(Long facilityId, Long managerId);

    /* 등록/수정/삭제: 소유권 보장 */
    Long createFacilityWithQr(FacilitiesDto dto, Long managerId);
    int updateFacilitiesByManager(FacilitiesDto facilities, Long managerId);
    int deleteFacilitiesByManager(Long facilityId, Long managerId);

    /* 부가 데이터: 항상 FACILITIES와 조인해 managerId 가드 */
    List<ChecklistTemplateDto> findTemplatesByFacilityIdAndManager(Long facilityId, Long managerId);

    List<InspectionDto> findInspectionByFacilityIdAndManager(Long facilityId, Long managerId);
    List<InspectionDto> findInspectionByFacilityIdAndPeriodAndManager(
            Long facilityId, LocalDate startD, LocalDate endD, Long managerId
    );

    /* QR 태그: facilityId로 조회(상위에서 소유 확인 후 사용) */
    FacilityTagDto findActiveTagByFacilityId(Long facilityId);

    // 주소에서 좌표 변경
    void createByManager(FacilitiesDto facility, Long managerId);
}
