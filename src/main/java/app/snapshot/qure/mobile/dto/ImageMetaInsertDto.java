package app.snapshot.qure.mobile.dto;
//작성자 : 최온유

import lombok.Data;

@Data
public class ImageMetaInsertDto {
    private Long imageId;     // 시퀀스
    private String filePath;  // S3 객체 키(예: inspection/123/45/uuid.jpg)
    private String mimeType;  // image/jpeg 등
    private Long resultId;    // FK: inspection_item_results.result_id
}
