package app.snapshot.qure.facilities.controller;


import app.snapshot.qure.facilities.dto.ChecklistTemplateDto;
import app.snapshot.qure.facilities.dto.FacilitiesDto;
import app.snapshot.qure.facilities.dto.InspectionDto;
import app.snapshot.qure.facilities.service.IFacilitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Controller
@RequestMapping("/facilities")
public class FacilitiesController {

    @Autowired
    private IFacilitiesService facilitiesService;

    // 목록 + 검색
    @GetMapping
    public String list(@RequestParam(value = "q", required = false) String q, Model model) {
        List<FacilitiesDto> facilities =
                (q == null || q.isBlank())
                        ? facilitiesService.getFacilitiesList()
                        : facilitiesService.searchFacilities(q.trim());
        model.addAttribute("facilities", facilities);
        return "facilities/facilities"; // /WEB-INF/views/facilities/facilities.jsp
    }

    // 설비 등록
    @GetMapping("/new")
    public String addForm(Model model) {
        model.addAttribute("facility", new FacilitiesDto()); // 빈 폼 바인딩
        return "facilities/facilities_add";
    }

    @PostMapping
    public String create(@ModelAttribute("facility") FacilitiesDto facility,
                         RedirectAttributes ra) {
        int rows = facilitiesService.insertFacilities(facility);
        if (rows > 0) {
            ra.addFlashAttribute("msg", "설비가 등록되었습니다.");
            // insert 시 selectKey로 facilityId가 채워짐
            return "redirect:/facilities/" + facility.getFacilityId();
        } else {
            ra.addFlashAttribute("msg", "등록에 실패했습니다. 다시 시도해주세요.");
            return "redirect:/facilities/new";
        }
    }


    // 삭제
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id, RedirectAttributes ra) {
        int cnt = facilitiesService.deleteFacilities(id, null);
        ra.addFlashAttribute("msg", cnt > 0 ? "삭제되었습니다." : "삭제할 데이터가 없습니다.");
        return "redirect:/facilities";
    }

    // 상세 조회
    @GetMapping("/{id}")
    public String detail(
            @PathVariable("id") int id,
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end,
            Model model, RedirectAttributes ra) {

        FacilitiesDto facility = facilitiesService.findById(id);
        if (facility == null) {
            ra.addFlashAttribute("msg", "해당 설비를 찾을 수 없습니다. (ID: " + id + ")");
            return "redirect:/facilities";
        }

        // 파라미터 보정 (한쪽만 들어와도 안전)
        LocalDate startD = (start != null && !start.isBlank()) ? LocalDate.parse(start) : null;
        LocalDate endD   = (end   != null && !end.isBlank())   ? LocalDate.parse(end)   : null;

        if (startD == null && endD == null) {
            endD   = LocalDate.now();
            startD = endD.minusDays(30);
        } else if (startD == null) {
            startD = endD.minusDays(30);
        } else if (endD == null) {
            endD = startD.plusDays(30);
        }
        if (endD.isBefore(startD)) { // 역전 방지
            LocalDate tmp = startD; startD = endD; endD = tmp;
        }
        ZoneId zone = ZoneId.systemDefault();
        Timestamp startTs    = Timestamp.from(startD.atStartOfDay(zone).toInstant());
        Timestamp endTsPlus1 = Timestamp.from(endD.plusDays(1).atStartOfDay(zone).toInstant());

        List<InspectionDto> inspections =
                facilitiesService.findInspectionByFacilityIdAndPeriod(id, startD, endD);

        // 나머지 그대로
        List<ChecklistTemplateDto> templates = facilitiesService.findTemplatesByFacilityId(id);
        model.addAttribute("facility", facility);
        model.addAttribute("templates", templates);
        model.addAttribute("inspections", inspections);
        model.addAttribute("start", startD.toString());
        model.addAttribute("end",   endD.toString());
        return "facilities/facilities_detail";
    }


}
