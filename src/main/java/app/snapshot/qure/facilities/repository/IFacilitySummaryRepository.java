package app.snapshot.qure.facilities.repository;

import app.snapshot.qure.facilities.dto.FacilitySummaryDto;
import org.apache.ibatis.annotations.Param;

public interface IFacilitySummaryRepository {
    FacilitySummaryDto findSummaryByFacilityId(@Param("facilityId") int facilityId);

}
