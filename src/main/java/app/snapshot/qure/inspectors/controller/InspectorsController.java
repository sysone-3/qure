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
                                    Model model
    ) {
        int size = 15;
        List<InspectorDTO> inspectors = inspectorsService.getPagedInspectors(page,size);
        int totalCount = inspectorsService.getTotalCount();
        int totalPages = (int)Math.ceil((double) totalCount/size);

        model.addAttribute("inspectors", inspectors);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalCount",totalCount);
        return "inspectors/inspectorsList";
    }
}