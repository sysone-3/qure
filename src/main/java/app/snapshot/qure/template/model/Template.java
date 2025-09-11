package app.snapshot.qure.template.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Template {
    private Long templateId;
    private DomainType domain;
    private String name;
    private int cycle;
    private CycleUnit cycleUnit;
    private int version;
    private ActiveStatus isActive;
    private LocalDateTime createdAt;
    private Integer facilityId;
}
