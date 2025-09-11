// src/main/java/app/snapshot/qure/admin/complain/service/IComplainService.java
package app.snapshot.qure.admin.complain.service;

import java.time.LocalDateTime;
import java.util.List;

import app.snapshot.qure.admin.complain.dto.ComplainDto;

public interface IComplainService {

    List<ComplainDto> getAllComplainList(int managerId);

    List<ComplainDto> getAllComplainList(
            int managerId, String q, String status,
            LocalDateTime fromDt, LocalDateTime toExclusive);

    List<ComplainDto> getAllComplainList(
            int managerId, String q, String status,
            LocalDateTime fromDt, LocalDateTime toEx,
            int offset, int size);

    int countComplainList(
            int managerId, String q, String status,
            LocalDateTime fromDt, LocalDateTime toEx);

    ComplainDto getByIdForManager(int reportId, int managerId);

    int deleteByIdForManager(int reportId, int managerId);

    int updateStatusForManager(int reportId, int managerId, String status);
}
