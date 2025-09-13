package app.snapshot.qure.inspection.controller;

import app.snapshot.qure.inspection.dto.InspectionItemDto;
import app.snapshot.qure.inspection.service.IInspectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
            @RequestParam(defaultValue = "8") int size,
            Model model) {

        List<InspectionItemDto> inspections =
                inspectionService.getInspections(keyword, startDate, endDate, status, page, size);

        int total = inspectionService.countInspections(keyword, startDate, endDate, status);
        int totalPages = (int) Math.ceil((double) total / size);

        model.addAttribute("inspections", inspections);
        model.addAttribute("page", page);
        model.addAttribute("totalPages", totalPages);

        return "inspection/inspectionList";
    }
}
