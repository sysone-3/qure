package app.snapshot.qure.inspection.dto;
// 작성자: 최이서

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InspectionHeaderDto {
    private Long inspectionId;
    private LocalDateTime submittedAt;

    private String facilityName;
    private String facilityFloor;
    private String facilityZone;

    private String inspectorName;
    private String inspectorPhone;
}
