package app.snapshot.qure.checklist.service;

import app.snapshot.qure.checklist.dto.CheckItemForm;
import app.snapshot.qure.checklist.model.Checklist;
import app.snapshot.qure.checklist.model.ChecklistType;
import app.snapshot.qure.checklist.model.Required;
import app.snapshot.qure.checklist.repository.IChecklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChecklistService implements IChecklistService {

    @Autowired
    @Qualifier("IChecklistRepository")
    IChecklistRepository checklistRepository; // 일반 조회/단건용

    @Autowired
    @Qualifier("batchChecklistRepository")
    IChecklistRepository batchChecklistRepository; // 배치 전용

    @Transactional
    @Override
    public void insertItemsBatch(long templateId, List<CheckItemForm> items) {
        int order = 1;
        for (CheckItemForm f : items) {
            if (f == null || f.getLabel() == null || f.getLabel().isBlank()) continue;

            Checklist entity = new Checklist();
            entity.setTemplateId(templateId);
            entity.setOrderNo(order++);
            entity.setLabel(f.getLabel());
            entity.setType(ChecklistType.fromFormType(f.getType()));
            entity.setRequired(Required.Y);

            batchChecklistRepository.insertChecklistItem(entity);
        }
    }

    private String mapTypeToDbEnum(String t) {
        return switch (t) {
            case "BOOLEAN" -> "BOOL";
            case "NUMBER"  -> "NUM";
            case "PHOTO"   -> "IMAGE";
            default        -> "TEXT";
        };
    }
}