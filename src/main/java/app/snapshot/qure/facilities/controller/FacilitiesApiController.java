package app.snapshot.qure.facilities.controller;

import app.snapshot.qure.facilities.dto.FacilityMarkerDto;
import app.snapshot.qure.facilities.service.FacilitiesService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/facilities")
public class FacilitiesApiController {

    private final FacilitiesService facilitiesService;

    public FacilitiesApiController(FacilitiesService facilitiesService) {
        this.facilitiesService = facilitiesService;
    }

    @GetMapping("/markers")
    public List<FacilityMarkerDto> markers() {
        Long managerId = mustManagerId(); // 기존에 쓰시던 방식 그대로 사용
        return facilitiesService.findMarkersForManager(managerId);
    }

    private Long mustManagerId() {
        // TODO: 로그인 붙이기 전 임시 값
        return 1L;
    }
}