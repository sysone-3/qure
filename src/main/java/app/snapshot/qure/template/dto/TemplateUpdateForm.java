package app.snapshot.qure.template.dto;
// 작성자: 최이서

import app.snapshot.qure.checklist.dto.CheckItemForm;
import lombok.Data;
import java.util.List;

@Data
public class TemplateUpdateForm {
    private Long templateId;   // 이전(활성) 템플릿 ID
    private Integer version;   // 이전 버전(낙관적 락)
    private String domain;     // CLEANING | FIRE | PATROL
    private String name;
    private Integer cycle;
    private String cycleUnit;  // DAY | WEEK | MONTH | YEAR
    private List<CheckItemForm> items;
}