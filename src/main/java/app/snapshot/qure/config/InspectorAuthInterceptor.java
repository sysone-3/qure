package app.snapshot.qure.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * /mobile/{tagId}/checklist 엔드포인트 전용 접근 제어.
 * - 세션 플래그 INSPECT_OK:{tagId} 가 true일 때만 통과.
 * - 다른 경로는 전혀 건드리지 않음.
 */
@Component
public class InspectorAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        final String ctx = req.getContextPath();              // 예: /qure
        final String path = req.getRequestURI().substring(ctx.length()); // 예: /mobile/21/checklist

        // 대상 경로가 아니면 즉시 통과
        // 기대 패턴: /mobile/{tagId}/checklist 또는 /mobile/{tagId}/checklist/...
        String[] seg = path.split("/");
        if (!(seg.length >= 4 && "mobile".equals(seg[1]) && "checklist".equals(seg[3]))) {
            return true;
        }

        // tagId 파싱 실패 시에도 안전하게 리다이렉트
        int tagId;
        try {
            tagId = Integer.parseInt(seg[2]);
        } catch (NumberFormatException e) {
            res.sendRedirect(ctx + "/mobile/main/0");
            return false;
        }

        HttpSession session = req.getSession(false);
        boolean ok = session != null && Boolean.TRUE.equals(session.getAttribute("INSPECT_OK:" + tagId));
        if (!ok) {
            res.sendRedirect(ctx + "/mobile/" + tagId + "/inspect");
            return false;
        }

        // 캐시 금지
        res.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        res.setHeader("Pragma", "no-cache");
        return true;
    }
}
