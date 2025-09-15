package app.snapshot.qure.login.handler;

import app.snapshot.qure.login.dto.ManagerDTO;
import app.snapshot.qure.login.service.ManagerService;
import app.snapshot.qure.login.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final ManagerService managerService;

    public OAuth2AuthenticationSuccessHandler(ManagerService managerService) {
        this.managerService = managerService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String email = (String) kakaoAccount.get("email");
        String name = (String) profile.get("nickname");

        // 매니저 정보 처리 및 ID 가져오기
        ManagerDTO manager = managerService.processLogin(email, name);

        // 세션에 카카오 로그인 정보 저장 (유틸리티 사용)
        HttpSession session = request.getSession(true); // 세션이 없으면 생성
        SessionUtil.setManagerSession(session, manager.getManagersId(), email, name);
        System.out.println("세션 저장 완료: managerId=" + manager.getManagersId() +
                ", sessionId=" + session.getId());

        // 세션 설정 추가
        session.setMaxInactiveInterval(60 * 60 * 24); // 24시간

        // 디버그 로그
        System.out.println("세션 저장 완료: managerId=" + manager.getManagersId() +
                ", sessionId=" + session.getId());

        // 홈 페이지로 리다이렉트
        response.sendRedirect("/dashboard");
    }
}