package app.snapshot.qure.facilities.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FacilitySummaryDto {
    private Integer facilityId;
    private String name;
    private String address;
    private String domain;
    private String zone;
    private LocalDate updateAt;
    private String status; // 합격/미흡/보류 등
    // 점검자
    private String inspectorName;
    private String inspectorPhone;
    private Integer inspectorId; // 점검일
}
