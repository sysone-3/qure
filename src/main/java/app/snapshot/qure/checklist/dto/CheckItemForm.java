package app.snapshot.qure.checklist.dto;
// 작성자: 최이서

import lombok.Data;

@Data
public class CheckItemForm {
    private String type;   // BOOLEAN | TEXT | NUMBER | PHOTO
    private String label;  // 항목 문구
}