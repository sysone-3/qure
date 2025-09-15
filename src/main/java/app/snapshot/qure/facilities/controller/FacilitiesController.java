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
import java.util.ArrayList;
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
                         @RequestParam(name = "addressForGeocode", required = false) String addressForGeocode,
                         RedirectAttributes ra,
                         HttpSession session) {
        Long managerId = SessionUtil.mustManagerId(session);

        // 1) 지오코딩용 "도로명+번지" 핵심만 추출 (hidden이 비어와도 dto.address만으로 안전하게 동작)
        String roadOnly =
                preferNonBlank( sanitizeRoad(addressForGeocode),
                        extractRoadCore(dto.getAddress()) ); // dto.address에서 도로명+번지만 뽑기

        // 2) 후보 쿼리 생성: 순서대로 시도 (첫 성공 시 사용)
        java.util.List<String> candidates = new java.util.ArrayList<>();
        if (notBlank(roadOnly)) {
            candidates.add(roadOnly);                    // "서울특별시 종로구 창경궁로 254"
            candidates.add(shortenSi(roadOnly));         // "서울 종로구 창경궁로 254"
            candidates.add(extractGuRoadNum(roadOnly));  // "종로구 창경궁로 254"
            candidates.add(extractRoadNum(roadOnly));    // "창경궁로 254"
            candidates.add(joinRoadNumber(roadOnly));    // "창경궁로254"
        }
        // 최후의 보루: 전체 주소에서 괄호/동/호 등 제거한 버전
        String fullNormalized = sanitizeRoad(dto.getAddress());
        if (notBlank(fullNormalized)) {
            candidates.add(fullNormalized);
        }
        // 중복/공백 제거
        candidates = candidates.stream().filter(this::notBlank).distinct().toList();

        // 3) 순차 지오코딩
        GeocodingService.LatLng ll = null;
        for (String q : candidates) {
            ll = geocodingService.geocode(q);
            if (ll != null) break;
        }

        if (ll != null) {
            dto.setGpsLat(round(ll.lat(), 6));
            dto.setGpsLng(round(ll.lng(), 6));
        } else {
            dto.setGpsLat(null);
            dto.setGpsLng(null);
        }

        // 4) 시설 + QR 생성
        Long facilityId = facilitiesService.createFacilityWithQr(dto, managerId);

        // 5) 템플릿 연결
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

// ========== 유틸 ==========

    // dto.address에서 "도로명+번지" 핵심만 추출 (예: "서울특별시 종로구 창경궁로 254 55" → "서울특별시 종로구 창경궁로 254")
    private static String extractRoadCore(String s) {
        if (s == null) return null;
        String t = s.trim();
        t = t.replaceAll("\\(.*?\\)", "");  // 괄호 제거
        // 앞에서부터 "…로|…길 + 번지(숫자[-숫자] 가능)"까지 캡처
        java.util.regex.Matcher m = java.util.regex.Pattern
                .compile("^(.+?(?:로|길)\\s*\\d+(?:-\\d+)?)\\b")
                .matcher(t);
        if (m.find()) {
            return m.group(1).replaceAll("\\s{2,}"," ").trim();
        }
        return sanitizeRoad(t); // 그래도 못 뽑으면 일반 정규화
    }

    // 괄호/동/호 등 제거 + 공백 정리
    private static String sanitizeRoad(String road) {
        if (road == null) return null;
        String s = road.trim();
        s = s.replaceAll("\\(.*?\\)", ""); // (건물명) 제거
        s = s.replaceAll("\\b\\d+동\\b", "");
        s = s.replaceAll("\\b\\d+호\\b", "");
        s = s.replaceAll("\\b\\d+층\\b", "");
        s = s.replaceAll("\\s{2,}", " ").trim();
        return s;
    }

    // "서울특별시" → "서울", "부산광역시" → "부산" 등
    private static String shortenSi(String s) {
        if (s == null) return null;
        return s.replace("특별시","").replace("광역시","").replace("자치시","")
                .replaceAll("\\s{2,}"," ").trim();
    }

    // "서울특별시 종로구 창경궁로 254" → "종로구 창경궁로 254"
    private static String extractGuRoadNum(String s) {
        if (s == null) return null;
        return s.replaceFirst("^.*?\\s(\\S+구|\\S+군)\\s", "$1 ").trim();
    }

    // "서울특별시 종로구 창경궁로 254" → "창경궁로 254"
    private static String extractRoadNum(String s) {
        if (s == null) return null;
        java.util.regex.Matcher m = java.util.regex.Pattern
                .compile("([가-힣A-Za-z0-9]+(?:로|길)\\s*\\d+(?:-\\d+)?)")
                .matcher(s);
        return m.find() ? m.group(1).trim() : s;
    }

    // "창경궁로 254" → "창경궁로254"
    private static String joinRoadNumber(String s) {
        if (s == null) return null;
        return s.replaceAll("([가-힣A-Za-z]+(?:로|길))\\s+(\\d+(?:-\\d+)?)", "$1$2");
    }

    private boolean notBlank(String s) {
        return s != null && !s.trim().isEmpty();
    }

    private static String preferNonBlank(String a, String b) {
        if (a != null && !a.isBlank()) return a;
        return b;
    }

    // 쉼표 구분 문자열 → Long 리스트
    private List<Long> parseIds(String csv) {
        if (csv == null || csv.isBlank()) {
            return new ArrayList<>();
        }
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
                       @RequestParam(value = "removedTemplateIds", required = false) String removedTemplateIds,
                       @RequestParam(value = "addedTemplateIds", required = false) String addedTemplateIds,
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

        // 2) 점검표 변경사항 처리
        if (rows > 0) {
            // 변경사항 방식 (삭제/추가 개별 처리)
            List<Long> removedIds = parseIds(removedTemplateIds);
            List<Long> addedIds = parseIds(addedTemplateIds);

            if (!removedIds.isEmpty() || !addedIds.isEmpty()) {
                facilitiesService.updateFacilityTemplates(facilityId, removedIds, addedIds, managerId);
                System.out.println("점검표 변경 처리 - 삭제: " + removedIds + ", 추가: " + addedIds);
            }

        /* 또는 전체 교체 방식 (더 단순하지만 덜 효율적)
        if (facility.getTemplateIds() != null && !facility.getTemplateIds().isBlank()) {
            facilitiesService.replaceAllFacilityTemplates(
                    facilityId,
                    parseIds(facility.getTemplateIds()),
                    managerId
            );
        } else {
            // 모든 점검표 연결 해제
            facilitiesService.replaceAllFacilityTemplates(facilityId, new ArrayList<>(), managerId);
        }
        */
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