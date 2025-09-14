package app.snapshot.qure.dashboard.service;

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
        return dashboardRepository.selectTodayInspectionStats(managerId);
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
        TodayInspectionStats todayStats = dashboardRepository.selectTodayInspectionStats(managerId);
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
