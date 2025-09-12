package app.snapshot.qure.facilities.dto;

import lombok.Data;

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

    private String templateIds; // "12,15,19" 같은 CSV
    public String getTemplateIds() { return templateIds; }
    public void setTemplateIds(String templateIds) { this.templateIds = templateIds; }
}
