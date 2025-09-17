package app.snapshot.qure.mobile.dto;
//작성자 : 최온유

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class CitizenReportDto {
  private Long citizenReportId;   // PK
  private Integer facilityId;     // FK
  private String category;        // 제목
  private String description;     // 상세
  private String email;           // nullable
  private String status;          // '미처리' | '처리중' | '처리완료' 등
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;   // 테이블에 있으면 사용
  private Integer resolvedBy;        // 처리자 ID (manager_id 등)
  private LocalDateTime resolvedAt;  // 처리 시각
}