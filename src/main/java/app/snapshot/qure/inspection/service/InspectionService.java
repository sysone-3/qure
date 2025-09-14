package app.snapshot.qure.inspection.service;

import app.snapshot.qure.inspection.dto.InspectionDetailDto;
import app.snapshot.qure.inspection.dto.InspectionHeaderDto;
import app.snapshot.qure.inspection.dto.InspectionItemDto;
import app.snapshot.qure.inspection.dto.InspectionResultItemDto;
import app.snapshot.qure.inspection.repository.IInspectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional(readOnly = true)
    public InspectionDetailDto getInspectionDetail(Long inspectionId) {
        InspectionHeaderDto h = inspectionRepository.selectInspectionHeader(inspectionId);
        if (h == null) return null;

        List<InspectionResultItemDto> items =
                inspectionRepository.selectInspectionItems(inspectionId);

        return InspectionDetailDto.builder()
                .inspectionId(inspectionId)
                .submittedAt(h.getSubmittedAt())
                .facility(InspectionDetailDto.FacilityBrief.builder()
                        .name(h.getFacilityName())
                        .floor(h.getFacilityFloor())
                        .zone(h.getFacilityZone())
                        .build())
                .inspector(InspectionDetailDto.InspectorBrief.builder()
                        .name(h.getInspectorName())
                        .phone(h.getInspectorPhone())
                        .build())
                .items(items)
                .build();
    }
}
