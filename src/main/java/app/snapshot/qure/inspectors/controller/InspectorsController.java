package app.snapshot.qure.inspectors.controller;
import app.snapshot.qure.inspectors.dto.InspectorDTO;
import app.snapshot.qure.inspectors.service.InspectorsService;
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
                                    Model model) {
        int size = 15; // 한 페이지 데이터 수
        List<InspectorDTO> inspectors = inspectorsService.getPagedInspectors(page, size);
        int totalCount = inspectorsService.getTotalCount();
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