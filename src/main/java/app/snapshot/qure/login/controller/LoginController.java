package app.snapshot.qure.login.controller;
// 작성자 : 구희원
// 세션 관련 작성자: 김민서
import app.snapshot.qure.login.dto.ManagerDTO;
import app.snapshot.qure.login.service.ManagerService;
import app.snapshot.qure.login.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller

public class LoginController {

    private final ManagerService managerService;

    public LoginController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @GetMapping("/")
    public String root() {
        return "login/login";
    }

    @GetMapping("/manager/home")
    public String showManagerHome(Authentication authentication, Model model, HttpSession session) {

        // 먼저 세션에서 확인
        if (SessionUtil.isLoggedIn(session)) {
            model.addAttribute("name", SessionUtil.getKakaoName(session));
            model.addAttribute("email", SessionUtil.getKakaoEmail(session));
            model.addAttribute("managerId", SessionUtil.getManagerId(session));
            return "manager/home";
        }

        // Authentication 객체에서 정보 추출하고 세션에 저장
        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User principal = (OAuth2User) authentication.getPrincipal();

            Map<String, Object> kakaoAccount = (Map<String, Object>) principal.getAttributes().get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

            String email = (String) kakaoAccount.get("email");
            String nickname = (String) profile.get("nickname");


            System.out.println("=== 카카오 로그인 성공 ===");
            System.out.println("카카오에서 받은 email = " + email);
            System.out.println("카카오에서 받은 nickname = " + nickname);

            // 매니저 정보 처리
            ManagerDTO manager = managerService.processLogin(email, nickname);

            // 세션에 저장
            SessionUtil.setManagerSession(session, manager.getManagersId(), email, nickname);

            model.addAttribute("name", nickname);
            model.addAttribute("email", email);
            model.addAttribute("managerId", manager.getManagersId());

            System.out.println("세션에 저장 완료: managerId=" + manager.getManagersId());

            return "redirect:/dashboard";
        }

        // 로그인되지 않은 경우
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        SessionUtil.clearSession(session);
        session.invalidate();
        return "redirect:/";
    }
}