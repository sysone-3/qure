package app.snapshot.qure.template.repository;

import app.snapshot.qure.template.model.Template;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ITemplateRepository {
    List<Template> getTemplateList(@Param("managerId") Long managerId);
    List<Template> getTemplateListFiltered(@Param("managerId") Long managerId,
                                           @Param("domain") String domain,
                                           @Param("q") String q);
    Template getTemplateById(Long id);
    int deactivateIfActive(@Param("templateId") Long templateId,
                           @Param("version") Integer version);
    int insertTemplate(Template template);
    int updateTemplate(Template template);
    int deleteTemplate(Long id);
}
