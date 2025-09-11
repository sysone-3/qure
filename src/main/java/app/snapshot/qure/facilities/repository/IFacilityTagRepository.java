package app.snapshot.qure.facilities.repository;
import app.snapshot.qure.facilities.dto.FacilityTagDto;

public interface IFacilityTagRepository {
    Long  getNextTagId();
    int  insertFacilityTagWithGivenId(FacilityTagDto tag);
    FacilityTagDto findActiveTagByFacilityId(Long facilityId);
    FacilityTagDto findById(Long tagId);
    int  deactivateActiveTags(Long facilityId); // 필요 시 사용
}