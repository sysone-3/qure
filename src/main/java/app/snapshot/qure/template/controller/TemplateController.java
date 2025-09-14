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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/template")
public class TemplateController {

    @Autowired ITemplateService templateService;
    @Autowired IChecklistService checklistService;

    /** 목록 */
    @RequestMapping(value = "")
    public String getAllTemplates(Model model) {
        List<Template> list = templateService.getTemplateList();
        model.addAttribute("templateList", list);
        return "template/templateList";
    }

    /** (신규) 선택 팝업: /template/select?popup=true[&q=] */
    @GetMapping("/select")
    public String selectPopup(@RequestParam(value = "q", required = false) String q,
                              @RequestParam(value = "popup", required = false, defaultValue = "false") boolean popup,
                              Model model) {
        List<Template> list = templateService.getTemplateList();

        model.addAttribute("templateList", list);
        model.addAttribute("q", q);

        // 팝업 모드면 팝업 전용 JSP를, 아니면 기존 리스트 화면을 재사용
        return popup ? "template/select_popup" : "template/templateList";
    }

    /** 등록 폼 (GET) — 팝업 모드 지원 */
    @RequestMapping(value = "/insert", method = RequestMethod.GET)
    public String showInsertForm(@RequestParam(value = "popup", required = false, defaultValue = "false") boolean popup,
                                 @RequestParam(value = "returnUrl", required = false) String returnUrl,
                                 Model model) {
        model.addAttribute("template", new Template());
        model.addAttribute("popup", popup);
        model.addAttribute("returnUrl", returnUrl); // 팝업에서 만든 뒤 돌아갈 URL (예: /template/select?popup=true)
        return "template/insertForm";
    }

    /** 등록 처리 (POST) — 팝업 모드면 returnUrl로 되돌아가게 */
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public String insertTemplate(TemplateCreateForm form,
                                 @RequestParam(value = "popup", required = false, defaultValue = "false") boolean popup,
                                 @RequestParam(value = "returnUrl", required = false) String returnUrl,
                                 RedirectAttributes ra) {
        try {
            long templateId = templateService.insertTemplate(form);
            ra.addFlashAttribute("message", templateId + " 번 점검표가 등록되었습니다.");

            if (popup && returnUrl != null && !returnUrl.isBlank()) {
                // 선택 팝업으로 되돌려 최신 목록에서 방금 만든 템플릿을 선택할 수 있게
                return "redirect:" + returnUrl;
            }
            return "redirect:/template";
        } catch (RuntimeException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
            // 실패 시에도 팝업이면 팝업으로 되돌림
            if (popup && returnUrl != null && !returnUrl.isBlank()) {
                return "redirect:" + returnUrl;
            }
            return "redirect:/template";
        }
    }

    /** 단건 조회 + 수정 폼 */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getTemplateAndEdit(@PathVariable Long id, Model model) {
        Template template = templateService.getTemplateById(id);
        List<Checklist> items = checklistService.getByTemplateId(id);

        model.addAttribute("template", template);
        model.addAttribute("items", items);
        return "template/editTemplate";
    }

    /** 새 버전으로 저장 */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateAsNewVersion(TemplateUpdateForm form, RedirectAttributes ra) {
        long newId = templateService.saveAsNewVersion(form);
        ra.addFlashAttribute("message", "새 버전(" + newId + ")으로 저장되었습니다.");
        return "redirect:/template";
    }

    /** 삭제 */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deleteTemplate(@RequestParam("templateId") Long id, RedirectAttributes ra) {
        try {
            templateService.deleteTemplate(id);
            ra.addFlashAttribute("message", id + " 번 점검표가 삭제되었습니다.");
        } catch (RuntimeException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:/template";
    }
}
