package app.snapshot.qure.template.service;

import app.snapshot.qure.checklist.dto.CheckItemForm;
import app.snapshot.qure.checklist.dto.TemplateCreateForm;
import app.snapshot.qure.checklist.model.Checklist;
import app.snapshot.qure.checklist.model.ChecklistType;
import app.snapshot.qure.checklist.model.Required;
import app.snapshot.qure.checklist.repository.IChecklistRepository;
import app.snapshot.qure.template.model.ActiveStatus;
import app.snapshot.qure.template.model.CycleUnit;
import app.snapshot.qure.template.model.DomainType;
import app.snapshot.qure.template.model.Template;
import app.snapshot.qure.template.repository.ITemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TemplateService implements ITemplateService {

    @Autowired
    @Qualifier("ITemplateRepository")
    ITemplateRepository templateRepository;

    @Autowired
    @Qualifier("IChecklistRepository")
    IChecklistRepository checklistRepository;

    @Override
    public List<Template> getTemplateList() {
        return templateRepository.getTemplateList();
    }

    @Override
    public Template getTemplateById(Long id) {
        return templateRepository.getTemplateById(id);
    }

    @Override
    public long insertTemplate(TemplateCreateForm form) {
        Template template = new Template();
        template.setDomain(DomainType.valueOf(form.getDomain()));
        template.setName(form.getName());
        template.setCycle(form.getCycle());
        template.setCycleUnit(CycleUnit.valueOf(form.getCycleUnit()));
        template.setVersion(1);
        template.setIsActive(ActiveStatus.Y);
        template.setFacilityId(null);

        templateRepository.insertTemplate(template);
        long templateId = template.getTemplateId();

        int order = 1;
        for (CheckItemForm item : form.getItems()) {
            Checklist entity = new Checklist();
            entity.setTemplateId(templateId);
            entity.setOrderNo(order++);
            entity.setLabel(item.getLabel());
            entity.setType(ChecklistType.fromFormType(item.getType()));
            entity.setRequired(Required.Y);

            checklistRepository.insertChecklistItem(entity);
        }

        return templateId;
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
