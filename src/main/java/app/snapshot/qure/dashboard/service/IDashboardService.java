package app.snapshot.qure.dashboard.service;

import app.snapshot.qure.dashboard.dto.OtherInspectionStats;
import app.snapshot.qure.dashboard.dto.DashboardSessionView;
import app.snapshot.qure.dashboard.dto.TodayInspectionStats;
import jakarta.servlet.http.HttpSession;

public interface IDashboardService {
    TodayInspectionStats getTodayInspectionStats();
    OtherInspectionStats getOtherInspectionStats();
    DashboardSessionView buildDashboardView(String name, String email);
}
