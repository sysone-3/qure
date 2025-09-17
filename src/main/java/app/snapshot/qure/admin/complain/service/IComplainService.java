package app.snapshot.qure.admin.complain.service;
//작성자 : 최온유

import java.time.LocalDateTime;
import java.util.List;

import app.snapshot.qure.admin.complain.dto.ComplainDto;

public interface IComplainService {

    List<ComplainDto> getAllComplainList(Long managerId);

    List<ComplainDto> getAllComplainList(
    		Long managerId, String q, String status,
            LocalDateTime fromDt, LocalDateTime toExclusive);

    List<ComplainDto> getAllComplainList(
    		Long managerId, String q, String status,
            LocalDateTime fromDt, LocalDateTime toEx,
            int offset, int size);

    int countComplainList(
    		Long managerId, String q, String status,
            LocalDateTime fromDt, LocalDateTime toEx);

    ComplainDto getByIdForManager(int reportId, Long managerId);

    int deleteByIdForManager(int reportId, Long managerId);

    int updateStatusForManager(int reportId, Long managerId, String status);
}
