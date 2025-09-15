package app.snapshot.qure.inspectors.service;

import app.snapshot.qure.inspectors.dto.InspectorRegisterDTO;
import app.snapshot.qure.inspectors.repository.InspectorsRegisterMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InspectorRegisterService {

    private final InspectorsRegisterMapper inspectorsRegisterMapper;

    /**
     * 점검자 등록
     * - 이름, 전화번호로 중복 체크
     * - 없으면 새로 등록, 있으면 무시
     */
    @Transactional
    public void registerInspector(InspectorRegisterDTO inspectorDto) {
        // 이미 존재하는 작업자인지 확인
        Integer existingId = inspectorsRegisterMapper
                .findInspectorIdByNameAndPhone(inspectorDto.getName(), inspectorDto.getPhone());

        if (existingId != null) {
            System.out.println("이미 존재하는 점검자 → id=" + existingId);
            return;
        }

        inspectorsRegisterMapper.insertInspector(inspectorDto);
        int newId = inspectorsRegisterMapper.getLastInsertedId();
        System.out.println("새 점검자 등록 완료 → id=" + newId);
    }
}
