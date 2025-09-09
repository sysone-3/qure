package app.snapshot.qure.facilities.dto;

import lombok.Data;

@Data
public class InspectionDto {
    private int inspectionId;
    private java.sql.Timestamp submittedAt;
    private String result;
    private int facilityId;
    private int inspectorId;


}
