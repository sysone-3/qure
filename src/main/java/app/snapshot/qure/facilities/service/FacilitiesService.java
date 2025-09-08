package app.snapshot.qure.facilities.service;

import java.security.Timestamp;
import java.util.List;

import app.snapshot.qure.facilities.dto.ChecklistTemplateDto;
import app.snapshot.qure.facilities.dto.InspectionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.snapshot.qure.facilities.dto.FacilitiesDto;
import app.snapshot.qure.facilities.repository.IFacilitiesRepository;

@Service
public class FacilitiesService implements IFacilitiesService {

    @Autowired
    IFacilitiesRepository facilitiesRepository;

    @Override
    public List<FacilitiesDto> getFacilitiesList() {
        return facilitiesRepository.getFacilitiesList();
    }

    @Override
    public List<FacilitiesDto> searchFacilities(String q) {
        return facilitiesRepository.searchFacilities(q);
    }

    @Override
    public FacilitiesDto getFacilitiesInfo(int facilityId) {
        return facilitiesRepository.getFacilitiesInfo(facilityId);
    }

    @Override
    public int insertFacilities(FacilitiesDto facilities) {
        return facilitiesRepository.insertFacilities(facilities);
    }

    @Override
    public int updateFacilities(FacilitiesDto facilities) {
        return facilitiesRepository.updateFacilities(facilities);
    }

    @Override
    @Transactional(readOnly = true)
    public FacilitiesDto findById(int facilityId) {
        return facilitiesRepository.getFacilitiesInfo(facilityId); // 없으면 null
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChecklistTemplateDto> findTemplatesByFacilityId(int facilityId) {
        return facilitiesRepository.findTemplatesByFacilityId(facilityId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InspectionDto> findInspectionByFacilityId(int facilityId) {
        return facilitiesRepository.findInspectionByFacilityId(facilityId);
    }

    @Override
    @Transactional
    public int deleteFacilities(int facilityId, String email) {
        return facilitiesRepository.deleteFacilities(facilityId, email);
    }

}

