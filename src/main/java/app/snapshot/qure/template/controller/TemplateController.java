package app.snapshot.qure.template.controller;

import app.snapshot.qure.template.model.Template;
import app.snapshot.qure.template.service.ITemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/template")
public class TemplateController {

    @Autowired
    ITemplateService templateService;

    @RequestMapping(value="")
    public String getAllTemplates(Model model){
        List<Template> list = templateService.getTemplateList();
        model.addAttribute("templateList", list);

        return "template/templateList";
    }

    // 등록 폼 (GET)
    @RequestMapping(value = "/insert", method = RequestMethod.GET)
    public String showInsertForm(Model model) {
        model.addAttribute("template", new Template());
        return "template/insertForm";  // -> template/insertForm.jsp
    }

    // 등록 처리 (POST)
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public String insertTemplate(Template template, RedirectAttributes redirectAttributes) {
        try {
            templateService.insertTemplate(template);
            redirectAttributes.addFlashAttribute("message",
                    template.getTemplateId() + " 번 점검표가 등록되었습니다.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:/template/list";
    }

    // 단건 조회 + 수정 폼 (같은 화면)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getTemplateAndEdit(@PathVariable Long id, Model model) {
        Template template = templateService.getTemplateById(id);
        model.addAttribute("template", template);
        return "template/viewForm";  // 조회 + 수정 통합 화면
    }

    // 수정 처리 (POST)
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateTemplate(Template template, RedirectAttributes redirectAttributes) {
        try {
            templateService.updateTemplate(template);
            redirectAttributes.addFlashAttribute("message",
                    template.getTemplateId() + " 번 점검표가 수정되었습니다.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:/template/list";
    }

    // 삭제 처리 (POST)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deleteTemplate(Long id, RedirectAttributes redirectAttributes) {
        try {
            templateService.deleteTemplate(id);
            redirectAttributes.addFlashAttribute("message",
                    id + " 번 점검표가 삭제되었습니다.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:/template/list";
    }
}
