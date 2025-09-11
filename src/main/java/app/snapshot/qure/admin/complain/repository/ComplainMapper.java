// src/main/java/app/snapshot/qure/admin/complain/repository/ComplainMapper.java
package app.snapshot.qure.admin.complain.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import app.snapshot.qure.admin.complain.dto.ComplainDto;

public interface ComplainMapper {

    // 필터(무페이징)
    List<ComplainDto> selectAllComplainList(
            @Param("managerId") int managerId,
            @Param("q") String q,
            @Param("status") String status,
            @Param("fromDt") LocalDateTime fromDt,
            @Param("toEx") LocalDateTime toEx);

    // 필터 + 페이징
    List<ComplainDto> selectAllComplainList(
            @Param("managerId") int managerId,
            @Param("q") String q,
            @Param("status") String status,
            @Param("fromDt") LocalDateTime fromDt,
            @Param("toEx") LocalDateTime toEx,
            @Param("offset") int offset,
            @Param("size") int size);

    int countComplainList(
            @Param("managerId") int managerId,
            @Param("q") String q,
            @Param("status") String status,
            @Param("fromDt") LocalDateTime fromDt,
            @Param("toEx") LocalDateTime toEx);

    ComplainDto selectOneForManager(@Param("reportId") int reportId,
                                    @Param("managerId") int managerId);

    int deleteByIdForManager(@Param("id") int id,
                             @Param("managerId") int managerId);

    int updateStatusForManager(@Param("id") int id,
                               @Param("managerId") int managerId,
                               @Param("status") String status);
}
