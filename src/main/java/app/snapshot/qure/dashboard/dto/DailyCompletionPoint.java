package app.snapshot.qure.dashboard.dto;
// 작성자: 최이서

import lombok.Data;
import java.sql.Date;

@Data
public class DailyCompletionPoint {
    private Date dayValue;
    private int percent;
}
