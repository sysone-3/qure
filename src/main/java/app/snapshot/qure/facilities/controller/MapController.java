package app.snapshot.qure.facilities.controller;

import app.snapshot.qure.facilities.service.MapService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// 작성자: 김민서
@Controller
public class MapController {

    private final MapService mapService;

    public MapController(MapService mapService) {
        this.mapService = mapService;
    }
    // 설비 등록 폼
    @GetMapping("/map")
    public String addForm(Model model) {
        model.addAttribute("kakaoAppKey", mapService.getKakaoAppKey());
        return "facilities/map";
    }
}
