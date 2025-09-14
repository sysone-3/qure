package app.snapshot.qure.inspection.controller;

import app.snapshot.qure.inspection.dto.InspectionDetailDto;
import app.snapshot.qure.inspection.dto.InspectionItemDto;
import app.snapshot.qure.inspection.service.IInspectionService;
import app.snapshot.qure.login.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/inspection")
public class InspectionController {

    @Autowired
    IInspectionService inspectionService;

    @GetMapping("/list")
    public String getInspectionList(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(defaultValue = "") String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "7") int size,
            HttpSession session,
            Model model) {
        Long managerId = SessionUtil.getManagerId(session);
        List<InspectionItemDto> inspections =
                inspectionService.getInspections(keyword, startDate, endDate, status, page, size, managerId);

        int total = inspectionService.countInspections(keyword, startDate, endDate, status, managerId);
        int totalPages = (int) Math.ceil((double) total / size);

        model.addAttribute("inspections", inspections);
        model.addAttribute("page", page);
        model.addAttribute("totalPages", totalPages);

        return "inspection/inspectionList";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam Long inspectionId, HttpSession session, Model model) {
        Long managerId = SessionUtil.getManagerId(session);
        InspectionDetailDto detail = inspectionService.getInspectionDetail(inspectionId, managerId);
        if (detail == null) {
            return "error/404";
        }

        String submittedAtStr = "-";
        Object ts = detail.getSubmittedAt();
        try {
            if (ts instanceof java.time.LocalDateTime) {
                submittedAtStr = ((java.time.LocalDateTime) ts)
                        .format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분"));
            } else if (ts instanceof Date) {
                submittedAtStr = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분")
                        .format((Date) ts);
            }
        } catch (Exception ignore) {}

        model.addAttribute("detail", detail);
        model.addAttribute("submittedAtStr", submittedAtStr);
        return "inspection/inspectionDetail";
    }
}
