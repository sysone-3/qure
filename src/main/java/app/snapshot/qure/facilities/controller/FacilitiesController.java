package app.snapshot.qure.facilities.controller;


import app.snapshot.qure.facilities.dto.ChecklistTemplateDto;
import app.snapshot.qure.facilities.dto.FacilitiesDto;
import app.snapshot.qure.facilities.dto.FacilityTagDto;
import app.snapshot.qure.facilities.dto.InspectionDto;
import app.snapshot.qure.facilities.service.IFacilitiesService;
import jakarta.servlet.http.HttpSession;
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

    @Autowired private IFacilitiesService facilitiesService;

    private int mustManagerId() {
        // TODO: 로그인 붙이기 전 임시 값
        return 1;
    }

//    private Integer mustManagerId(HttpSession session) {
//        return (Integer) session.getAttribute("LOGIN_MANAGER_ID");
//    }

    // 목록 + 검색
    @GetMapping
    public String list(@RequestParam(value = "q", required = false) String q,
                       Model model, HttpSession session) {
        int managerId = mustManagerId();
        List<FacilitiesDto> facilities = (q == null || q.isBlank())
                ? facilitiesService.getFacilitiesListByManager(managerId)
                : facilitiesService.searchFacilitiesByManager(managerId, q.trim());
        model.addAttribute("facilities", facilities);
        return "facilities/facilities";
    }

    // 설비 등록 폼
    @GetMapping("/new")
    public String addForm(Model model) {
        model.addAttribute("facility", new FacilitiesDto());
        return "facilities/facilities_add";
    }

    // 등록 + QR 발급 (로그인한 관리자 소유로 저장)
    @PostMapping
    public String create(@ModelAttribute FacilitiesDto dto,
                         RedirectAttributes ra,
                         HttpSession session) {
        int managerId = mustManagerId();
        int facilityId = facilitiesService.createFacilityWithQr(dto, managerId);
        ra.addFlashAttribute("msg", "설비가 등록되었습니다. QR을 발급했어요.");
        return "redirect:/facilities/" + facilityId + "/qr";
    }

    // 등록 직후 QR 확인 페이지
    @GetMapping("/{facilityId}/qr")
    public String showQr(@PathVariable int facilityId, Model model, RedirectAttributes ra) {
        int managerId = mustManagerId();

        var facility = facilitiesService.findByfacilityIdAndManager(facilityId, managerId);
        if (facility == null) {
            ra.addFlashAttribute("msg", "설비를 찾을 수 없습니다.");
            return "redirect:/facilities";
        }
        FacilityTagDto tag = facilitiesService.findActiveTagByFacilityId(facilityId);
        model.addAttribute("facility", facility);
        model.addAttribute("tag", tag);
        return "facilities/facility_qr";
    }

    // 설비 수정
    @GetMapping("/{facilityId}/edit")
    public String editForm(@PathVariable("facilityId") int facilityId, Model model, RedirectAttributes ra) {
        int managerId = mustManagerId();

        FacilitiesDto facility = facilitiesService.findByfacilityIdAndManager(facilityId, managerId);
        if (facility == null) {
            ra.addFlashAttribute("msg", "해당 설비가 없습니다. (ID: " + facilityId + ")");
            return "redirect:/facilities";
        }
        model.addAttribute("facility", facility);
        return "facilities/facilities_edit"; // JSP 경로
    }


    // 상세 (본인 소유만 접근)
    @GetMapping("/{facilityId}")
    public String detail(@PathVariable int facilityId,
                         @RequestParam(required = false) String start,
                         @RequestParam(required = false) String end,
                         Model model, RedirectAttributes ra, HttpSession session) {

        int managerId = mustManagerId();
        FacilitiesDto facility = facilitiesService.findByfacilityIdAndManager(facilityId, managerId);
        if (facility == null) {
            ra.addFlashAttribute("msg", "접근 권한이 없거나 존재하지 않는 설비입니다.");
            return "redirect:/facilities";
        }

        // 날짜 보정 동일 …
        LocalDate startD = (start != null && !start.isBlank()) ? LocalDate.parse(start) : null;
        LocalDate endD   = (end   != null && !end.isBlank())   ? LocalDate.parse(end)   : null;
        if (startD == null && endD == null) { endD = LocalDate.now(); startD = endD.minusDays(30); }
        else if (startD == null) { startD = endD.minusDays(30); }
        else if (endD == null) { endD = startD.plusDays(30); }
        if (endD.isBefore(startD)) { LocalDate t = startD; startD = endD; endD = t; }

        var inspections = facilitiesService
                .findInspectionByFacilityIdAndPeriodAndManager(facilityId, startD, endD, managerId);
        var templates = facilitiesService.findTemplatesByFacilityIdAndManager(facilityId, managerId);

        var tag = facilitiesService.findActiveTagByFacilityId(facilityId); // 소유 확인은 위에서 끝

        model.addAttribute("facility", facility);
        model.addAttribute("inspections", inspections);
        model.addAttribute("templates", templates);
        model.addAttribute("tag", tag);
        model.addAttribute("start", startD.toString());
        model.addAttribute("end", endD.toString());
        return "facilities/facilities_detail";
    }

    // 수정/삭제도 동일하게 managerId로 가드
    @PostMapping("/{id}")
    public String edit(@PathVariable("id") int facilityId,
                       @ModelAttribute("facility") FacilitiesDto facility,
                       RedirectAttributes ra, HttpSession session) {
        int managerId = mustManagerId();
        facility.setFacilityId(facilityId);
        int rows = facilitiesService.updateFacilitiesByManager(facility, managerId);
        ra.addFlashAttribute("msg", rows > 0 ? "설비가 수정되었습니다." : "수정 권한이 없거나 실패했습니다.");
        return "redirect:/facilities/" + facilityId;
    }

    @PostMapping("/{facilityId}/delete")
    public String delete(@PathVariable int facilityId, RedirectAttributes ra, HttpSession session) {
        int managerId = mustManagerId();
        int cnt = facilitiesService.deleteFacilitiesByManager(facilityId, managerId);
        ra.addFlashAttribute("msg", cnt > 0 ? "삭제되었습니다." : "삭제 권한이 없거나 데이터가 없습니다.");
        return "redirect:/facilities";
    }
}
