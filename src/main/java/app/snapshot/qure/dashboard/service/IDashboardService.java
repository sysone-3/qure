package app.snapshot.qure.dashboard.service;
// 작성자: 최이서

import app.snapshot.qure.dashboard.dto.*;
import app.snapshot.qure.inspection.dto.InspectionItemDto;

import java.util.List;

public interface IDashboardService {
    TodayInspectionStats getTodayInspectionStats(Long managerId);
    OtherInspectionStats getOtherInspectionStats(Long managerId);
    DashboardSessionView buildDashboardView(String name, String email, Long managerId);
    List<DailyCompletionPoint> getRecentDailyCompletion(int days, Long managerId);
}
