package app.snapshot.qure.login.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class LoginController {

    @GetMapping("/")
    public String root() {
        return "login/login"; // /WEB-INF/views/login/login.jsp 로 이동
    }

    @GetMapping("/manager/home")
    public String showManagerHome(Authentication authentication, Model model) {
        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User principal = (OAuth2User) authentication.getPrincipal();

            Map<String, Object> kakaoAccount = (Map<String, Object>) principal.getAttributes().get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

            String email = (String) kakaoAccount.get("email");
            String nickname = (String) profile.get("nickname");

            model.addAttribute("name", nickname);
            model.addAttribute("email", email);
            model.addAttribute("attributes", principal.getAttributes());
        }
        return "manager/home";
    }

}
