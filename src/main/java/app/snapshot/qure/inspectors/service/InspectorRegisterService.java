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
        // 1. 이미 존재하는 작업자인지 확인
        Integer existingId = inspectorsRegisterMapper.findInspectorIdByNameAndPhone(inspectorDto.getName(), inspectorDto.getPhone());

        int inspectorId;
        if (existingId != null) {
            inspectorId = existingId; // 기존 inspector 재사용
        } else {
            inspectorsRegisterMapper.insertInspector(inspectorDto);
            inspectorId = inspectorsRegisterMapper.getLastInsertedId();
        }

        // 2. 설비 등록
        facilityDto.setInspectorId(inspectorId);
        facilitiesRegisterMapper.insertFacility(facilityDto);
    }

}
