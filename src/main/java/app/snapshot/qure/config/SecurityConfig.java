package app.snapshot.qure.config;

import app.snapshot.qure.login.dto.ManagerDTO;
import app.snapshot.qure.login.service.KakaoOAuth2UserService;
import app.snapshot.qure.login.service.ManagerService;
import app.snapshot.qure.login.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Map;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:application-login.properties")
public class SecurityConfig {

    private final KakaoOAuth2UserService kakaoOAuth2UserService;
    private final ManagerService managerService;   // ✅ ManagerService 주입

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.client-secret}")
    private String kakaoClientSecret;

    // ✅ 생성자에서 두 서비스 모두 주입
    public SecurityConfig(KakaoOAuth2UserService kakaoOAuth2UserService,
                          ManagerService managerService) {
        this.kakaoOAuth2UserService = kakaoOAuth2UserService;
        this.managerService = managerService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .csrf(csrf -> csrf.disable())

                // 세션 관리 설정
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )

                .oauth2Login(oauth -> oauth
                        .loginPage("/")
                        .successHandler((request, response, authentication) -> {
                            OAuth2User principal = (OAuth2User) authentication.getPrincipal();
                            Map<String, Object> kakaoAccount =
                                    (Map<String, Object>) principal.getAttributes().get("kakao_account");
                            Map<String, Object> profile =
                                    (Map<String, Object>) kakaoAccount.get("profile");

                            String email = (String) kakaoAccount.get("email");
                            String nickname = (String) profile.get("nickname");

                            ManagerDTO manager = managerService.processLogin(email, nickname);

                            HttpSession session = request.getSession();
                            SessionUtil.setManagerSession(session, manager.getManagersId(), email, nickname);

                            System.out.println("로그인 성공: " + nickname + " (" + email + ")");

                            response.sendRedirect(request.getContextPath() + "/dashboard");
                        })
                        .userInfoEndpoint(userInfo -> userInfo.userService(kakaoOAuth2UserService))
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.kakaoClientRegistration());
    }

    private ClientRegistration kakaoClientRegistration() {
        return ClientRegistration.withRegistrationId("kakao")
                .clientId(kakaoClientId)
                .clientSecret(kakaoClientSecret)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/login/oauth2/code/kakao") // 실제로는 /qure/login/oauth2/code/kakao
                .scope("profile_nickname", "account_email")
                .authorizationUri("https://kauth.kakao.com/oauth/authorize")
                .tokenUri("https://kauth.kakao.com/oauth/token")
                .userInfoUri("https://kapi.kakao.com/v2/user/me")
                .userNameAttributeName("id")
                .clientName("Kakao")
                .build();
    }
}
