package app.snapshot.qure.inspection.service;

import app.snapshot.qure.inspection.dto.InspectionItemDto;
import app.snapshot.qure.inspection.repository.IInspectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class InspectionService implements IInspectionService {

    @Autowired
    IInspectionRepository inspectionRepository;

    @Override
    public List<InspectionItemDto> getRecentInspections(int limit) {
        return inspectionRepository.selectRecentInspections(limit);
    }

    @Override
    public List<InspectionItemDto> getInspections(String keyword, Date startDate, Date endDate,
                                                  String status, int page, int size) {
        int startRow = (page - 1) * size + 1;
        int endRow = page * size;
        return inspectionRepository.selectInspectionsPage(keyword, startDate, endDate, status, startRow, endRow);
    }

    @Override
    public int countInspections(String keyword, Date startDate, Date endDate, String status) {
        return inspectionRepository.countInspections(keyword, startDate, endDate, status);
    }
}
