package app.snapshot.qure.inspectors.dto;

import lombok.Data;

@Data
public class InspectorRegisterDTO {
    private Integer inspectorId;
    private String name;
    private String phone;
    private Long managerId;
}
