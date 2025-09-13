package app.snapshot.qure.inspectors.repository;

import app.snapshot.qure.inspectors.dto.InspectorDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Mapper
public interface InspectorsMapper {
    List<InspectorDTO> findPaged(@Param("startRow") int startRow, @Param("endRow") int endRow);
    int countAll();
}
