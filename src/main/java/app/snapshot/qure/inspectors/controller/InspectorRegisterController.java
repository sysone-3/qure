package app.snapshot.qure.inspectors.controller;

import app.snapshot.qure.inspectors.dto.InspectorRegisterDTO;
import app.snapshot.qure.facilities.dto.FacilityRegisterDTO;
import app.snapshot.qure.inspectors.service.InspectorRegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@RequiredArgsConstructor
public class InspectorRegisterController {

    private final InspectorRegisterService inspectorRegisterService;

    @PostMapping("/inspectors/register")
    public String registerInspectorWithFacility(
            @ModelAttribute InspectorRegisterDTO inspectorDto,
            @ModelAttribute FacilityRegisterDTO facilityDto) {

        inspectorRegisterService.registerInspectorWithFacility(inspectorDto, facilityDto);
        return "redirect:/inspectors/inspectorsList";
    }
}
