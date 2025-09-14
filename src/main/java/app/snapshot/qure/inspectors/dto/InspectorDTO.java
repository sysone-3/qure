package app.snapshot.qure.inspectors.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InspectorDTO {
    private int inspectorId;
    private String phone;
    private String name;
}
