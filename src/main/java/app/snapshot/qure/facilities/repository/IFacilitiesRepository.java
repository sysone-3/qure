package app.snapshot.qure.facilities.repository;
import java.util.List;
import java.util.Map;

import app.snapshot.qure.facilities.dto.FacilitiesDto;

public interface IFacilitiesRepository {
    List<FacilitiesDto> getFacilitiesList();
    List<FacilitiesDto> searchFacilities(String q);
    FacilitiesDto getFacilitiesInfo(int facilityId);

    int insertFacilities(FacilitiesDto facilities);
    int updateFacilities(FacilitiesDto facilities);
    int deleteFacilities(int facilityId, String email); // email은 미사용
}
