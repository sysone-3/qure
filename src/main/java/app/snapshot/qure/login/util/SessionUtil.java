package app.snapshot.qure.login.util;

import jakarta.servlet.http.HttpSession;

public class SessionUtil {

    public static final String LOGIN_MANAGER_ID = "LOGIN_MANAGER_ID";
    public static final String KAKAO_EMAIL = "KAKAO_EMAIL";
    public static final String KAKAO_NAME = "KAKAO_NAME";

    // 세션에 매니저 정보 저장
    public static void setManagerSession(HttpSession session, Long managerId, String email, String name) {
        session.setAttribute(LOGIN_MANAGER_ID, managerId);
        session.setAttribute(KAKAO_EMAIL, email);
        session.setAttribute(KAKAO_NAME, name);
    }

    // 세션에서 매니저 ID 가져오기 (로그인 필수)
    public static Long mustManagerId(HttpSession session) {
        Long managerId = (Long) session.getAttribute(LOGIN_MANAGER_ID);
        if (managerId == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        return managerId;
    }

    // 세션에서 매니저 ID 가져오기 (nullable)
    public static Long getManagerId(HttpSession session) {
        return (Long) session.getAttribute(LOGIN_MANAGER_ID);
    }

    // 세션에서 이메일 가져오기
    public static String getKakaoEmail(HttpSession session) {
        return (String) session.getAttribute(KAKAO_EMAIL);
    }

    // 세션에서 이름 가져오기
    public static String getKakaoName(HttpSession session) {
        return (String) session.getAttribute(KAKAO_NAME);
    }

    // 로그인 상태 확인
    public static boolean isLoggedIn(HttpSession session) {
        return session.getAttribute(LOGIN_MANAGER_ID) != null;
    }

    // 세션 정리
    public static void clearSession(HttpSession session) {
        session.removeAttribute(LOGIN_MANAGER_ID);
        session.removeAttribute(KAKAO_EMAIL);
        session.removeAttribute(KAKAO_NAME);
    }
}