package app.snapshot.qure.inspectors.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InspectorsController {

    @GetMapping("/inspectors/inspectorsList")
    public String showInspectorList() {
        // /WEB-INF/views/inspectors/inspectorList.jsp 로 이동
        return "inspectors/inspectorsList";
    }
}