package app.snapshot.qure.facilities.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Getter
@Service
@PropertySource("classpath:application-map.properties")
public class MapService {
    private final String kakaoAppKey;
    private final String kakaoApiKey;

    public MapService(@Value("${kakao.app.key}") String kakaoAppKey, @Value("${kakao.api.key}") String kakaoApiKey) {
        this.kakaoAppKey = kakaoAppKey;
        this.kakaoApiKey = kakaoApiKey;
    }



}
