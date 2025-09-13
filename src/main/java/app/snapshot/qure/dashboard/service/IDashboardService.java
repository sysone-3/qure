package app.snapshot.qure.dashboard.service;

import app.snapshot.qure.dashboard.dto.*;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public interface IDashboardService {
    TodayInspectionStats getTodayInspectionStats();
    OtherInspectionStats getOtherInspectionStats();
    DashboardSessionView buildDashboardView(String name, String email);
    List<DailyCompletionPoint> getRecentDailyCompletion(int days);
    List<RecentInspectionItem> getRecentInspections(int limit);
}
