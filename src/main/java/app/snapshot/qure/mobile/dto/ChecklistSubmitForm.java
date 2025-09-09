package app.snapshot.qure.mobile.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ChecklistSubmitForm {
    private String facilityId;
    private String templateId;
    private String tagId;
    private String nonce;
    private List<ResultRow> results;
    
    @Data
    public static class ResultRow {
        private Long itemId;
        private String value;
        private List<MultipartFile> photos;
    }
}
