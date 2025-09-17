package app.snapshot.qure.inspectors.controller;
//작성자 : 구희원
import app.snapshot.qure.inspectors.dto.InspectorDTO;
import app.snapshot.qure.inspectors.service.InspectorsService;
import app.snapshot.qure.login.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@Controller
public class InspectorsController {

    private final InspectorsService inspectorsService;

    public InspectorsController(InspectorsService inspectorsService) {
        this.inspectorsService = inspectorsService;
    }

    @GetMapping("/inspectors/inspectorsList")
    public String showInspectorList(@RequestParam(defaultValue = "1") int page,
                                    Model model, HttpSession session) {
        int size = 15; // 한 페이지 데이터 수
        Long managersId = SessionUtil.getManagerId(session);
        System.out.println("세션 managersId: " + managersId);
        if (managersId == null) {
            return "redirect:/dashboard";
        }

        List<InspectorDTO> inspectors = inspectorsService.getPagedInspectors(page, size,managersId);
        int totalCount = inspectorsService.getTotalCount(Math.toIntExact(managersId));
        int totalPages = (int) Math.ceil((double) totalCount / size);
        int pageBlock = 3;
        int currentBlock = (int) Math.ceil((double) page / pageBlock);

        int startPage = (currentBlock - 1) * pageBlock + 1;
        int endPage = startPage + pageBlock - 1;
        if (endPage > totalPages) {
            endPage = totalPages;
        }

        model.addAttribute("inspectors", inspectors);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "inspectors/inspectorsList";
    }
}