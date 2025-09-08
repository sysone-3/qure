package app.snapshot.qure.template.repository;

import app.snapshot.qure.template.model.Template;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface ITemplateRepository {
    List<Template> getTemplateList();
//    List<TemplateDto> searchTemplates(@Param("domain") DomainType domain, @Param("keyword") String keyword);
//    int insertTemplate(TemplateDto templateDto);
//    int updateTemplate(TemplateDto templateDto);
//    int deleteTemplate(Long id);
}
