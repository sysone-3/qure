package app.snapshot.qure.inspection.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/inspection")
public class InspectionController {

    @GetMapping("")
    public String getInspectionList() {
        return "inspection/inspectionList";
    }
}
