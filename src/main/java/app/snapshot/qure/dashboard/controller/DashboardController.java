package app.snapshot.qure.dashboard.controller;

import app.snapshot.qure.dashboard.dto.DashboardSessionView;
import app.snapshot.qure.dashboard.dto.OtherInspectionStats;
import app.snapshot.qure.dashboard.dto.TodayInspectionStats;
import app.snapshot.qure.dashboard.service.IDashboardService;
import app.snapshot.qure.login.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    IDashboardService dashboardService;

    @GetMapping("")
    public String dashboardPage(HttpSession session, Model model) {
        String name = SessionUtil.getKakaoName(session);
        String email = SessionUtil.getKakaoEmail(session);
        DashboardSessionView vm = dashboardService.buildDashboardView(name, email);
        TodayInspectionStats todayStats = dashboardService.getTodayInspectionStats();
        OtherInspectionStats otherStats = dashboardService.getOtherInspectionStats();

        model.addAttribute("vm", vm);
        model.addAttribute("todayStats", todayStats);
        model.addAttribute("otherStats", otherStats);

        return "dashboard/dashboard"; // JSP 뷰
    }
}
