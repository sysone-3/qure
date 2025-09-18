package app.snapshot.qure.inspection.dto;
// 작성자: 최이서

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InspectionDto {
    private Long inspectionId;
    private LocalDateTime submittedAt;
    private String result;
    private Long facilityId;
    private Long inspectorId;
}
