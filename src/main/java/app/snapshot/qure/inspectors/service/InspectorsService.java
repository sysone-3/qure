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

    public List<InspectorDTO> getAllInspectors() {
        return inspectorsMapper.findAll();
    }
}
