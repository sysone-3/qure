package app.snapshot.qure.facilities.controller;


import app.snapshot.qure.facilities.dto.ChecklistTemplateDto;
import app.snapshot.qure.facilities.dto.FacilitiesDto;
import app.snapshot.qure.facilities.dto.FacilityTagDto;
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

    // 등록 + QR 발급
    @PostMapping
    public String create(@ModelAttribute FacilitiesDto dto, RedirectAttributes ra) {
        int facilityId = facilitiesService.createFacilityWithQr(dto);
        ra.addFlashAttribute("msg", "설비가 등록되었습니다. QR을 발급했어요.");
        return "redirect:/facilities/" + facilityId + "/qr";
    }

    // 등록 직후 QR 확인 페이지
    @GetMapping("/{id}/qr")
    public String showQr(@PathVariable int id, Model model, RedirectAttributes ra) {
        var facility = facilitiesService.findById(id);
        if (facility == null) {
            ra.addFlashAttribute("msg", "설비를 찾을 수 없습니다.");
            return "redirect:/facilities";
        }
        FacilityTagDto tag = facilitiesService.findActiveTagByFacilityId(id);
        model.addAttribute("facility", facility);
        model.addAttribute("tag", tag);
        return "facilities/facility_qr";
    }

    // 설비 수정
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") int id, Model model, RedirectAttributes ra) {
        FacilitiesDto facility = facilitiesService.findById(id);
        if (facility == null) {
            ra.addFlashAttribute("msg", "해당 설비가 없습니다. (ID: " + id + ")");
            return "redirect:/facilities";
        }
        model.addAttribute("facility", facility);
        return "facilities/facilities_edit"; // JSP 경로
    }

    // 저장
    @PostMapping("/{id}")
    public String edit(@PathVariable("id") int id,
                       @ModelAttribute("facility") FacilitiesDto facility,
                       RedirectAttributes ra) {
        facility.setFacilityId(id); // URL 우선
        int rows = facilitiesService.updateFacilities(facility);
        if (rows > 0) {
            ra.addFlashAttribute("msg", "설비가 수정되었습니다.");
            return "redirect:/facilities/" + id;
        } else {
            ra.addFlashAttribute("msg", "수정에 실패했습니다. 다시 시도해주세요.");
            return "redirect:/facilities/" + id + "/edit";
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

        // qr 다운로드 관련
        FacilityTagDto tag = facilitiesService.findActiveTagByFacilityId(id);
        model.addAttribute("tag", tag);

        List<ChecklistTemplateDto> templates = facilitiesService.findTemplatesByFacilityId(id);
        model.addAttribute("facility", facility);
        model.addAttribute("templates", templates);
        model.addAttribute("inspections", inspections);
        model.addAttribute("start", startD.toString());
        model.addAttribute("end",   endD.toString());
        return "facilities/facilities_detail";
    }


}
