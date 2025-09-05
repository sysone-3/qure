package app.snapshot.qure.template.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TemplateController {

    @GetMapping("/template/list")
    public String showList() {
        return "template/list"; // /WEB-INF/classes/views/template/list.jsp
    }

    @GetMapping("/template/create")
    public String showCreate() {
        return "template/create"; // /WEB-INF/classes/views/template/create.jsp
    }
}
