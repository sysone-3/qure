package app.snapshot.qure.inspectors.controller;

import app.snapshot.qure.inspectors.dto.InspectorDetailDTO;
import app.snapshot.qure.inspectors.repository.InspectorsDetailMapper;
import app.snapshot.qure.inspectors.service.InspectorDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/inspectors")
@RequiredArgsConstructor
public class InspectorDetailController {

    private final InspectorDetailService inspectorDetailService;

    @GetMapping("/detail/{id}")
    public String inspectorDetail(@PathVariable("id") int id, Model model) {
        List<InspectorDetailDTO> details = inspectorDetailService.getInspectorDetail(id);

        if (!details.isEmpty()) {
            model.addAttribute("inspectorName", details.get(0).getInspectorName());
            model.addAttribute("phone", details.get(0).getPhone());
            model.addAttribute("facilities", details);
        }

        return "inspectors/inspectorDetail";
    }
}