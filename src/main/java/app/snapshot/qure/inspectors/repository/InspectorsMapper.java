package app.snapshot.qure.inspectors.repository;

import app.snapshot.qure.inspectors.dto.InspectorDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InspectorsMapper {
    List<InspectorDTO> findAll();
}
