package app.snapshot.qure.facilities.service;

import java.util.List;
import java.util.Map;

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
    @Transactional
    public int deleteFacilities(int facilityId, String email) {
        return facilitiesRepository.deleteFacilities(facilityId, email);
    }
}

