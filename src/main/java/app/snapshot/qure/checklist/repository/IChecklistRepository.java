package app.snapshot.qure.checklist.repository;

import app.snapshot.qure.checklist.model.Checklist;

import java.util.List;

public interface IChecklistRepository {
    List<Checklist> getChecklistById(Long id);
    int insertChecklistItem(Checklist checklist);
}
