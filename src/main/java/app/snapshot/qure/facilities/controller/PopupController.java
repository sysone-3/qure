package app.snapshot.qure.facilities.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// 작성자: 김민서
@Controller
@RequestMapping("/popup")
public class PopupController {
    @GetMapping("/juso")
    public String jusoPopup() {
        return "facilities/jusoPopup";
    }

    // JUSO에서 결과를 POST로 되돌려줄 때도 같은 뷰로 렌더
    @PostMapping("/juso")
    public String jusoPopupPost() {
        return "facilities/jusoPopup";
    }
}
