package app.snapshot.qure.facilities.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import app.snapshot.qure.facilities.dto.ChecklistTemplateDto;
import app.snapshot.qure.facilities.dto.FacilitiesDto;
import app.snapshot.qure.facilities.dto.FacilityTagDto;
import app.snapshot.qure.facilities.dto.InspectionDto;
import org.springframework.transaction.annotation.Transactional;

public interface IFacilitiesService {
    List<FacilitiesDto> getFacilitiesList();
    List<FacilitiesDto> searchFacilities(String q);

    FacilitiesDto getFacilitiesInfo(int facilityId);

    int insertFacilities(FacilitiesDto facilities);
    int updateFacilities(FacilitiesDto facilities);
    FacilitiesDto findById(int facilityId);
    List<ChecklistTemplateDto> findTemplatesByFacilityId(int facilityId);

    @Transactional(readOnly = true)
    List<InspectionDto> findInspectionByFacilityId(int facilityId);

    int deleteFacilities(int facilityId, String email);

    List<InspectionDto> findInspectionByFacilityIdAndPeriod(int facilityId, LocalDate startD, LocalDate endD);

    FacilityTagDto findActiveTagByFacilityId(int id);

    int createFacilityWithQr(FacilitiesDto dto);
}
