package app.snapshot.qure.checklist.dto;

import lombok.Data;

@Data
public class CheckItemForm {
    private String type;   // BOOLEAN | TEXT | NUMBER | PHOTO
    private String label;  // 항목 문구
}