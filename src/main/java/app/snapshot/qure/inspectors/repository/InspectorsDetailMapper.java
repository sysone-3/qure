package app.snapshot.qure.inspectors.repository;

import app.snapshot.qure.inspectors.dto.InspectorDetailDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InspectorsDetailMapper {
    List<InspectorDetailDTO> getInspectorDetail(@Param("inspectorId") int inspectorId);
}
