<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width,initial-scale=1"/>
    <title>Kakao 지도</title>

    <style>
        html, body { height:100%; margin:0; font-family: Arial, sans-serif; }
        .container { display:flex; height:100vh; overflow:hidden; }

        /* 사이드바 */
        .sidebar { width:350px; background:#fff; border-right:1px solid #ddd; overflow-y:auto; flex-shrink:0; }
        .sidebar-header { padding:20px; border-bottom:1px solid #eee; background:#f8f9fa; }
        .sidebar-header h2 { margin:0; font-size:18px; color:#333; }

        .facility-list { padding:0; }
        .facility-item { padding:15px 20px; border-bottom:1px solid #eee; cursor:pointer; transition:background-color .2s; }
        .facility-item:hover { background:#f5f5f5; }
        .facility-item.active { background:#e3f2fd; border-left:4px solid #2196f3; }
        .facility-name { font-weight:bold; font-size:14px; color:#333; margin-bottom:4px; }
        .facility-address { font-size:12px; color:#666; line-height:1.4; }
        .facility-links { margin-top:8px; font-size:12px; }
        .facility-links a { color:#0066cc; text-decoration:none; margin-right:8px; }
        .facility-links a:hover { text-decoration:underline; }

        /* 지도 */
        .map-wrap { flex:1; position:relative; overflow:hidden; }
        #map { width:100%; height:100%; min-height:400px; }

        /* 정보창 */
        .iw { padding:8px 10px; font-size:14px; line-height:1.4; background:rgba(255,255,255,.95); max-width:280px; max-height:300px; overflow-y:auto; }
        .iw a { color:#0066cc; text-decoration:none; }
        .iw a:hover { text-decoration:underline; }

        .loading { text-align:center; padding:40px 20px; color:#666; }

        @media (max-width:768px) {
            .container { flex-direction:column; }
            .sidebar { width:100%; height:200px; border-right:none; border-bottom:1px solid #ddd; }
            .map-wrap { height:calc(100vh - 200px); }
        }
    </style>

    <!-- autoload=false 로드 -->
    <script src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=${kakaoAppKey}&autoload=false"></script>
</head>
<body>
<div class="container">
    <div class="sidebar">
        <div class="sidebar-header"><h2>시설 목록</h2></div>
        <div id="facility-list" class="facility-list">
            <div class="loading">시설 정보를 불러오는 중...</div>
        </div>
    </div>
    <div class="map-wrap"><div id="map"></div></div>
</div>

<script>
    let map = null;
    let markers = [];       // 원본 인덱스별 마커
    let infoWindows = [];   // 원본 인덱스별 정보창
    let facilityData = [];  // API 원본 데이터

    function safeRelayout(m) {
        if (!m) return;
        try { const c = m.getCenter(); m.relayout(); m.setCenter(c); } catch(e) { console.warn(e); }
    }

    function renderFacilityList(facilities) {
        const list = document.getElementById('facility-list');
        if (!facilities || facilities.length === 0) {
            list.innerHTML = '<div class="loading">표시할 시설이 없습니다.</div>';
            return;
        }
        var html = '';
        for (var i = 0; i < facilities.length; i++) {
            var f = facilities[i];
            var lat = Number(String(f.gpsLat).trim());
            var lng = Number(String(f.gpsLng).trim());
            if (isNaN(lat) || isNaN(lng) || lat === 0 || lng === 0) continue;

            var name = f.name ? f.name : '이름없음';
            var addr = f.address ? f.address : '';
            var detailLink = '<c:url value="/facilities/"/>' + f.facilityId;
            var mapLink = 'https://map.kakao.com/link/map/' + encodeURIComponent(name) + ',' + lat + ',' + lng;
            var dirLink = 'https://map.kakao.com/link/to/'  + encodeURIComponent(name) + ',' + lat + ',' + lng;

            html += ''
                + '<div class="facility-item" data-index="' + i + '" onclick="selectFacility(' + i + ')">'
                + '  <div class="facility-name">' + name + '</div>'
                + '  <div class="facility-address">' + addr + '</div>'
                + '  <div class="facility-links">'
                + '    <a href="' + detailLink + '" onclick="event.stopPropagation()">상세보기</a>'
                + '    <a href="' + mapLink + '" target="_blank" onclick="event.stopPropagation()">큰지도</a>'
                + '    <a href="' + dirLink + '" target="_blank" onclick="event.stopPropagation()">길찾기</a>'
                + '  </div>'
                + '</div>';
        }
        list.innerHTML = html;
    }

    function selectFacility(index) {
        var f = facilityData[index];
        if (!f) return;
        var lat = Number(String(f.gpsLat).trim());
        var lng = Number(String(f.gpsLng).trim());
        if (isNaN(lat) || isNaN(lng)) return;

        document.querySelectorAll('.facility-item').forEach(function(el){ el.classList.remove('active'); });
        var el = document.querySelector('.facility-item[data-index="' + index + '"]');
        if (el) el.classList.add('active');

        var pos = new kakao.maps.LatLng(lat, lng);
        map.setCenter(pos);
        map.setLevel(3);

        if (infoWindows[index]) {
            infoWindows.forEach(function(iw){ iw && iw.close(); });
            infoWindows[index].open(map, markers[index]);
        }
    }

    kakao.maps.load(async function () {
        map = new kakao.maps.Map(document.getElementById('map'), {
            center: new kakao.maps.LatLng(37.5665, 126.9780),
            level: 7
        });

        var resizeTimeout;
        window.addEventListener('resize', function(){
            clearTimeout(resizeTimeout);
            resizeTimeout = setTimeout(function(){ safeRelayout(map); }, 200);
        });

        try {
            const res = await fetch('<c:url value="/api/facilities/markers"/>', { credentials:'same-origin', headers:{ 'Accept':'application/json' }});
            if (!res.ok) throw new Error('API 실패: ' + res.status);

            const data = await res.json();
            console.log('마커 데이터:', data);

            if (!data || data.length === 0) {
                document.getElementById('facility-list').innerHTML = '<div class="loading">표시할 시설이 없습니다.</div>';
                new kakao.maps.InfoWindow({ position: map.getCenter(), content: '<div class="iw">표시할 시설이 없습니다.</div>' }).open(map);
                return;
            }

            facilityData = data;
            renderFacilityList(data);

            // ====== 안전한 위치 그룹화 (숫자 좌표 보관) ======
            // key: 문자열, value: { lat:Number, lng:Number, facilities:Array<{... , originalIndex:Number}> }
            var locationGroups = new Map();
            for (var i = 0; i < data.length; i++) {
                var item = data[i];
                var lat = Number(String(item.gpsLat).trim());
                var lng = Number(String(item.gpsLng).trim());
                if (isNaN(lat) || isNaN(lng) || lat === 0 || lng === 0) continue;

                var key = lat.toFixed(6) + ',' + lng.toFixed(6);
                if (!locationGroups.has(key)) locationGroups.set(key, { lat: lat, lng: lng, facilities: [] });
                locationGroups.get(key).facilities.push(Object.assign({}, item, { originalIndex: i }));
            }
            console.log('locationGroups size =', locationGroups.size);

            var bounds = new kakao.maps.LatLngBounds();
            var locationCount = 0;

            // (선택) 커스텀 마커 이미지
            var imgSingle = new kakao.maps.MarkerImage(
                'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png',
                new kakao.maps.Size(24, 35),
                { offset: new kakao.maps.Point(12, 35) }
            );
            var imgMulti = new kakao.maps.MarkerImage(
                'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png',
                new kakao.maps.Size(24, 35),
                { offset: new kakao.maps.Point(12, 35) }
            );

            locationGroups.forEach(function(group){
                var lat = group.lat, lng = group.lng, facilities = group.facilities;
                try {
                    var pos = new kakao.maps.LatLng(lat, lng);
                    var marker = new kakao.maps.Marker({
                        position: pos,
                        image: facilities.length > 1 ? imgMulti : imgSingle,
                        map: map,
                        clickable: true
                    });
                    bounds.extend(pos);
                    locationCount++;

                    // 정보창 HTML (문자열 연결만 사용)
                    var html = '<div class="iw">';
                    if (facilities.length === 1) {
                        var f = facilities[0];
                        var name = f.name ? f.name : '이름없음';
                        var detailLink = '<c:url value="/facilities/"/>' + f.facilityId;

                        html += '<div><b>' + name + '</b></div>'
                            +  '<div style="color:#666">' + (f.address ? f.address : '') + '</div>'
                            +  '<div style="margin-top:6px">'
                            +  '  <a href="' + detailLink + '">상세보기</a> · '
                            +  '</div>';
                    } else {
                        html += '<div style="font-weight:bold; margin-bottom:6px">📍 이 위치의 시설들 (' + facilities.length + '개)</div>';
                        for (var j = 0; j < facilities.length; j++) {
                            var ff = facilities[j];
                            var nm = ff.name ? ff.name : '이름없음';
                            var addr = ff.address ? ff.address : '';
                            html += '<div style="padding:6px 0; border-top:1px solid #eee; cursor:pointer"'
                                +  '     onclick="selectFacility(' + ff.originalIndex + ')">'
                                +  '  <div style="color:#0066cc; font-weight:bold">' + nm + '</div>'
                                +  '  <div style="color:#666">' + addr + '</div>'
                                +  '</div>';
                        }
                    }

                    html += '</div>';

                    var iw = new kakao.maps.InfoWindow({ content: html, removable: true });
                    facilities.forEach(function(f){ markers[f.originalIndex] = marker; infoWindows[f.originalIndex] = iw; });

                    kakao.maps.event.addListener(marker, 'click', function () {
                        infoWindows.forEach(function(x){ x && x.close(); });
                        iw.open(map, marker);
                    });

                } catch (e) {
                    console.error('마커 생성 실패:', e, { lat: lat, lng: lng, facilities: facilities });
                }
            });

            console.log('마커 생성 완료:', locationCount, '개 위치, 총', data.length, '개 시설');

            if (locationCount > 0) {
                if (locationCount === 1) {
                    map.setLevel(3);
                    var first = Array.from(locationGroups.values())[0];
                    map.setCenter(new kakao.maps.LatLng(first.lat, first.lng));
                } else {
                    map.setBounds(bounds);
                }
            }

            setTimeout(function(){ safeRelayout(map); }, 100);

        } catch (err) {
            console.error('지도 로딩 오류:', err);
            document.getElementById('facility-list').innerHTML =
                '<div class="loading">오류: 시설 데이터를 불러오지 못했습니다.</div>';
            new kakao.maps.InfoWindow({ position: map.getCenter(), content: '<div class="iw">오류: 시설 데이터를 불러오지 못했습니다.</div>' }).open(map);
        }
    });
</script>
</body>
</html>
