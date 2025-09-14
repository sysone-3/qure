package app.snapshot.qure.facilities.dto;

import lombok.Data;

@Data
public class FacilityRegisterDTO {
    private String domain;   // 소속
    private String equipment;     // 설비명
    private int inspectorId; // FK
}
