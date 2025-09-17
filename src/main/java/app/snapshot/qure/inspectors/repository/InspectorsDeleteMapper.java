package app.snapshot.qure.inspectors.repository;
//작성자 : 구희원
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InspectorsDeleteMapper {
    void deleteInspector(String inspectorId);
}
