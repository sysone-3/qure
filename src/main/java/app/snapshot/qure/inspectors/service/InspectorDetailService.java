package app.snapshot.qure.inspectors.service;

import app.snapshot.qure.inspectors.dto.InspectorDetailDTO;
import app.snapshot.qure.inspectors.repository.InspectorsDetailMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InspectorDetailService {

    private final InspectorsDetailMapper detailMapper;

    public List<InspectorDetailDTO> getInspectorDetail(int inspectorId) {
        return detailMapper.getInspectorDetail(inspectorId);
    }
}
