package app.snapshot.qure.inspection.dto;
// 작성자: 최이서

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InspectionDetailDto {
    private Long inspectionId;
    private LocalDateTime submittedAt;

    private FacilityBrief facility;
    private InspectorBrief inspector;

    private List<InspectionResultItemDto> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FacilityBrief {
        private String name;
        private String floor;
        private String zone;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InspectorBrief {
        private String name;
        private String phone;
    }
}
