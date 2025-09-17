package app.snapshot.qure.facilities.dto;

import lombok.Data;

import java.util.Date;

// 작성자: 김민서
@Data
public class FacilitiesDto {
	private Long facilityId;
    private String domain;
    private String name;
    private String floor;
    private String zone;
    private String address;
    private Double gpsLat;
    private Double gpsLng;
    private String status;
    private String memo;
    private java.sql.Timestamp createdAt;
    private java.sql.Timestamp updatedAt;
    private Long managersId;
    private Long inspectorId;
    private Date nextScheduledAt;


    private String templateIds; // "12,15,19" 같은 CSV
    private String scheduleStatus; // OVERDUE | UPCOMING | NONE
    private Integer daysDelta;     // 경과/남은 일수

}
