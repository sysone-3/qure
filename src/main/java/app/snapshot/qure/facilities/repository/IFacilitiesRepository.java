package app.snapshot.qure.facilities.repository;

import app.snapshot.qure.facilities.dto.ChecklistTemplateDto;
import app.snapshot.qure.facilities.dto.FacilitiesDto;
import app.snapshot.qure.facilities.dto.FacilityMarkerDto;
import app.snapshot.qure.facilities.dto.InspectionDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface IFacilitiesRepository {
    List<FacilitiesDto> getFacilitiesListByManager(@Param("managerId") Long managerId);

    List<FacilitiesDto> searchFacilitiesByManager(@Param("managerId") Long managerId,
                                                  @Param("q") String q);

    FacilitiesDto getFacilitiesInfoByManager(@Param("facilityId") Long facilityId,
                                             @Param("managerId") Long managerId);

    Long insertFacilitiesByManager(@Param("f") FacilitiesDto f,
                                  @Param("managerId") Long managerId);

    int updateFacilitiesByManager(@Param("facility") FacilitiesDto facilities,
                                  @Param("managerId") Long managerId);

    int deleteFacilitiesByManager(@Param("facilityId") Long facilityId,
                                  @Param("managerId") Long managerId);

    List<ChecklistTemplateDto> findTemplatesByFacilityIdAndManager(@Param("facilityId") Long facilityId,
                                                                   @Param("managerId") Long managerId);

    List<InspectionDto> findInspectionByFacilityIdAndManager(@Param("facilityId") Long facilityId,
                                                             @Param("managerId") Long managerId);

    List<InspectionDto> findInspectionByFacilityIdAndPeriodAndManager(@Param("facilityId") Long facilityId,
                                                                      @Param("startD") LocalDate startD,
                                                                      @Param("endD") LocalDate endD,
                                                                      @Param("managerId") Long managerId);
    List<FacilityMarkerDto> findMarkersByManagerId(@Param("managerId") Long managerId);

    List<FacilitiesDto> findByManagerWithNullCoords(@Param("managerId") Long managerId);

    int updateCoords(@Param("facilityId") Long facilityId,
                     @Param("gpsLat") Double gpsLat,
                     @Param("gpsLng") Double gpsLng);


    void attachTemplatesToFacility(@Param("facilityId") Long facilityId,
                                   @Param("templateIds") List<Long> templateIds, @Param("managerId") Long managerId );

}

