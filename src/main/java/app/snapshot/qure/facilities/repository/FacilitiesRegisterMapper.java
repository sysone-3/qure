package app.snapshot.qure.facilities.repository;

import app.snapshot.qure.facilities.dto.FacilityRegisterDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FacilitiesRegisterMapper {
    void insertFacility(FacilityRegisterDTO dto);
}
