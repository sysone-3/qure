package app.snapshot.qure.facilities.repository;
import app.snapshot.qure.facilities.dto.FacilityTagDto;

public interface IFacilityTagRepository {
    int  getNextTagId();
    int  insertFacilityTagWithGivenId(FacilityTagDto tag);
    FacilityTagDto findActiveTagByFacilityId(int facilityId);
    FacilityTagDto findById(int tagId);
    int  deactivateActiveTags(int facilityId); // 필요 시 사용
}