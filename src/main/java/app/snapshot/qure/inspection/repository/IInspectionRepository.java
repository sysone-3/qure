package app.snapshot.qure.inspection.repository;

import app.snapshot.qure.inspection.dto.InspectionItemDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IInspectionRepository {
    List<InspectionItemDto> selectRecentInspections(@Param("limit") int limit);

}
