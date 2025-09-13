package app.snapshot.qure.dashboard.service;

import app.snapshot.qure.dashboard.dto.OtherInspectionStats;
import app.snapshot.qure.dashboard.dto.DashboardSessionView;
import app.snapshot.qure.dashboard.dto.TodayInspectionStats;
import app.snapshot.qure.dashboard.repository.IDashboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardService implements IDashboardService {

    @Autowired
    IDashboardRepository dashboardRepository;

    @Override
    public TodayInspectionStats getTodayInspectionStats() {
        return dashboardRepository.selectTodayInspectionStats();
    }

    @Override
    public OtherInspectionStats getOtherInspectionStats() {
        OtherInspectionStats stats = dashboardRepository.selectOtherInspectionStats();
        int complaints = dashboardRepository.selectOpenComplaints();
        stats.setOpenComplaints(complaints);
        return stats;
    }

    @Override
    public DashboardSessionView buildDashboardView(String name, String email) {
        TodayInspectionStats todayStats = dashboardRepository.selectTodayInspectionStats();

        DashboardSessionView vm = new DashboardSessionView();
        vm.setName(name);
        vm.setEmail(email);
        vm.setTodayTotal(todayStats.getTodayTotal());
        vm.setTodayCompleted(todayStats.getTodayCompleted());
        return vm;
    }
}
