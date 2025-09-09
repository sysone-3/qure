package app.snapshot.qure.mobile.dto;

import app.snapshot.qure.checklist.dto.ChecklistType;
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
    private ChecklistType type;   // ← 여기 Enum으로 교체
    private boolean required;
    private String note;
    private String guide;
    private int templateId;
}