package app.snapshot.qure.facilities.dto;

import lombok.Data;

@Data
public class ChecklistTemplateDto {

    private Long templateId;
    private String domain;
    private String name;
    private int cycle;
    private int version;
    private String isActive;
    private java.sql.Timestamp createdAt;
    private Long facilityId;


}
