package app.snapshot.qure.inspection.dto;
// 작성자: 최이서

import lombok.Data;

@Data
public class ImageDto {
    private Long imageId;
    private String filePath;
    private String mimeType;
    private Long resultId;
}
