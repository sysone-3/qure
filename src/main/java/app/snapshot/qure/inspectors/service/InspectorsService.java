package app.snapshot.qure.inspectors.service;

import app.snapshot.qure.inspectors.dto.InspectorDTO;
import app.snapshot.qure.inspectors.repository.InspectorsMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InspectorsService {
    private final InspectorsMapper inspectorsMapper;


    public InspectorsService(InspectorsMapper inspectorMapper) {
        this.inspectorsMapper = inspectorMapper;
    }

    public List<InspectorDTO> getPagedInspectors(int page, int size) {
        int startRow = (page-1) * size+1;
        int endRow = page * size;
        return inspectorsMapper.findPaged(startRow, endRow);
    }

    public int getTotalCount() {
        return inspectorsMapper.countAll();
    };
}
