package app.snapshot.qure.template.controller;

import app.snapshot.qure.template.model.Template;
import app.snapshot.qure.template.service.ITemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
public class TemplateController {

    @Autowired
    ITemplateService templateService;

    @RequestMapping(value="/template")
    public String getAllTemplates(Model model){
        List<Template> list = templateService.getTemplateList();
        model.addAttribute("templateList", list);

        return "template/templateList";
    }
}
