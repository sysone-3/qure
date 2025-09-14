package app.snapshot.qure.template.service;

import app.snapshot.qure.checklist.dto.CheckItemForm;
import app.snapshot.qure.checklist.dto.TemplateCreateForm;
import app.snapshot.qure.checklist.model.Checklist;
import app.snapshot.qure.checklist.model.ChecklistType;
import app.snapshot.qure.checklist.model.Required;
import app.snapshot.qure.checklist.repository.IChecklistRepository;
import app.snapshot.qure.checklist.service.IChecklistService;
import app.snapshot.qure.template.dto.TemplateUpdateForm;
import app.snapshot.qure.template.model.ActiveStatus;
import app.snapshot.qure.template.model.CycleUnit;
import app.snapshot.qure.template.model.DomainType;
import app.snapshot.qure.template.model.Template;
import app.snapshot.qure.template.repository.ITemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TemplateService implements ITemplateService {

    @Autowired
    @Qualifier("ITemplateRepository")
    ITemplateRepository templateRepository;

    @Autowired
    @Qualifier("IChecklistRepository")
    IChecklistRepository checklistRepository;

    @Autowired
    IChecklistService checklistService;

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
        template.setManagerId(form.getManagerId());

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

    @Transactional
    @Override
    public long saveAsNewVersion(TemplateUpdateForm form) {
        // 1) 이전 템플릿 조회 & 검증
        Template prev = templateRepository.getTemplateById(form.getTemplateId());
        if (prev == null || prev.getIsActive() != ActiveStatus.Y) {
            throw new IllegalStateException("활성 템플릿이 아닙니다.");
        }

        if (form.getVersion() == null) {
            throw new IllegalArgumentException("버전 값이 없습니다.");
        }
        int expected = prev.getVersion();
        int supplied = form.getVersion();

        if (expected != supplied) {
            throw new IllegalStateException("버전 충돌: 페이지를 새로고침 후 다시 시도하세요.");
        }

        // 2) 새 템플릿 생성 (버전 + 1, Y)
        Template next = new Template();
        next.setDomain(DomainType.valueOf(form.getDomain()));
        next.setName(form.getName());
        next.setCycle(form.getCycle());
        next.setCycleUnit(CycleUnit.valueOf(form.getCycleUnit()));
        next.setVersion(supplied + 1);
        next.setIsActive(ActiveStatus.Y);
        next.setFacilityId(prev.getFacilityId()); // 필요시 유지
        templateRepository.insertTemplate(next);  // selectKey로 templateId 채워짐

        // 3) 새 템플릿 아이템 전량 INSERT(배치)
        checklistService.insertItemsBatch(next.getTemplateId(), form.getItems());

        // 4) 이전 템플릿 비활성화 (낙관적)
        int updated = templateRepository.deactivateIfActive(prev.getTemplateId(), prev.getVersion());
        if (updated != 1) {
            throw new IllegalStateException("다른 사용자가 먼저 수정했습니다.");
        }

        return next.getTemplateId();
    }
}
