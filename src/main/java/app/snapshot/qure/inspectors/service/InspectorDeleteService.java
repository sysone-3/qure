package app.snapshot.qure.inspectors.service;
// 작성자 : 구희원
import app.snapshot.qure.inspectors.repository.InspectorsDeleteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InspectorDeleteService {

    private final InspectorsDeleteMapper deleteMapper;

    @Transactional
    public void deleteInspectors(String[] inspectorIds) {
        for (String id : inspectorIds) {
            // 이제는 CASCADE가 다 해결해주므로 inspector_profile만 삭제
            deleteMapper.deleteInspector(id);
        }
    }
}

