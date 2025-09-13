package app.snapshot.qure.inspection.repository;

import app.snapshot.qure.inspection.dto.InspectionItemDto;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface IInspectionRepository {
    List<InspectionItemDto> selectRecentInspections(@Param("limit") int limit);
    List<InspectionItemDto> selectInspectionsPage(@Param("keyword") String keyword,
                                                  @Param("startDate") Date startDate,
                                                  @Param("endDate") Date endDate,
                                                  @Param("status") String status,
                                                  @Param("startRow") int startRow,
                                                  @Param("endRow") int endRow);

    int countInspections(@Param("keyword") String keyword,
                         @Param("startDate") Date startDate,
                         @Param("endDate") Date endDate,
                         @Param("status") String status);
}
