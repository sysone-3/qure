package app.snapshot.qure.login.service;

import app.snapshot.qure.login.dto.ManagerDTO;
import app.snapshot.qure.login.repository.ManagerMapper;
import org.springframework.transaction.annotation.Transactional;

public class ManagerService {
    private final ManagerMapper managerMapper;

    public ManagerService(ManagerMapper managerMapper) {
        this.managerMapper = managerMapper;
    }

    @Transactional
    public ManagerDTO processLogin(String email, String name) {
        ManagerDTO manager = managerMapper.findByEmail(email);
        if (manager == null) {
            manager = ManagerDTO.builder()
                    .name(name)
                    .email(email)
                    .build();
            managerMapper.insert(manager);
        } else {
            managerMapper.updateLoginTime(manager.getManagersId());
        }
        return manager;
    }
}
