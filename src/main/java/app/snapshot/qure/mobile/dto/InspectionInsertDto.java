package app.snapshot.qure.mobile.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class InspectionInsertDto {
    private Long inspectionId;      // PK (sequence로 생성)
    private LocalDateTime submittedAt;
    private String result;
    private Integer facilityId;
    private Integer inspectorId;
}