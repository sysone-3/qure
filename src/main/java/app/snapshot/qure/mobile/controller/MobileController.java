package app.snapshot.qure.mobile.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import app.snapshot.qure.mobile.dto.ChecklistItemDto;
import app.snapshot.qure.mobile.dto.ChecklistSubmitForm;
import app.snapshot.qure.mobile.dto.ChecklistTemplateDto;
import app.snapshot.qure.mobile.dto.TagSummaryDto;
import app.snapshot.qure.mobile.repository.MobileMapper;
import app.snapshot.qure.mobile.service.IMobileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@Slf4j
@RequestMapping(value="/mobile")
@Controller
public class MobileController {

    @Autowired IMobileService mobileService;
    @Autowired MobileMapper mobileMapper; // 이미지 메타 조회용

    // S3 클라이언트
    @Autowired
    private S3Client s3;
    
    @Value("${s3.bucket}")  private String bucket;

    // 최종 모바일 메인 페이지
    @GetMapping("/main/{tagId}")
    public String mobileMainPage(@PathVariable("tagId") int tagId, Model model) {
        TagSummaryDto tagInfo = mobileService.getTagInfoByTagId(tagId);
        model.addAttribute("tagInfo", tagInfo);
        return "mobile/mobileMain";
    }

    @GetMapping(value="/{tagId}/inspect")
    public String inspectorValidatePage(@PathVariable("tagId") int tagId, Model model) {
        model.addAttribute("tagId", tagId);
        return "mobile/inspectorValidate";
    }

    @PostMapping(value="/{tagId}/inspect")
    public String validateInspector(@PathVariable("tagId") int tagId,
                                    @RequestParam("pin") int pin,
                                    RedirectAttributes ra,
                                    HttpSession session,
                                    Model model) {

        Boolean ok = mobileService.validateInspectorPin(tagId, pin);
        if (ok) {
            // [추가] 인터셉터가 검사할 세션 플래그 설정
            session.setAttribute("INSPECT_OK:"+tagId, true);
            return "redirect:/mobile/" + tagId + "/checklist";
        }
        model.addAttribute("tagId", tagId);
        model.addAttribute("errorMsg", "인증 실패: 휴대폰 번호 뒷자리 4자리를 확인하세요.");
        return "mobile/inspectorValidate";
    }

    @GetMapping(value="/{tagId}/checklist")
    public String checklistView(@PathVariable("tagId") int tagId, Model model, HttpSession session, HttpServletRequest request) {
        // [유지] 2차 방어. 인터셉터가 1차로 걸러도 여기서 한 번 더 확인.
        Boolean ok = (Boolean) session.getAttribute("INSPECT_OK:"+tagId);
        if (ok == null || !ok) return "redirect:/mobile/" + tagId + "/inspect";

        TagSummaryDto tagInfo = mobileService.getTagInfoByTagId(tagId);
        model.addAttribute("tagInfo", tagInfo);

        ChecklistTemplateDto template = mobileService.getTemplateByTagId(tagId);
        model.addAttribute("template", template);

        List<ChecklistItemDto> items = mobileService.getChecklistItemByTemplateId(template.getTemplateId());
        model.addAttribute("items", items);

        model.addAttribute("tagId", tagId);

        // [유지] NONCE는 POST 위조/중복 방지 용도
        String nonce = UUID.randomUUID().toString();
        session.setAttribute("NONCE:"+tagId, nonce);
        request.setAttribute("nonce", nonce);

        return "mobile/checklist";
    }

    @PostMapping(value="/{tagId}/checklist")
    public String checklistView(@PathVariable("tagId") int tagId,
                                ChecklistSubmitForm form,
                                HttpSession session,
                                RedirectAttributes ra) {

        // [유지] 세션 플래그 확인
        Boolean ok = (Boolean) session.getAttribute("INSPECT_OK:" + tagId);
        if (ok == null || !ok) {
            ra.addFlashAttribute("errorMsg", "세션 만료. 다시 인증하세요.");
            return "redirect:/mobile/main/" +tagId;
        }

        // [유지] NONCE 검증 및 제거
        String key = "NONCE:" + tagId;
        String expected = (String) session.getAttribute(key);
        session.removeAttribute(key);
        if (expected == null || !expected.equals(form.getNonce())) {
            ra.addFlashAttribute("errorMsg", "잘못된 요청입니다. 다시 시도하세요.");
            return "redirect:/mobile/main/"+tagId;
        }

        try {
            Long inspectionId = mobileService.saveChecklistSubmission(tagId, form);

            // [추가] 제출 완료 시 접근 플래그 제거 → 뒤로가기 등으로 재진입 차단
            session.removeAttribute("INSPECT_OK:" + tagId);

            ra.addFlashAttribute("successMsg", "제출 완료 (ID: " + inspectionId + ")");
            return "redirect:/mobile/main/" + tagId;
        } catch (Exception e) {
            log.error("checklist submit error", e);
            ra.addFlashAttribute("errorMsg", "제출 실패");
            // [보완] 실패 시에도 NONCE는 이미 제거됨. 재표시 위해 재인증 유도는 하지 않음.
            return "redirect:/mobile/main/" + tagId;
        }
    }

    // ↓ 추가: S3에서 이미지 스트리밍 반환
    @GetMapping("/file/{imageId}")
    public ResponseEntity<InputStreamResource> serve(@PathVariable long imageId) {
        var m = mobileMapper.selectImageMeta(imageId); // filePath=S3 key, mimeType
        if (m == null || m.getFilePath() == null) return ResponseEntity.notFound().build();

        ResponseInputStream<GetObjectResponse> in =
                s3.getObject(b -> b.bucket(bucket).key(m.getFilePath()));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(m.getMimeType()))
                .body(new InputStreamResource(in));
    }
    
    // [TEST] 이미지 테스트 화면 엔드포인트
    @GetMapping("/test/images")
    public String imageTestPage(@RequestParam(name = "limit", defaultValue = "20") int limit,
                                Model model) {
        if (limit < 1) limit = 1;
        if (limit > 200) limit = 200;
        var images = mobileService.listLatestImages(limit); // [TEST] 서비스 호출
        model.addAttribute("images", images);
        model.addAttribute("limit", limit);
        return "mobile/imageTest";
    }
    
    @GetMapping("/{tagId}/complain")
    public String complainPage(@PathVariable("tagId") int tagId, Model model) {
        TagSummaryDto tagInfo = mobileService.getTagInfoByTagId(tagId);
        model.addAttribute("tagInfo",tagInfo);
        return "mobile/complain";
    }
    
    @PostMapping("/{tagId}/complain")
    public String submit(@PathVariable int tagId,
                         @RequestParam String category,
                         @RequestParam String description,
                         @RequestParam(required=false) String email,
                         RedirectAttributes ra) {
      try {
        long id = mobileService.submitComplain(tagId, category, description, email);
        ra.addFlashAttribute("complainStatus", "OK");
        ra.addFlashAttribute("complainMsg", "민원 접수 완료 (ID: " + id + ")");
        ra.addFlashAttribute("reportId", id);
      } catch (Exception e) {
        log.error("complain submit error", e);
        ra.addFlashAttribute("complainStatus", "FAIL");
        ra.addFlashAttribute("complainMsg", "민원 접수 실패");
      }
      return "redirect:/mobile/main/" + tagId;
    }
}
