package app.snapshot.qure.inspection.service;

import app.snapshot.qure.inspection.dto.InspectionDetailDto;
import app.snapshot.qure.inspection.dto.InspectionItemDto;

import java.util.Date;
import java.util.List;

public interface IInspectionService {
    List<InspectionItemDto> getRecentInspections(int limit);
    List<InspectionItemDto> getInspections(String keyword, Date startDate, Date endDate,
                                           String status, int page, int size);

    int countInspections(String keyword, Date startDate, Date endDate, String status);
    InspectionDetailDto getInspectionDetail(Long inspectionId);
}
