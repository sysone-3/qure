package app.snapshot.qure.inspectors.service;

import app.snapshot.qure.inspectors.dto.InspectorDTO;
import app.snapshot.qure.inspectors.repository.InspectorsMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InspectorsService {
    private final InspectorsMapper inspectorsMapper;


    public InspectorsService(InspectorsMapper inspectorMapper) {
        this.inspectorsMapper = inspectorMapper;
    }

    public List<InspectorDTO> getPagedInspectors(int page, int size, long managersId) {
        int startRow = (page - 1) * size + 1;
        int endRow = page * size;

        Map<String, Object> params = new HashMap<>();
        params.put("startRow", startRow);
        params.put("endRow", endRow);
        params.put("managersId", managersId);

        return inspectorsMapper.findPagedByManager(params);
    }

    public int getTotalCount(int managersId) {
        return inspectorsMapper.countByManager(managersId);
    }


}
