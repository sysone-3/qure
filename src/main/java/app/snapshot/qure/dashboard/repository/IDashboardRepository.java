package app.snapshot.qure.dashboard.repository;

import app.snapshot.qure.dashboard.dto.OtherInspectionStats;
import app.snapshot.qure.dashboard.dto.TodayInspectionStats;

public interface IDashboardRepository {
    TodayInspectionStats selectTodayInspectionStats();
    OtherInspectionStats selectOtherInspectionStats();
    int selectOpenComplaints();
}
