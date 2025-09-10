package app.snapshot.qure.facilities.repository;

import app.snapshot.qure.facilities.dto.ChecklistTemplateDto;
import app.snapshot.qure.facilities.dto.FacilitiesDto;
import app.snapshot.qure.facilities.dto.InspectionDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface IFacilitiesRepository {
    List<FacilitiesDto> getFacilitiesListByManager(@Param("managerId") int managerId);

    List<FacilitiesDto> searchFacilitiesByManager(@Param("managerId") int managerId,
                                                  @Param("q") String q);

    FacilitiesDto getFacilitiesInfoByManager(@Param("facilityId") int facilityId,
                                             @Param("managerId") int managerId);

    int insertFacilitiesByManager(@Param("f") FacilitiesDto f,
                                  @Param("managerId") int managerId);

    int updateFacilitiesByManager(@Param("facility") FacilitiesDto facilities,
                                  @Param("managerId") int managerId);

    int deleteFacilitiesByManager(@Param("facilityId") int facilityId,
                                  @Param("managerId") int managerId);

    List<ChecklistTemplateDto> findTemplatesByFacilityIdAndManager(@Param("facilityId") int facilityId,
                                                                   @Param("managerId") int managerId);

    List<InspectionDto> findInspectionByFacilityIdAndManager(@Param("facilityId") int facilityId,
                                                             @Param("managerId") int managerId);

    List<InspectionDto> findInspectionByFacilityIdAndPeriodAndManager(@Param("facilityId") int facilityId,
                                                                      @Param("startD") LocalDate startD,
                                                                      @Param("endD") LocalDate endD,
                                                                      @Param("managerId") int managerId);


}

