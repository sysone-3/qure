package app.snapshot.qure.inspectors.repository;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InspectorsDeleteMapper {
    void deleteInspector(String inspectorId);
}
