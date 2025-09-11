package app.snapshot.qure.facilities.controller;

import app.snapshot.qure.facilities.dto.FacilityMarkerDto;
import app.snapshot.qure.facilities.service.FacilitiesService;
import app.snapshot.qure.login.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
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
    public List<FacilityMarkerDto> markers(HttpSession session) {
        Long managerId = SessionUtil.mustManagerId(session);
        return facilitiesService.findMarkersForManager(managerId);
    }

}