package app.snapshot.qure.inspection.service;

import app.snapshot.qure.inspection.dto.InspectionItemDto;
import app.snapshot.qure.inspection.repository.IInspectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InspectionService implements IInspectionService {

    @Autowired
    IInspectionRepository inspectionRepository;

    @Override
    public List<InspectionItemDto> getRecentInspections(int limit) {
        return inspectionRepository.selectRecentInspections(limit);
    }
}
