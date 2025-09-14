package app.snapshot.qure.dashboard.dto;

import lombok.Data;
import java.sql.Date;

@Data
public class DailyCompletionPoint {
    private Date dayValue;
    private int percent;
}
