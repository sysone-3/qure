package app.snapshot.qure.facilities.service;

import java.security.Timestamp;
import java.util.List;
import java.util.Map;

import app.snapshot.qure.facilities.dto.ChecklistTemplateDto;
import app.snapshot.qure.facilities.dto.FacilitiesDto;
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

}
