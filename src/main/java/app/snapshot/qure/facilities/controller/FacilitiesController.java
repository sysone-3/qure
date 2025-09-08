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
            @PathVariable("id") int id, Model model, RedirectAttributes ra) {

        FacilitiesDto facility = facilitiesService.findById(id);
        if (facility == null) {
            ra.addFlashAttribute("msg", "해당 설비를 찾을 수 없습니다. (ID: " + id + ")");
            return "redirect:/facilities";
        }

        List<InspectionDto> inspections = facilitiesService.findInspectionByFacilityId(id);
        // 설비별 템플릿 데이터
        List<ChecklistTemplateDto> templates = facilitiesService.findTemplatesByFacilityId(id);

        model.addAttribute("facility", facility);
        model.addAttribute("templates", templates);
          model.addAttribute("inspections", inspections);

        return "facilities/facilities_detail"; // /WEB-INF/views/facilities/facilities_detail.jsp
    }

    /** (선택) POST로 상세를 열어야 할 폼이 있다면 이쪽으로 위임 */
    @PostMapping("/{id}/detail")
    public String detailPost(@PathVariable("id") int id) {
        return "redirect:/facilities/" + id;
    }


}
