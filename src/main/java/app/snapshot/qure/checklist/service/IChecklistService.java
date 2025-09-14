package app.snapshot.qure.checklist.service;

import app.snapshot.qure.checklist.dto.CheckItemForm;
import app.snapshot.qure.checklist.model.Checklist;

import java.util.List;

public interface IChecklistService {
    List<Checklist> getByTemplateId(long templateId);
    void insertItemsBatch(long templateId, List<CheckItemForm> items);
}
