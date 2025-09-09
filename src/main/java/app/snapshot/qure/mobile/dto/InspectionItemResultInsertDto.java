package app.snapshot.qure.mobile.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class InspectionItemResultInsertDto {
    private Long resultId;          // 시퀀스로 채움(selectKey)
    private Long inspectionId;      // 현재 점검 PK
    private Long itemId;            // 폼에서 온 항목 ID
    private String valueBool;       // 'Y' or 'N' 또는 null
    private Double valueNum;        // 숫자 또는 null
    private String valueText;       // 텍스트 또는 null
    private LocalDateTime createdAt;
    private Long imageId;           // 지금은 항상 null
}
