package app.snapshot.qure.template.service;

import app.snapshot.qure.checklist.dto.TemplateCreateForm;
import app.snapshot.qure.template.dto.TemplateUpdateForm;
import app.snapshot.qure.template.model.Template;

import java.util.List;

public interface ITemplateService {
    List<Template> getTemplateList(Long managerId);
    Template getTemplateById(Long id);
    long insertTemplate(TemplateCreateForm template);
    int updateTemplate(Template template);
    int deleteTemplate(Long id);
    long saveAsNewVersion(TemplateUpdateForm form);
}

