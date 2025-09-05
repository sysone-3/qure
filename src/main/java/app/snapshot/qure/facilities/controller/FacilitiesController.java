package app.snapshot.qure.facilities.controller;


import app.snapshot.qure.facilities.dto.FacilitiesDto;
import app.snapshot.qure.facilities.service.IFacilitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        return "facilities/index"; // /WEB-INF/views/facilities/list.jsp
    }

    // 삭제
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id, RedirectAttributes ra) {
        int cnt = facilitiesService.deleteFacilities(id, null);
        ra.addFlashAttribute("msg", cnt > 0 ? "삭제되었습니다." : "삭제할 데이터가 없습니다.");
        return "redirect:/facilities";
    }
}
