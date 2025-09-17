package app.snapshot.qure.facilities.repository;

import app.snapshot.qure.facilities.dto.FacilitySummaryDto;
import org.apache.ibatis.annotations.Param;

// 작성자: 김민서
public interface IFacilitySummaryRepository {
    FacilitySummaryDto findSummaryByFacilityId(@Param("facilityId") Long facilityId);

}
