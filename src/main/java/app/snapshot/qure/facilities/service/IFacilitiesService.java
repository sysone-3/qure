package app.snapshot.qure.facilities.service;
import java.time.LocalDate;
import java.util.List;

import app.snapshot.qure.facilities.dto.ChecklistTemplateDto;
import app.snapshot.qure.facilities.dto.FacilitiesDto;
import app.snapshot.qure.facilities.dto.FacilityTagDto;
import app.snapshot.qure.facilities.dto.InspectionDto;

public interface IFacilitiesService {

    /* 목록/검색: 로그인한 관리자 소유만 */
    List<FacilitiesDto> getFacilitiesListByManager(int managerId);
    List<FacilitiesDto> searchFacilitiesByManager(int managerId, String q);

    /* 단건 조회: 소유 확인 포함 */
    FacilitiesDto getFacilitiesInfoByManager(int facilityId, int managerId);
    FacilitiesDto findByfacilityIdAndManager(int facilityId, int managerId);

    /* 등록/수정/삭제: 소유권 보장 */
    int createFacilityWithQr(FacilitiesDto dto, int managerId);
    int updateFacilitiesByManager(FacilitiesDto facilities, int managerId);
    int deleteFacilitiesByManager(int facilityId, int managerId);

    /* 부가 데이터: 항상 FACILITIES와 조인해 managerId 가드 */
    List<ChecklistTemplateDto> findTemplatesByFacilityIdAndManager(int facilityId, int managerId);

    List<InspectionDto> findInspectionByFacilityIdAndManager(int facilityId, int managerId);
    List<InspectionDto> findInspectionByFacilityIdAndPeriodAndManager(
            int facilityId, LocalDate startD, LocalDate endD, int managerId
    );

    /* QR 태그: facilityId로 조회(상위에서 소유 확인 후 사용) */
    FacilityTagDto findActiveTagByFacilityId(int facilityId);

    // 주소에서 좌표 변경
    void createByManager(FacilitiesDto facility, int managerId);
}
