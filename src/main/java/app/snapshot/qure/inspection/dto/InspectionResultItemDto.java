package app.snapshot.qure.inspection.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InspectionResultItemDto {
    private Long itemId;
    private String label;        // 질문
    private String type;
    private Integer orderNo;
    private String answerText;
    private List<String> imagePaths; // IMAGE인 경우
    private String status;
}
