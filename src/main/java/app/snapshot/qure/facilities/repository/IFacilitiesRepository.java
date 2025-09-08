package app.snapshot.qure.facilities.repository;

import app.snapshot.qure.facilities.dto.ChecklistTemplateDto;
import app.snapshot.qure.facilities.dto.FacilitiesDto;
import app.snapshot.qure.facilities.dto.InspectionDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.security.Timestamp;
import java.util.List;

@Mapper  // MyBatis Mapper 인터페이스
public interface IFacilitiesRepository {

    List<FacilitiesDto> getFacilitiesList();

    List<FacilitiesDto> searchFacilities(@Param("q") String q);

    FacilitiesDto getFacilitiesInfo(@Param("facilityId") int facilityId);

    int insertFacilities(FacilitiesDto facilities);

    int updateFacilities(FacilitiesDto facilities);

    int deleteFacilities(@Param("facilityId") int facilityId, @Param("email") String email);

    List<ChecklistTemplateDto> findTemplatesByFacilityId(@Param("facilityId") int facilityId);

    List<InspectionDto> findInspectionByFacilityId(@Param("facilityId") int facilityId);
}
