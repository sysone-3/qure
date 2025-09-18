package app.snapshot.qure.checklist.repository;
// 작성자: 최이서

import app.snapshot.qure.checklist.model.Checklist;

import java.util.List;

public interface IChecklistRepository {
    List<Checklist> getChecklistById(Long id);
    int insertChecklistItem(Checklist checklist);
}
