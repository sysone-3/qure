package app.snapshot.qure.inspection.dto;
// 작성자: 최이서

import lombok.Data;
import java.sql.Timestamp;

@Data
public class InspectionItemDto {
    private Long inspectionId;
    private Timestamp submittedAt;
    private String result;

    private Long facilityId;
    private String domain;         // 유형
    private String facilityName;   // 점검 설비 이름
    private String floor;          // 층
    private String zone;           // 구역

    private Long inspectorId;
    private String inspectorName;  // 점검자
}
