package app.snapshot.qure.checklist.repository;

import app.snapshot.qure.template.model.Template;

import java.util.List;

public interface IChecklistRepository {
    List<Template> getTemplateList();
    Template getTemplateById(Long id);
    int insertTemplate(Template template);
    int updateTemplate(Template template);
    int deleteTemplate(Long id);
}
