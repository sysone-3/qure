package app.snapshot.qure.facilities.dto;

import lombok.Data;

@Data
public class FacilitiesDto {
	private int facilityId;
    private String domain;
    private String name;
    private String floor;
    private String zone;
    private String address;
    private double gpsLat;
    private double gpsLng;
    private String status;
    private String memo;
    private java.sql.Timestamp createdAt;
    private java.sql.Timestamp updatedAt;
    private int managersId;
    private int inspectorId;


}
