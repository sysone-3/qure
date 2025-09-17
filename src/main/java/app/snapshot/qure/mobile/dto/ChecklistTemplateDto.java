package app.snapshot.qure.mobile.dto;
//작성자 : 최온유

import app.snapshot.qure.template.dto.ActiveStatus;
import app.snapshot.qure.template.dto.CycleUnit;
import app.snapshot.qure.template.dto.DomainType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChecklistTemplateDto {
    private int templateId;
    private DomainType domain;
    private String name;
    private int cycle;
    private CycleUnit cycleUnit;
    private int version;
    private ActiveStatus isActive;
    private java.time.LocalDateTime createdAt;
    private int facilityId;
}
