package app.snapshot.qure.dashboard.dto;
// 작성자: 최이서

import lombok.Data;

@Data
public class DashboardSessionView {
    private String name;
    private String email;
    private int todayTotal;
    private int todayCompleted;
}
