package app.snapshot.qure.admin.complain.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ComplainDto {
	private int citizenReportId;
	private String category;
	private String description;
	private String email;
	private String status;
	private	LocalDateTime createdAt;
	private int resolvedBy;
	private LocalDateTime resolvedAt;
	private int facilityId;
	private String name;
	private String floor;
	private String zone;
	private String address;
}
