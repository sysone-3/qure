package app.snapshot.qure.template.repository;

import app.snapshot.qure.template.model.Template;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface ITemplateRepository {
    List<Template> getTemplateList();
    Template getTemplateById(Long id);
    int insertTemplate(Template template);
    int updateTemplate(Template template);
    int deleteTemplate(Long id);
}
