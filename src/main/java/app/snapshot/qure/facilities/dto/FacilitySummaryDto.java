package app.snapshot.qure.facilities.dto;

import lombok.Data;


@Data
public class FacilitySummaryDto {
    private Long facilityId;
    private String name;
    private String address;
    private String domain;
    private String zone;
    private java.sql.Timestamp updatedAt;
    private String status; // 합격/미흡/보류 등
    // 점검자
    private String inspectorName;
    private String inspectorPhone;
    private Long inspectorId; // 점검일
}
