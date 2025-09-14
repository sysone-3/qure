package app.snapshot.qure.inspectors.service;

import app.snapshot.qure.inspectors.dto.InspectorRegisterDTO;
import app.snapshot.qure.facilities.dto.FacilityRegisterDTO;
import app.snapshot.qure.inspectors.repository.InspectorsRegisterMapper;
import app.snapshot.qure.facilities.repository.FacilitiesRegisterMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InspectorRegisterService {

    private final InspectorsRegisterMapper inspectorsRegisterMapper;
    private final FacilitiesRegisterMapper facilitiesRegisterMapper;

    @Transactional
    public void registerInspectorWithFacility(InspectorRegisterDTO inspectorDto, FacilityRegisterDTO facilityDto) {
        // 1. 점검자 등록
        inspectorsRegisterMapper.insertInspector(inspectorDto);

        // 2. 방금 생성된 inspector_id 가져오기
        int inspectorId = inspectorsRegisterMapper.getLastInsertedId();

        // 3. 설비 DTO에 inspectorId 주입
        facilityDto.setInspectorId(inspectorId);

        // 4. 설비 등록
        facilitiesRegisterMapper.insertFacility(facilityDto);
    }
}
