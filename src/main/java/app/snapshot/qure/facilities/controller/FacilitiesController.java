package app.snapshot.qure.facilities.controller;


import app.snapshot.qure.facilities.dto.FacilitiesDto;
import app.snapshot.qure.facilities.dto.FacilityTagDto;
import app.snapshot.qure.facilities.service.GeocodingService;
import app.snapshot.qure.facilities.service.IFacilitiesService;
import app.snapshot.qure.login.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
@Controller
@RequestMapping("/facilities")
public class FacilitiesController {

    @Autowired private IFacilitiesService facilitiesService;
    @Autowired
    private GeocodingService geocodingService;

    public FacilitiesController(IFacilitiesService facilitiesService,
                                GeocodingService geocodingService) {
        this.facilitiesService = facilitiesService;
        this.geocodingService = geocodingService;
    }


    // 목록 + 검색
    @GetMapping
    public String list(@RequestParam(value = "q", required = false) String q,
                       Model model, HttpSession session) {
        Long managerId = SessionUtil.mustManagerId(session);
        List<FacilitiesDto> facilities = (q == null || q.isBlank())
                ? facilitiesService.getFacilitiesListByManager(managerId)
                : facilitiesService.searchFacilitiesByManager(managerId, q.trim());
        model.addAttribute("facilities", facilities);

        return "facilities/facilities";
    }

    // 설비 등록 폼
    @GetMapping("/new")
    public String newForm(Model model,
                          @RequestParam(value = "popup", required = false, defaultValue = "false") boolean popup,
                          @RequestParam(value = "returnUrl", required = false) String returnUrl) {

        // 기본 속성들
        model.addAttribute("popup", popup);
        model.addAttribute("returnUrl", returnUrl != null ? returnUrl : "");

        // 빈 DTO 객체 추가 (JSP에서 사용할 수 있음)
        model.addAttribute("facility", new FacilitiesDto());

        // 도메인 옵션들 추가
        model.addAttribute("domains", List.of("청결", "순찰", "소방"));

        // 디버깅을 위한 로그
        System.out.println("newForm 메서드 호출됨 - popup: " + popup + ", returnUrl: " + returnUrl);

        return "facilities/facilities_add";
    }

    // 주소에서 좌표로 변경
    @PostMapping
    public String create(@ModelAttribute FacilitiesDto dto,
                         RedirectAttributes ra,
                         HttpSession session) {
        Long managerId = SessionUtil.mustManagerId(session);

        // 1) 주소 → 좌표
        GeocodingService.LatLng ll = geocodingService.geocode(dto.getAddress());
        if (ll != null) {
            dto.setGpsLat(round(ll.lat(), 6));
            dto.setGpsLng(round(ll.lng(), 6));
        } else {
            dto.setGpsLat(null);
            dto.setGpsLng(null);
        }

        // 2) 시설 + QR 생성
        Long facilityId = facilitiesService.createFacilityWithQr(dto, managerId);

        // 3) 템플릿 연결 (선택된 경우에만)
        if (dto.getTemplateIds() != null && !dto.getTemplateIds().isBlank()) {
            facilitiesService.attachTemplatesToFacility(
                    facilityId,
                    parseIds(dto.getTemplateIds()),
                    managerId
            );
        }

        ra.addFlashAttribute("msg", "설비가 등록되었습니다.");
        return "redirect:/facilities/" + facilityId + "/qr";
    }

    // 쉼표 구분 문자열 → Long 리스트
    private List<Long> parseIds(String csv) {
        return java.util.Arrays.stream(csv.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Long::valueOf)
                .toList();
    }
    // static 제거
    private Double round(double v, int scale) {
        double p = Math.pow(10, scale);
        return Math.round(v * p) / p;
    }

    // 등록 직후 QR 확인 페이지
    @GetMapping("/{facilityId}/qr")
    public String showQr(@PathVariable Long facilityId, Model model, RedirectAttributes ra, HttpSession session) {
        Long managerId = SessionUtil.mustManagerId(session);

        var facility = facilitiesService.findByfacilityIdAndManager(facilityId, managerId);
        if (facility == null) {
            ra.addFlashAttribute("msg", "설비를 찾을 수 없습니다.");
            return "redirect:/facilities";
        }
        FacilityTagDto tag = facilitiesService.findActiveTagByFacilityId(facilityId);
        model.addAttribute("facility", facility);
        model.addAttribute("tag", tag);
        return "facilities/facility_qr";
    }
    
