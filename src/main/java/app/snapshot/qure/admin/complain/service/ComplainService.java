// src/main/java/app/snapshot/qure/admin/complain/service/ComplainService.java
package app.snapshot.qure.admin.complain.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import app.snapshot.qure.admin.complain.dto.ComplainDto;
import app.snapshot.qure.admin.complain.repository.ComplainMapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ComplainService implements IComplainService {

    private final ComplainMapper complainMapper;

    @Override
    public List<ComplainDto> getAllComplainList(int managerId) {
        return complainMapper.selectAllComplainList(managerId, null, null, null, null);
    }

    @Override
    public List<ComplainDto> getAllComplainList(
            int managerId, String q, String status,
            LocalDateTime fromDt, LocalDateTime toExclusive) {
        return complainMapper.selectAllComplainList(managerId, q, status, fromDt, toExclusive);
    }

    @Override
    public List<ComplainDto> getAllComplainList(
            int managerId, String q, String status,
            LocalDateTime fromDt, LocalDateTime toEx,
            int offset, int size) {
        return complainMapper.selectAllComplainList(
                managerId, q, status, fromDt, toEx, offset, size);
    }

    @Override
    public int countComplainList(
            int managerId, String q, String status,
            LocalDateTime fromDt, LocalDateTime toEx) {
        return complainMapper.countComplainList(managerId, q, status, fromDt, toEx);
    }

    @Override
    public ComplainDto getByIdForManager(int reportId, int managerId) {
        return complainMapper.selectOneForManager(reportId, managerId);
    }

    @Override
    public int deleteByIdForManager(int reportId, int managerId) {
        return complainMapper.deleteByIdForManager(reportId, managerId);
    }

    @Override
    public int updateStatusForManager(int reportId, int managerId, String status) {
        return complainMapper.updateStatusForManager(reportId, managerId, status);
    }
}
