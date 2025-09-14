package app.snapshot.qure.checklist.dto;

import lombok.Data;

import java.util.List;

@Data
public class TemplateCreateForm {
    private String domain;      // CLEANING | FIRE | PATROL
    private String name;        // templateName
    private Integer cycle;      // cycleNumber
    private String cycleUnit;   // DAY | WEEK | MONTH | YEAR
    private List<CheckItemForm> items; // items[i].type / items[i].label
    private Long managerId;
}
