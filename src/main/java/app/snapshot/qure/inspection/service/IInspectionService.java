package app.snapshot.qure.inspection.service;

import app.snapshot.qure.inspection.dto.InspectionItemDto;

import java.util.List;

public interface IInspectionService {
    List<InspectionItemDto> getRecentInspections(int limit);
}
