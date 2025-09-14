package app.snapshot.qure.facilities.dto;

import lombok.Data;

@Data
public class InspectionDto {
    private Long inspectionId;
    private java.sql.Timestamp submittedAt;
    private String result;
    private Long facilityId;
    private Long inspectorId;


}
