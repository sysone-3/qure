package app.snapshot.qure.login.service;

import app.snapshot.qure.login.dto.ManagerDTO;
import app.snapshot.qure.login.repository.ManagerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
public class KakaoOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private ManagerMapper managerMapper;

    @Autowired
    public KakaoOAuth2UserService(ManagerMapper managerMapper) {
        this.managerMapper = managerMapper;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String email = (String) kakaoAccount.get("email");
        String name = (String) profile.get("nickname");

        // DB 처리
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

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_MANAGER")),
                attributes,
                "id"
        );
    }
}


