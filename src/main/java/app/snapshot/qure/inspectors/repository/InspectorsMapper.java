package app.snapshot.qure.inspectors.repository;

import app.snapshot.qure.inspectors.dto.InspectorDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface InspectorsMapper {
    // 특정 매니저의 작업자 목록 페이징 조회
    List<InspectorDTO> findPagedByManager(Map<String, Object> params);

    // 특정 매니저의 작업자 총 수 카운트
    int countByManager(long managersId);
}
