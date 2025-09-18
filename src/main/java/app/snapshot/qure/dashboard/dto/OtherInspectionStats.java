package app.snapshot.qure.dashboard.dto;
// 작성자: 최이서

import lombok.Data;

@Data
public class OtherInspectionStats {
    private int untilYesterdayTotal;
    private int untilYesterdayCompleted;
    private int upcomingCount;
    private int overdueUninspected;
    private int openComplaints;
}
