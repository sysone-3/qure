package app.snapshot.qure.inspection.service;
// 작성자: 최이서

import app.snapshot.qure.inspection.dto.InspectionDetailDto;
import app.snapshot.qure.inspection.dto.InspectionItemDto;

import java.util.Date;
import java.util.List;

public interface IInspectionService {
    List<InspectionItemDto> getRecentInspections(int limit, Long managerId);
    List<InspectionItemDto> getInspections(String keyword, Date startDate, Date endDate,
                                           String status, int page, int size, Long managerId);

    int countInspections(String keyword, Date startDate, Date endDate, String status, Long managerId);
    InspectionDetailDto getInspectionDetail(Long inspectionId, Long managerId);
}
