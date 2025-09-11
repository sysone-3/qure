<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width,initial-scale=1"/>
    <title>Kakao 지도</title>
    <style>
        html, body { height:100%; margin:0; }
        .map-wrap {
            position: relative !important;
            height: 100vh !important;
            width: 100% !important;
            overflow: hidden !important;
        }
        #map {
            position: absolute !important;
            top: 0 !important;
            left: 0 !important;
            width: 100% !important;
            height: 100% !important;
            min-height: 400px !important;
        }
        .iw {
            padding:6px 8px;
            font-size:14px;
            line-height:1.4;
            background:rgba(255,255,255,.95);
        }
    </style>
    <script src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=${kakaoAppKey}&autoload=false"></script>
</head>
<body>
<div class="map-wrap"><div id="map"></div></div>

<script>
    function safeRelayout(map) {
        if (!map) return;
        try {
            const center = map.getCenter();
            map.relayout();
            map.setCenter(center);
        } catch (e) {
            console.warn('Relayout error:', e);
        }
    }

    kakao.maps.load(async function () {
        const container = document.getElementById('map');
        const map = new kakao.maps.Map(container, {
            center: new kakao.maps.LatLng(37.5665, 126.9780),
            level: 7
        });

        // 리사이즈 이벤트
        let resizeTimeout;
        window.addEventListener('resize', () => {
            clearTimeout(resizeTimeout);
            resizeTimeout = setTimeout(() => safeRelayout(map), 200);
        });

        try {
            const res = await fetch('<c:url value="/api/facilities/markers"/>', {
                credentials: 'same-origin',
                headers: {
                    'Accept': 'application/json'
                }
            });

            if (!res.ok) {
                throw new Error('API 실패: ' + res.status);
            }

            const markers = await res.json();
            console.log('마커 데이터:', markers);

            if (!markers || markers.length === 0) {
                new kakao.maps.InfoWindow({
                    position: map.getCenter(),
                    content: '<div class="iw">표시할 시설이 없습니다.</div>'
                }).open(map);
                return;
            }

            const bounds = new kakao.maps.LatLngBounds();
            const markerImage = new kakao.maps.MarkerImage(
                "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png",
                new kakao.maps.Size(24, 35)
            );

            let markerCount = 0;
            markers.forEach((item, index) => {
                // 좌표 검증
                const lat = parseFloat(item.gpsLat);
                const lng = parseFloat(item.gpsLng);

                if (isNaN(lat) || isNaN(lng) || lat === 0 || lng === 0) {
                    console.warn(`마커 ${index} 좌표 오류:`, item);
                    return;
                }

                try {
                    const pos = new kakao.maps.LatLng(lat, lng);
                    const marker = new kakao.maps.Marker({
                        position: pos,
                        image: markerImage
                    });
                    marker.setMap(map);
                    bounds.extend(pos);
                    markerCount++;

                    // JavaScript에서 encodeURIComponent 사용 (JSP가 아닌)
                    const facilityName = item.name || '이름없음';
                    const mapLink = 'https://map.kakao.com/link/map/'
                        + encodeURIComponent(facilityName) + ',' + lat + ',' + lng;
                    const dirLink = 'https://map.kakao.com/link/to/'
                        + encodeURIComponent(facilityName) + ',' + lat + ',' + lng;
                    const detailLink = '<c:url value="/facilities/"/>' + item.facilityId;

                    const html =
                        '<div class="iw">' +
                        '<div><b>' + facilityName + '</b></div>' +
                        '<div style="color:#666">' + (item.address || '') + '</div>' +
                        '<div style="margin-top:6px">' +
                        '<a href="' + detailLink + '">상세보기</a> · ' +
                        '<a href="' + mapLink + '" target="_blank">큰지도</a> · ' +
                        '<a href="' + dirLink + '" target="_blank">길찾기</a>' +
                        '</div>' +
                        '</div>';

                    const infoWindow = new kakao.maps.InfoWindow({ content: html });
                    kakao.maps.event.addListener(marker, 'click', function() {
                        infoWindow.open(map, marker);
                    });

                } catch (markerError) {
                    console.error(`마커 ${index} 생성 실패:`, markerError);
                }
            });

            console.log(`마커 생성 완료: ${markerCount}/${markers.length}개`);

            // 지도 범위 설정
            if (markerCount > 0) {
                if (markerCount === 1) {
                    map.setLevel(3);
                    // 첫 번째 유효한 마커 위치로 이동
                    const firstMarker = markers.find(item => {
                        const lat = parseFloat(item.gpsLat);
                        const lng = parseFloat(item.gpsLng);
                        return !isNaN(lat) && !isNaN(lng) && lat !== 0 && lng !== 0;
                    });
                    if (firstMarker) {
                        map.setCenter(new kakao.maps.LatLng(
                            parseFloat(firstMarker.gpsLat),
                            parseFloat(firstMarker.gpsLng)
                        ));
                    }
                } else {
                    map.setBounds(bounds);
                }
            }

            // 최종 레이아웃 조정
            setTimeout(() => safeRelayout(map), 100);

        } catch (error) {
            console.error('지도 로딩 오류:', error);
            new kakao.maps.InfoWindow({
                position: map.getCenter(),
                content: '<div class="iw">오류: 시설 데이터를 불러오지 못했습니다.</div>'
            }).open(map);
        }
    });
</script>
</body>
</html>