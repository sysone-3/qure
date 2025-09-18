package app.snapshot.qure.checklist.dto;
// 작성자: 최이서

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistDto {
    private Long itemId;
    private Integer orderNo;
    private String label;
    private String type;
    private String required;
    private String note;
    private String guideline;
    private Long templateId;
}
