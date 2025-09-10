package app.snapshot.qure.admin.complain.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import app.snapshot.qure.admin.complain.dto.ComplainDto;

public interface ComplainMapper {
	
	List<ComplainDto> selectAllComplainList(int managerId);
	
	List<ComplainDto> selectAllComplainList(
		    @Param("managerId") int managerId,
		    @Param("q") String q,
		    @Param("status") String status,
		    @Param("fromDt") LocalDateTime fromDt,
		    @Param("toExclusive") LocalDateTime toExclusive);
	
	ComplainDto selectOneForManager(@Param("reportId") int reportId, @Param("managerId") int managerId);
	
	int deleteByIdForManager(@Param("id") int id, @Param("managerId") int managerId);
	
	int updateStatusForManager(@Param("id") int id,
            @Param("managerId") int managerId,
            @Param("status") String status);



}
