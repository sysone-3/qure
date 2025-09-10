package app.snapshot.qure.template.service;

import app.snapshot.qure.template.model.Template;
import app.snapshot.qure.template.repository.ITemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TemplateService implements ITemplateService {

    @Autowired
    ITemplateRepository templateRepository;

    @Override
    public List<Template> getTemplateList() {
        return templateRepository.getTemplateList();
    }

    @Override
    public Template getTemplateById(Long id) {
        return templateRepository.getTemplateById(id);
    }

    @Override
    public int insertTemplate(Template template) {
        return templateRepository.insertTemplate(template);
    }

    @Override
    public int updateTemplate(Template template) {
        return templateRepository.updateTemplate(template);
    }

    @Override
    public int deleteTemplate(Long id) {
        return templateRepository.deleteTemplate(id);
    }
}
