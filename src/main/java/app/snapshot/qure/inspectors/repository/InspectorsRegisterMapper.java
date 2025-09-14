package app.snapshot.qure.inspectors.repository;

import app.snapshot.qure.inspectors.dto.InspectorRegisterDTO;
import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;

@Mapper
public interface InspectorsRegisterMapper {
    void insertInspector(InspectorRegisterDTO dto);
    int getLastInsertedId(); // 시퀀스 currval 가져오기용

    Integer findInspectorIdByNameAndPhone(
            @Param("name") String name,
            @Param("phone") String phone
    );
}
