package app.snapshot.qure.inspectors.controller;
// 작성자 : 구희원
import app.snapshot.qure.inspectors.dto.InspectorRegisterDTO;
import app.snapshot.qure.inspectors.service.InspectorRegisterService;
import app.snapshot.qure.login.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class InspectorRegisterController {

    private final InspectorRegisterService inspectorRegisterService;

    @PostMapping("/inspectors/register")
    public String registerInspector(
            @ModelAttribute InspectorRegisterDTO inspectorDto,
            HttpSession session) {

        Long managerId = SessionUtil.getManagerId(session);
        if (managerId == null) {
            return "redirect:/dashboard"; // 로그인 안된 경우
        }

        inspectorDto.setManagerId(managerId);

        inspectorRegisterService.registerInspector(inspectorDto);
        return "redirect:/inspectors/inspectorsList";
    }
}
