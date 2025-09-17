package app.snapshot.qure.mobile.dto;
//작성자 : 최온유

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ChecklistSubmitForm {
    private String facilityId;
    private String templateId;
    private String tagId;
    private String nonce;
    private Double submitLat;   // 위도 추가
    private Double submitLng;   // 경도 추가
    private List<ResultRow> results;
    
    @Data
    public static class ResultRow {
        private Long itemId;
        private String value;
        private MultipartFile[] photos;
    }
}
