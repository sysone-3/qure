package app.snapshot.qure.mobile.service;

import java.util.List;

import app.snapshot.qure.mobile.dto.ChecklistItemDto;
import app.snapshot.qure.mobile.dto.ChecklistSubmitForm;
import app.snapshot.qure.mobile.dto.ChecklistTemplateDto;
import app.snapshot.qure.mobile.dto.CitizenReportDto;
import app.snapshot.qure.mobile.dto.ImageMetaInsertDto;
import app.snapshot.qure.mobile.dto.TagSummaryDto;

public interface IMobileService {
	
	// tagId → 설비 요약
    TagSummaryDto getTagInfoByTagId(int tagId);

    // tagId, pin -> 검증 결과
    boolean validateInspectorPin(int tagId, int pin);

    // tagId → 템플릿
    ChecklistTemplateDto getTemplateByTagId(int tagId);

    // templateId → 체크리스트 항목
    List<ChecklistItemDto> getChecklistItemByTemplateId(int templateId);
    
    // 체크리스트 결과 제출
    Long saveChecklistSubmission(int tagId, ChecklistSubmitForm form);
    
 // [TEST] 최근 이미지 조회
    List<ImageMetaInsertDto> listLatestImages(int limit);
    
    long submitComplain(int tagId, String category, String description, String email);


}
