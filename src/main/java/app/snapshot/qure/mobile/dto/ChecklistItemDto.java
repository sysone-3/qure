package app.snapshot.qure.mobile.dto;
//작성자 : 최온유

import app.snapshot.qure.checklist.model.ChecklistType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChecklistItemDto {
    private Long itemId;
    private int orderNo;
    private String label;
    private ChecklistType type;
    private boolean required;
    private String note;
    private String guide;
    private int templateId;
}