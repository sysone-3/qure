package app.snapshot.qure.checklist.model;
// 작성자: 최이서

import lombok.Data;

@Data
public class Checklist {
    private Long ItemId;
    private int orderNo;
    private String label;
    private ChecklistType type;
    private Required required;
    private String note;
    private String guideline;
    private Long templateId;
}
