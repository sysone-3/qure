package app.snapshot.qure.inspectors.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import app.snapshot.qure.inspectors.service.InspectorDeleteService;

@Controller
@RequiredArgsConstructor
public class InspectorDeleteController {

    private final InspectorDeleteService inspectorDeleteService;

    @PostMapping("/inspectors/delete")
    public String deleteInspectors(
            @RequestParam("selectedIds") String selectedIds,
            RedirectAttributes redirectAttributes
    ) {
        String[] idsArray = selectedIds.split(",");
        inspectorDeleteService.deleteInspectors(idsArray);
        redirectAttributes.addFlashAttribute("message", "선택한 작업자가 삭제되었습니다.");
        return "redirect:/inspectors/inspectorsList"; // 삭제 후 목록으로 리다이렉트
    }
}
