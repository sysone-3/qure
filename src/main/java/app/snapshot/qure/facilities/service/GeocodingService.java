package app.snapshot.qure.facilities.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class GeocodingService {

    private final RestTemplate restTemplate;
    private final String kakaoApiKey;

    public GeocodingService(@Value("${kakao.api.key}") String kakaoApiKey) {
        this.kakaoApiKey = kakaoApiKey;
        this.restTemplate = new RestTemplate();
        System.out.println("카카오 API 키 로드됨: " + kakaoApiKey.substring(0, Math.min(5, kakaoApiKey.length())) + "...");
    }

    public LatLng geocode(String rawAddress) {
        String address = normalizeAddress(rawAddress);
        if (address.isEmpty()) return null;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // 1) 주소 검색 (유사 매칭 + 1건만)
        URI url = UriComponentsBuilder
                .fromHttpUrl("https://dapi.kakao.com/v2/local/search/address.json")
                .queryParam("query", address)              // 한글 그대로 넣어도 됨
                .queryParam("analyze_type", "similar")
                .queryParam("size", 1)
                .build()                                   // 이미-인코딩 아님
                .encode(StandardCharsets.UTF_8)            // 여기서 UTF-8 인코딩
                .toUri();



        try {
            ResponseEntity<KakaoGeocodeResponse> resp =
                    restTemplate.exchange(url, HttpMethod.GET, entity, KakaoGeocodeResponse.class);
            if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null
                    && resp.getBody().documents != null && !resp.getBody().documents.isEmpty()) {

                var doc = resp.getBody().documents.get(0);
                return new LatLng(Double.parseDouble(doc.y), Double.parseDouble(doc.x));
            }
        } catch (Exception e) {
            System.err.println("주소검색 오류: " + e.getMessage());
        }

        // 2) 키워드 검색(축약 쿼리 + 길이 제한 회피)
        String keyword = toKeywordQuery(address);      // 예: "종로구 창경궁로 254"
        if (keyword.isEmpty()) return null;

        // 100바이트 제한 방어
        keyword = trimToBytes(keyword, 100);

        URI kwUrl = UriComponentsBuilder
                .fromHttpUrl("https://dapi.kakao.com/v2/local/search/keyword.json")
                .queryParam("query", keyword)              // 축약한 키워드
                .queryParam("size", 1)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();

        try {
            ResponseEntity<KakaoKeywordResponse> resp =
                    restTemplate.exchange(kwUrl, HttpMethod.GET, entity, KakaoKeywordResponse.class);

            if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null
                    && resp.getBody().documents != null && !resp.getBody().documents.isEmpty()) {

                var doc = resp.getBody().documents.get(0);
                return new LatLng(Double.parseDouble(doc.y), Double.parseDouble(doc.x));
            }
        } catch (Exception e) {
            System.err.println("키워드검색 오류: " + e.getMessage());
        }

        return null;
    }

    /* ---- helpers ---- */

    private static String normalizeAddress(String s) {
        if (s == null) return "";
        // 앞뒤 공백 제거 → 중복 공백 하나로 → 괄호/특수문자 일부 제거
        String cleaned = s.trim()
                .replaceAll("\\s+", " ")
                .replaceAll("[\\p{Z}\\t\\n\\r]+", " ")
                .replaceAll("[()\\[\\]{}]", "");
        return cleaned;
    }

    // "서울특별시 종로구 창경궁로 254" → "종로구 창경궁로 254" 처럼 짧게
    private static String toKeywordQuery(String address) {
        // 행정동/구 중심으로 뒤쪽 도로명+번지 남기기
        // 너무 공격적이면 필요 최소한만 자르세요.
        String a = address;
        // 시/도 제거(매칭폭을 줄여 쿼리 길이 단축)
        a = a.replaceFirst("^(서울특별시|서울시|경기도|부산광역시|대구광역시|인천광역시|광주광역시|대전광역시|울산광역시|세종특별자치시)\\s*", "");
        // 끝의 동/로/길 + 번지 패턴이 있으면 앞부분 더 줄이기
        // 예: "종로구 창경궁로 254" 형태만 유지
        return a;
    }

    private static String trimToBytes(String s, int maxBytes) {
        byte[] bytes = s.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        if (bytes.length <= maxBytes) return s;
        // 바이트 기준으로 안전하게 자르기
        int len = 0, i = 0;
        for (char c : s.toCharArray()) {
            int blen = String.valueOf(c).getBytes(java.nio.charset.StandardCharsets.UTF_8).length;
            if (len + blen > maxBytes) break;
            len += blen; i++;
        }
        return s.substring(0, i);
    }

    public record LatLng(double lat, double lng) {}

    public static class KakaoGeocodeResponse {
        public List<Document> documents;
    }
    public static class Document {
        public String x; // 경도
        public String y; // 위도
        public String address_name;
        public String road_address_name;
    }
    public static class KakaoKeywordResponse {
        public List<KeywordDocument> documents;
    }
    public static class KeywordDocument {
        public String x; public String y;
        public String place_name; public String address_name; public String road_address_name;
    }
}
