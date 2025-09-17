package app.snapshot.qure.admin.complain.repository;
//작성자 : 최온유

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import app.snapshot.qure.admin.complain.dto.ComplainDto;

public interface ComplainMapper {

    // 필터(무페이징)
    List<ComplainDto> selectAllComplainList(
            @Param("managerId") Long managerId,
            @Param("q") String q,
            @Param("status") String status,
            @Param("fromDt") LocalDateTime fromDt,
            @Param("toEx") LocalDateTime toEx);

    // 필터 + 페이징
    List<ComplainDto> selectAllComplainList(
            @Param("managerId") Long managerId,
            @Param("q") String q,
            @Param("status") String status,
            @Param("fromDt") LocalDateTime fromDt,
            @Param("toEx") LocalDateTime toEx,
            @Param("offset") int offset,
            @Param("size") int size);

    int countComplainList(
            @Param("managerId") Long managerId,
            @Param("q") String q,
            @Param("status") String status,
            @Param("fromDt") LocalDateTime fromDt,
            @Param("toEx") LocalDateTime toEx);

    ComplainDto selectOneForManager(@Param("reportId") int reportId,
                                    @Param("managerId") Long managerId);

    int deleteByIdForManager(@Param("id") int id,
                             @Param("managerId") Long managerId);

    int updateStatusForManager(@Param("id") int id,
                               @Param("managerId") Long managerId,
                               @Param("status") String status);
}
