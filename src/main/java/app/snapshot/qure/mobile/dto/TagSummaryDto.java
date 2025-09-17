package app.snapshot.qure.mobile.dto;
//작성자 : 최온유

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TagSummaryDto {
	private int tagId;
    private String name;
    private String address;
    private String floor;
    private String zone;
    private int facilityId;
    
}