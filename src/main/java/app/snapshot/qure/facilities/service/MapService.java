package app.snapshot.qure.facilities.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Getter
@Service
public class MapService {
    private final String kakaoAppKey;

    public MapService(@Value("${kakao.app.key}") String kakaoAppKey) {
        this.kakaoAppKey = kakaoAppKey;
    }



}
