package app.snapshot.qure.dashboard.repository;

import app.snapshot.qure.dashboard.dto.DailyCompletionPoint;
import app.snapshot.qure.dashboard.dto.OtherInspectionStats;
import app.snapshot.qure.inspection.dto.InspectionItemDto;
import app.snapshot.qure.dashboard.dto.TodayInspectionStats;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IDashboardRepository {
    TodayInspectionStats selectTodayInspectionStats(@Param("managerId") Long managerId);
    OtherInspectionStats selectOtherInspectionStats(@Param("managerId") Long managerId);
    int selectOpenComplaints(@Param("managerId") Long managerId);
    List<DailyCompletionPoint> selectRecentDailyCompletion(@Param("days") int days,
                                                           @Param("managerId") Long managerId);
}
