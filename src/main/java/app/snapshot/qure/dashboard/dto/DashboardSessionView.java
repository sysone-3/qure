package app.snapshot.qure.dashboard.dto;

import lombok.Data;

@Data
public class DashboardSessionView {
    private String name;
    private String email;
    private int todayTotal;
    private int todayCompleted;
}
