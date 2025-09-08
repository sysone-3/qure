package app.snapshot.qure.inspection.dto;

import lombok.Data;

@Data
public class ImageDto {
    private Long imageId;
    private String filePath;
    private String mimeType;
    private Long resultId;
}
