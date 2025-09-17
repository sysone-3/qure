package app.snapshot.qure.inspectors.dto;
//작성자 : 구희원
import lombok.Data;

@Data
public class InspectorRegisterDTO {
    private Integer inspectorId;
    private String name;
    private String phone;
    private Long managerId;
}
