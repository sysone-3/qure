package app.snapshot.qure.dashboard.service;
// 작성자: 최이서

import app.snapshot.qure.dashboard.dto.*;
import app.snapshot.qure.dashboard.repository.IDashboardRepository;
import app.snapshot.qure.inspection.dto.InspectionItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService implements IDashboardService {

    @Autowired
    IDashboardRepository dashboardRepository;

    @Override
    public TodayInspectionStats getTodayInspectionStats(Long managerId) {
        TodayInspectionStats stats = dashboardRepository.selectTodayInspectionStats(managerId);
        if (stats == null) {
            // 기본값으로 채워서 Null 방지
            stats = new TodayInspectionStats();
            stats.setTodayTotal(0);
            stats.setTodayCompleted(0);
        }
        return stats;
    }

    @Override
    public OtherInspectionStats getOtherInspectionStats(Long managerId) {
        OtherInspectionStats stats = dashboardRepository.selectOtherInspectionStats(managerId);
        int complaints = dashboardRepository.selectOpenComplaints(managerId);
        stats.setOpenComplaints(complaints);
        return stats;
    }

    @Override
    public DashboardSessionView buildDashboardView(String name, String email, Long managerId) {
        TodayInspectionStats todayStats = getTodayInspectionStats(managerId);
        DashboardSessionView vm = new DashboardSessionView();
        vm.setName(name);
        vm.setEmail(email);
        vm.setTodayTotal(todayStats.getTodayTotal());
        vm.setTodayCompleted(todayStats.getTodayCompleted());
        return vm;
    }

    @Override
    public List<DailyCompletionPoint> getRecentDailyCompletion(int days, Long managerId) {
        return dashboardRepository.selectRecentDailyCompletion(days, managerId);
    }
}
