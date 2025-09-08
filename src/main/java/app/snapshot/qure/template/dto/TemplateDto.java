package app.snapshot.qure.template.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TemplateDto {
    private Long templateId;
    private DomainType domain;
    private String name;
    private int cycle;
    private int version;
    private ActiveStatus isActive;
    private LocalDateTime createdAt;
    private int facilityId;
}
