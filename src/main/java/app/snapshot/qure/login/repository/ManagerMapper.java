package app.snapshot.qure.login.repository;
// 작성자 : 구희원
import app.snapshot.qure.login.dto.ManagerDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper

public interface ManagerMapper {
    ManagerDTO findByEmail(String email);
    void insert (ManagerDTO manager);
    void updateLoginTime(Long managersId);
}
