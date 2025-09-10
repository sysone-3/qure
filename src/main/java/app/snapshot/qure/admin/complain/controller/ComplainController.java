package app.snapshot.qure.admin.complain.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import app.snapshot.qure.admin.complain.dto.ComplainDto;
import app.snapshot.qure.admin.complain.service.ComplainService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping(value="/admin")
@Controller
public class ComplainController {
	
	@Autowired
	ComplainService complainService;
	
	@GetMapping("/complain")
	public String complainlistPage(Model model,
			HttpSession session,	
			@RequestParam(required=false) String q,
		    @RequestParam(required=false) String status,
		    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
		    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
		
		//하드 코딩
		int managerId = 20;
		
		LocalDateTime fromDt = (from != null) ? from.atStartOfDay() : null;
	    LocalDateTime toExclusive = (to != null) ? to.plusDays(1).atStartOfDay() : null;
		
		// 해당 관리자에게 배정된 모든 시설의 민원신고 목록
		List<ComplainDto> complainlist = complainService.getAllComplainList(managerId, q, status, fromDt, toExclusive);
		
		model.addAttribute("managerId", managerId);
		model.addAttribute("complainlist", complainlist);
		
		return "complain/complainlist";
	}
	
	
	@GetMapping("/complain/detail")
	public String complainDetail(int id, HttpSession session, Model model) {
	    // TODO: 세션에서 매니저 ID를 꺼내 쓰면 교체
	    int managerId = 20;
	    ComplainDto selected = complainService.getByIdForManager(id, managerId);
	    model.addAttribute("selected", selected);
	    return "complain/_detail"; // 프래그먼트 JSP
	}
	
	@PostMapping("/complain/delete")
	@ResponseBody
	public java.util.Map<String, Object> delete(@RequestParam int id, HttpSession session) {
	    int managerId = 20; // TODO: 세션에서 가져오기
	    int n = complainService.deleteByIdForManager(id, managerId);
	    return java.util.Collections.singletonMap("ok", n > 0);
	}
	
	@PostMapping("/complain/status")
	@ResponseBody
	public Map<String, Object> updateStatus(@RequestParam int id,
	                                        @RequestParam String status,
	                                        HttpSession session) {
	    int managerId = 20; // TODO: 세션에서 가져오기
	    if (!"IN_PROGRESS".equals(status) && !"RESOLVED".equals(status)) {
	        return java.util.Collections.singletonMap("ok", false);
	    }
	    int n = complainService.updateStatusForManager(id, managerId, status);
	    return java.util.Collections.singletonMap("ok", n > 0);
	}



}
