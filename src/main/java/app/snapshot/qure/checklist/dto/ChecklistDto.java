package app.snapshot.qure.checklist.dto;

import lombok.Data;

@Data
public class ChecklistDto {
    private Long ItemId;
    private int orderNo;
    private String label;
    private ChecklistType type;
    private Boolean required;
    private String note;
    private String guideline;
    private Long templateId;
}
