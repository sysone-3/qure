package app.snapshot.qure.facilities.dto;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class FacilityTagDto {
    private Integer tagId;
    private String  code;        // QR에 넣을 최종 URL
    private String  active;      // 'Y' or 'N'
    private Timestamp issuedAt;
    private Timestamp revokedAt;
    private Integer facilityId;
}
