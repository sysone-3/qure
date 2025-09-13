package app.snapshot.qure.dashboard.dto;

import lombok.Data;

@Data
public class OtherInspectionStats {
    private int untilYesterdayTotal;
    private int untilYesterdayCompleted;
    private int upcomingCount;
    private int overdueUninspected;
    private int openComplaints;
}
