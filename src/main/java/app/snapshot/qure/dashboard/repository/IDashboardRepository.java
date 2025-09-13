package app.snapshot.qure.dashboard.repository;

import app.snapshot.qure.dashboard.dto.DailyCompletionPoint;
import app.snapshot.qure.dashboard.dto.OtherInspectionStats;
import app.snapshot.qure.dashboard.dto.RecentInspectionItem;
import app.snapshot.qure.dashboard.dto.TodayInspectionStats;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IDashboardRepository {
    TodayInspectionStats selectTodayInspectionStats();
    OtherInspectionStats selectOtherInspectionStats();
    int selectOpenComplaints();
    List<DailyCompletionPoint> selectRecentDailyCompletion(@Param("days") int days);
    List<RecentInspectionItem> selectRecentInspections(@Param("limit") int limit);
}
