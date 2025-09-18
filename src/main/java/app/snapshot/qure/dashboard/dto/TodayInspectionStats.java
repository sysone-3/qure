package app.snapshot.qure.dashboard.dto;
// 작성자: 최이서

import lombok.Data;

@Data
public class TodayInspectionStats {
    private int todayTotal;
    private int todayCompleted;
}