    // 설비 수정
    // 설비 수정
    @GetMapping("/{facilityId}/edit")
    public String editForm(@PathVariable("facilityId") Long facilityId,
                           Model model, RedirectAttributes ra, HttpSession session) {
        Long managerId = SessionUtil.mustManagerId(session);

        FacilitiesDto facility = facilitiesService.findByfacilityIdAndManager(facilityId, managerId);
        if (facility == null) {
            ra.addFlashAttribute("msg", "해당 설비가 없습니다. (ID: " + facilityId + ")");
            return "redirect:/facilities";
        }

        // ★ 설비에 연결된 템플릿 조회 → 모델에 넣기
        var attached = facilitiesService.findTemplatesByFacilityIdAndManager(facilityId, managerId);
        model.addAttribute("facility", facility);
        model.addAttribute("attachedTemplates", attached); // ← JSP는 이것만 사용

        return "facilities/facilities_edit";
    }


    // 상세 (본인 소유만 접근)
    @GetMapping("/{facilityId}")
    public String detail(@PathVariable Long facilityId,
                         @RequestParam(required = false) String start,
                         @RequestParam(required = false) String end,
                         Model model, RedirectAttributes ra, HttpSession session) {

        Long managerId = SessionUtil.mustManagerId(session);
        FacilitiesDto facility = facilitiesService.findByfacilityIdAndManager(facilityId, managerId);
        if (facility == null) {
            ra.addFlashAttribute("msg", "접근 권한이 없거나 존재하지 않는 설비입니다.");
            return "redirect:/facilities";
        }

        // 날짜 보정
        LocalDate startD = (start != null && !start.isBlank()) ? LocalDate.parse(start) : null;
        LocalDate endD   = (end   != null && !end.isBlank())   ? LocalDate.parse(end)   : null;
        if (startD == null && endD == null) { endD = LocalDate.now(); startD = endD.minusDays(30); }
        else if (startD == null) { startD = endD.minusDays(30); }
        else if (endD == null) { endD = startD.plusDays(30); }
        if (endD.isBefore(startD)) { LocalDate t = startD; startD = endD; endD = t; }

        var inspections = facilitiesService
                .findInspectionByFacilityIdAndPeriodAndManager(facilityId, startD, endD, managerId);
        var templates = facilitiesService.findTemplatesByFacilityIdAndManager(facilityId, managerId);

        var tag = facilitiesService.findActiveTagByFacilityId(facilityId); // 소유 확인은 위에서 끝

        model.addAttribute("facility", facility);
        model.addAttribute("inspections", inspections);
        model.addAttribute("templates", templates);
        model.addAttribute("tag", tag);
        model.addAttribute("start", startD.toString());
        model.addAttribute("end", endD.toString());
        return "facilities/facilities_detail";
    }

    // 수정/삭제도 동일하게 managerId로 가드
    @PostMapping("/{id}")
    public String edit(@PathVariable("id") Long facilityId,
                       @ModelAttribute("facility") FacilitiesDto facility,
                       RedirectAttributes ra, HttpSession session) {
        Long managerId = SessionUtil.mustManagerId(session);

        // 1) 주소 → 좌표 (주소가 변경된 경우)
        if (facility.getAddress() != null && !facility.getAddress().isBlank()) {
            GeocodingService.LatLng ll = geocodingService.geocode(facility.getAddress());
            if (ll != null) {
                facility.setGpsLat(round(ll.lat(), 6));
                facility.setGpsLng(round(ll.lng(), 6));
            } else {
                facility.setGpsLat(null);
                facility.setGpsLng(null);
            }
        }

        facility.setFacilityId(facilityId);
        int rows = facilitiesService.updateFacilitiesByManager(facility, managerId);

        // 2) 점검표 연결 업데이트 (설비 수정이 성공한 경우에만)
        if (rows > 0 && facility.getTemplateIds() != null && !facility.getTemplateIds().isBlank()) {
            facilitiesService.attachTemplatesToFacility(
                    facilityId,
                    parseIds(facility.getTemplateIds()),
                    managerId
            );
        }

        ra.addFlashAttribute("msg", rows > 0 ? "설비가 수정되었습니다." : "수정 권한이 없거나 실패했습니다.");
        return "redirect:/facilities/" + facilityId;
    }

    @PostMapping("/{facilityId}/delete")
    public String delete(@PathVariable Long facilityId, RedirectAttributes ra, HttpSession session) {
        Long managerId = SessionUtil.mustManagerId(session);
        int cnt = facilitiesService.deleteFacilitiesByManager(facilityId, managerId);
        ra.addFlashAttribute("msg", cnt > 0 ? "삭제되었습니다." : "삭제 권한이 없거나 데이터가 없습니다.");
        return "redirect:/facilities";
    }
}