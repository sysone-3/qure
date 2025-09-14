package app.snapshot.qure.inspection.repository;

import app.snapshot.qure.inspection.dto.InspectionHeaderDto;
import app.snapshot.qure.inspection.dto.InspectionItemDto;
import app.snapshot.qure.inspection.dto.InspectionResultItemDto;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface IInspectionRepository {
    List<InspectionItemDto> selectRecentInspections(@Param("limit") int limit, @Param("managerId") Long managerId);
    List<InspectionItemDto> selectInspectionsPage(@Param("keyword") String keyword,
                                                  @Param("startDate") Date startDate,
                                                  @Param("endDate") Date endDate,
                                                  @Param("status") String status,
                                                  @Param("startRow") int startRow,
                                                  @Param("endRow") int endRow,
                                                  @Param("managerId") Long managerId);

    int countInspections(@Param("keyword") String keyword,
                         @Param("startDate") Date startDate,
                         @Param("endDate") Date endDate,
                         @Param("status") String status,
                         @Param("managerId") Long managerId);

    InspectionHeaderDto selectInspectionHeader(@Param("inspectionId") Long inspectionId,  @Param("managerId") Long managerId);
    List<InspectionResultItemDto> selectInspectionItems(@Param("inspectionId") Long inspectionId,  @Param("managerId") Long managerId);
    List<String> selectImagesByResultId(@Param("resultId") Long resultId);
}
