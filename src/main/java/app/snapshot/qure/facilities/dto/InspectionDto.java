package app.snapshot.qure.facilities.dto;

import lombok.Data;

// 작성자: 김민서
@Data
public class InspectionDto {
    private Long inspectionId;
    private java.sql.Timestamp submittedAt;
    private String result;
    private Long facilityId;
    private Long inspectorId;


}
