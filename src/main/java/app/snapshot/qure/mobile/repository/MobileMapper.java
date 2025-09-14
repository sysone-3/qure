package app.snapshot.qure.mobile.repository;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import app.snapshot.qure.checklist.model.ChecklistType;
import app.snapshot.qure.mobile.dto.ChecklistItemDto;
import app.snapshot.qure.mobile.dto.ChecklistTemplateDto;
import app.snapshot.qure.mobile.dto.CitizenReportDto;
import app.snapshot.qure.mobile.dto.ImageMetaInsertDto;
import app.snapshot.qure.mobile.dto.InspectionInsertDto;
import app.snapshot.qure.mobile.dto.InspectionItemResultInsertDto;
import app.snapshot.qure.mobile.dto.SubmitIdDto;
import app.snapshot.qure.mobile.dto.TagSummaryDto;

public interface MobileMapper {

    TagSummaryDto selectTagSummaryById(@Param("tagId") int tagId);

    String selectPin4byTagId(@Param("tagId") int tagId);

    ChecklistTemplateDto selectTemplateByTagId(@Param("tagId") int tagId);

    List<ChecklistItemDto> selectAllChecklistByTemplateId(@Param("templateId") int templateId);

    SubmitIdDto selectSubmitIdByTagId(@Param("tagId") int tagId);

    int insertInspection(InspectionInsertDto dto);

    ChecklistType selectItemTypeById(@Param("itemId") Long itemId);

    int insertInspectionItemResult(InspectionItemResultInsertDto dto);

    int insertImageMeta(ImageMetaInsertDto dto);

    ImageMetaInsertDto selectImageMeta(@Param("imageId") long imageId);

    List<ImageMetaInsertDto> selectLatestImages(@Param("limit") int limit);

    int insertComplain(CitizenReportDto dto);

    Integer selectManagerIdByFacilityId(@Param("facilityId") int facilityId);

    int updateNextScheduledAtByFacility(
        @Param("facilityId") int facilityId,
        @Param("cycle") int cycle,
        @Param("cycleUnit") String cycleUnit
    );
}
