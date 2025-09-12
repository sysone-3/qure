// src/main/java/app/snapshot/qure/admin/complain/controller/ComplainController.java
package app.snapshot.qure.admin.complain.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import app.snapshot.qure.admin.complain.dto.ComplainDto;
import app.snapshot.qure.admin.complain.service.ComplainService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin")
@Controller
public class ComplainController {

    private final ComplainService complainService;

    @GetMapping("/complain")
    public String complainlistPage(
            Model model,
            HttpSession session,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate to) {
    	
    	model.addAttribute("nav","complain");

        int managerId = 20;
        LocalDateTime fromDt = (from == null) ? null : from.atStartOfDay(); // 해당날짜의 00시로 변경
        LocalDateTime toEx    = (to == null) ? null : to.plusDays(1).atStartOfDay(); // 그 다음 날짜의 00시로 변경
        int p = Math.max(page, 1); //페이지 번호 최소 1 보장
        int s = Math.max(size, 1); //페이지 당 목록 수 최소 1 보장
        int offset = (p - 1) * s; // 다음 페이지 목록 시작점 지정 (페이지당 10개 -> 다음 페이지는 11번부터)

        List<ComplainDto> rows = complainService.getAllComplainList(managerId, q, status, fromDt, toEx, offset, s); //필터링 조회
        int total = complainService.countComplainList(managerId, q, status, fromDt, toEx);

        model.addAttribute("complainlist", rows);
        model.addAttribute("page", p);
        model.addAttribute("size", s);
        model.addAttribute("total", total);
        model.addAttribute("pages", (int) Math.ceil(total / (double) s));
        return "complain/complainlist";
    }

    @GetMapping("/complain/detail")
    public String complainDetail(@RequestParam int id, HttpSession session, Model model) {
        int managerId = 20;
        ComplainDto selected = complainService.getByIdForManager(id, managerId);
        model.addAttribute("selected", selected);
        return "complain/_detail";
    }

    @PostMapping("/complain/delete")
    @ResponseBody
    public Map<String, Object> delete(@RequestParam int id, HttpSession session) {
        int managerId = 20;
        int n = complainService.deleteByIdForManager(id, managerId);
        return java.util.Collections.singletonMap("ok", n > 0);
    }

    @PostMapping("/complain/status")
    @ResponseBody
    public Map<String,Object> updateStatus(@RequestParam int id,
                                           @RequestParam String status) {
        int managerId = 20;
        status = status == null ? null : status.trim().toUpperCase();  // ← 추가
        if (!"IN_PROGRESS".equals(status) && !"RESOLVED".equals(status)) {
            return java.util.Collections.singletonMap("ok", false);
        }
        int n = complainService.updateStatusForManager(id, managerId, status);
        return java.util.Collections.singletonMap("ok", n > 0);
    }

}
