package app.snapshot.qure.inspectors.dto;

import lombok.Data;

@Data
public class InspectorDetailDTO {
    private int inspectorId;
    private String name;
    private String phone;
    private String domain;
    private String facility;
}
