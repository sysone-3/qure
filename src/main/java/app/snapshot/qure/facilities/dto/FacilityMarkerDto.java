package app.snapshot.qure.facilities.dto;

import lombok.Data;

@Data
public class FacilityMarkerDto {
    private Long facilityId;
    private String name;
    private String address;
    private Double gpsLat;
    private Double gpsLng;
}
