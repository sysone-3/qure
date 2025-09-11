package app.snapshot.qure.checklist.service;

import app.snapshot.qure.checklist.dto.CheckItemForm;

import java.util.List;

public interface IChecklistService {
    void insertItemsBatch(long templateId, List<CheckItemForm> items);
}
