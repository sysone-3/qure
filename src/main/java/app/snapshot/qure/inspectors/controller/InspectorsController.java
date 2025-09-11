package app.snapshot.qure.inspectors.controller;
import app.snapshot.qure.inspectors.service.InspectorsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InspectorsController {

    private final InspectorsService inspectorsService;

    public InspectorsController(InspectorsService inspectorsService) {
        this.inspectorsService = inspectorsService;
    }

    @GetMapping("/inspectors/inspectorsList")
    public String showInspectorList(Model model
    ) {
        model.addAttribute("inspectors",inspectorsService.getAllInspectors());
        return "inspectors/inspectorsList";
    }
}