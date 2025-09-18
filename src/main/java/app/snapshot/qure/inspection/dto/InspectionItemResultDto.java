package app.snapshot.qure.inspection.dto;
// 작성자: 최이서

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InspectionItemResultDto {
    private Long resultId;
    private Boolean valueBool;
    private Float valueNum;
    private String valueText;
    private LocalDateTime createdAt;
    private Long itemId;
    private Long inspectionId;
}
