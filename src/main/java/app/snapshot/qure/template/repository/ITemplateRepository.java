package app.snapshot.qure.template.repository;

import app.snapshot.qure.template.dto.DomainType;
import app.snapshot.qure.template.dto.TemplateDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ITemplateRepository {
    List<TemplateDto> getTemplateList();
    List<TemplateDto> searchTemplates(@Param("domain") DomainType domain, @Param("keyword") String keyword);
    int insertTemplate(TemplateDto templateDto);
    int updateTemplate(TemplateDto templateDto);
    int deleteTemplate(Long id);
}
