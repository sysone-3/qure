package app.snapshot.qure.template.controller;

import app.snapshot.qure.checklist.dto.TemplateCreateForm;
import app.snapshot.qure.checklist.model.Checklist;
import app.snapshot.qure.checklist.service.IChecklistService;
import app.snapshot.qure.template.dto.TemplateUpdateForm;
import app.snapshot.qure.template.model.Template;
import app.snapshot.qure.template.service.ITemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/template")
public class TemplateController {

    @Autowired
    ITemplateService templateService;

    @Autowired
    IChecklistService checklistService;

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
    public String insertTemplate(TemplateCreateForm form, RedirectAttributes redirectAttributes) {
        try {
            long templateId = templateService.insertTemplate(form);
            redirectAttributes.addFlashAttribute("message",
                    templateId + " 번 점검표가 등록되었습니다.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:/template";
    }

    // 단건 조회 + 수정 폼 (같은 화면)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getTemplateAndEdit(@PathVariable Long id, Model model) {
        Template template = templateService.getTemplateById(id);
        List<Checklist> items = checklistService.getByTemplateId(id);

        model.addAttribute("template", template);
        model.addAttribute("items", items);
        return "template/editTemplate";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateAsNewVersion(TemplateUpdateForm form, RedirectAttributes ra) {
        long newId = templateService.saveAsNewVersion(form);
        ra.addFlashAttribute("message", "새 버전(" + newId + ")으로 저장되었습니다.");
        return "redirect:/template"; // or "redirect:/template/" + newId
    }

    // 삭제 처리 (POST)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deleteTemplate(@RequestParam("templateId") Long id, RedirectAttributes redirectAttributes) {
        try {
            templateService.deleteTemplate(id);
            redirectAttributes.addFlashAttribute("message",
                    id + " 번 점검표가 삭제되었습니다.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:/template";
    }
}
