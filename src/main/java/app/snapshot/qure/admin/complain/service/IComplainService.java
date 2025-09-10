package app.snapshot.qure.admin.complain.service;

import java.time.LocalDateTime;
import java.util.List;

import app.snapshot.qure.admin.complain.dto.ComplainDto;


public interface IComplainService {
	
	List<ComplainDto> getAllComplainList(int managerId);
	
	List<ComplainDto> getAllComplainList(
		    int managerId, String q, String status,
		    LocalDateTime fromDt, LocalDateTime toExclusive); // 필터용
	
	ComplainDto getByIdForManager(int reportId, int managerId);
	
	int deleteByIdForManager(int reportId, int managerId);
	
	int updateStatusForManager(int reportId, int managerId, String status);


	
	

	

}
