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
    </style>

    <!-- 기본 레이아웃 CSS 추가 -->
    <link rel="stylesheet" href="<c:url value='/assets/css/reset.css'/>" />
    <link rel="stylesheet" href="<c:url value='/assets/css/navigation.css'/>" />
    <link rel="stylesheet" href="<c:url value='/assets/css/components.css'/>" />
    <link rel="stylesheet" href="<c:url value='/assets/css/map.css'/>" />

    <!-- autoload=false 로드 -->
    <script defer src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=${kakaoAppKey}&libraries=services&autoload=false"></script>
</head>
<body>
<!-- nav 변수 설정 (사이드바에서 '지도' 메뉴 활성화) -->
<c:set var="nav" value="map" scope="request"/>

<div class="layout">
    <!-- 전역 메뉴 사이드바 -->
    <%@ include file="/WEB-INF/views/fragments/sidebar.jspf" %>

    <!-- 메인 콘텐츠 -->
    <main class="content">
        <div class="map-container">
            <!-- 시설목록 패널 -->
            <div class="facility-panel">
                <div class="facility-header"><h2>시설 목록</h2></div>
                <div id="facility-list" class="facility-list">
                    <div class="loading">시설 정보를 불러오는 중...</div>
                </div>
            </div>

            <!-- 지도 -->
            <div class="map-wrap"><div id="map"></div></div>
        </div>
    </main>
</div>
<script defer>
    // 전역 변수 선언
    var map;
    var facilityData = [];
    var markers = [];
    var overlays = []; // InfoWindow 대신 CustomOverlay 관리

    function formatDate(timestamp) {
        if (!timestamp || timestamp === '—') return '—';
        const date = new Date(Number(timestamp));
        if (isNaN(date.getTime())) return '—';
        const y = date.getFullYear();
        const m = String(date.getMonth()+1).padStart(2,'0');
        const d = String(date.getDate()).padStart(2,'0');
        return y + '.' + m + '.' + d;
    }

    function renderSidebar(mode, payload) {
        var box = document.getElementById('facility-list');

        if (mode === 'list') {
            renderFacilityList(facilityData);
            return;
        }

        // mode === 'detail'
        var d = payload || {};
        var title = d.name || '시설';
        var addr  = d.address || '';
        var domain = d.domain || '—';
        var zone   = d.zone || '—';
        var updatedAt = d.updatedAt ? formatDate(d.updatedAt) : '—';
        var inspectorId   = (typeof d.inspectorId !== 'undefined' && d.inspectorId !== null) ? d.inspectorId : '—';
        var inspectorName = d.inspectorName || '—';
        var inspectorPhone= d.inspectorPhone || '—';
        var detailLink    = '<c:url value="/facilities/"/>' + d.facilityId;

        var html = ''
            + '<div style="padding:16px; border-bottom:1px solid #eee; background:#f8f9fa; display:flex; align-items:center; gap:8px">'
            + '  <button id="backToListBtn" style="border:1px solid #ddd; background:#fff; border-radius:8px; padding:6px 10px; cursor:pointer">← 목록</button>'
            + '  <h2 style="margin:0; font-size:18px; color:#333">상세 정보</h2>'
            + '</div>'

            + '<div style="padding:18px;">'
            + '  <div style="font-weight:700; font-size:18px; margin-bottom:4px;">' + title + '</div>'
            + '  <div style="color:#666; font-size:13px; margin-bottom:16px;">' + addr + '</div>'

            + '  <table style="width:100%; border-collapse:collapse; font-size:14px;"><tbody>'
            + '    <tr><td style="padding:8px 0; color:#888; width:32%">시설 ID</td><td style="padding:8px 0;">' + d.facilityId + '</td></tr>'
            + '    <tr><td style="padding:8px 0; color:#888;">도메인</td><td style="padding:8px 0;">' + domain + '</td></tr>'
            + '    <tr><td style="padding:8px 0; color:#888;">존/위치</td><td style="padding:8px 0;">' + zone + '</td></tr>'
            + '    <tr><td style="padding:8px 0; color:#888;">최근 점검일</td><td style="padding:8px 0;">' + updatedAt + '</td></tr>'
            + '  </tbody></table>'

            + '  <div style="margin:18px 0 8px; font-weight:700;">점검자 정보</div>'
            + '  <table style="width:100%; border-collapse:collapse; font-size:14px;"><tbody>'
            + '    <tr><td style="padding:8px 0; color:#888; width:32%">ID</td><td style="padding:8px 0;">' + inspectorId + '</td></tr>'
            + '    <tr><td style="padding:8px 0; color:#888;">이름</td><td style="padding:8px 0;">' + inspectorName + '</td></tr>'
            + '    <tr><td style="padding:8px 0; color:#888;">전화번호</td><td style="padding:8px 0;">' + inspectorPhone + '</td></tr>'
            + '  </tbody></table>'

            + '  <div style="margin-top:16px;">'
            + '    <a href="' + detailLink + '" class="btn-detail" style="display:inline-block; padding:10px 14px; background:#ffeab3; border-radius:10px; text-decoration:none; color:#333; font-weight:600;">상세보기</a>'
            + '  </div>'
            + '</div>';

        box.innerHTML = html;
        document.getElementById('backToListBtn').addEventListener('click', function() {
            renderSidebar('list');
        });
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

            html += ''
                + '<div class="facility-item" data-index="' + i + '" onclick="selectFacility(' + i + ')">'
                + '  <div class="facility-name">' + name + '</div>'
                + '  <div class="facility-address">' + addr + '</div>'
                + '  <div class="facility-links">'
                + '    <a href="' + detailLink + '" onclick="event.stopPropagation()">상세보기</a>'
                + '  </div>'
                + '</div>';
        }
        list.innerHTML = html;
    }

    async function selectFacility(index) {
        var f = facilityData[index];
        if (!f) return;

        var lat = Number(String(f.gpsLat).trim());
        var lng = Number(String(f.gpsLng).trim());
        if (!isNaN(lat) && !isNaN(lng)) {
            var pos = new kakao.maps.LatLng(lat, lng);
            map.setCenter(pos);
            map.setLevel(3);
        }

        document.querySelectorAll('.facility-item').forEach(function(el){ el.classList.remove('active'); });
        var el = document.querySelector('.facility-item[data-index="' + index + '"]');
        if (el) el.classList.add('active');

        try {
            var res = await fetch('<c:url value="/api/facilities/"/>' + f.facilityId + '/summary', {
                credentials: 'same-origin',
                headers: { 'Accept': 'application/json' }
            });
            if (!res.ok) throw new Error('요약 API 실패: ' + res.status);
            var summary = await res.json();

            renderSidebar('detail', summary);

            // 해당 마커의 말풍선 열기
            if (overlays[index]) {
                overlays.forEach(function(ov){ ov && ov.setMap(null); });
                overlays[index].setMap(map);
            }
        } catch (e) {
            console.error(e);
            alert('상세 정보를 불러오지 못했습니다.');
        }
    }

    function safeRelayout(map) {
        if (map && typeof map.relayout === 'function') {
            map.relayout();
        }
    }

    // CustomOverlay 생성 유틸
    function createBalloonOverlay(position, innerHTML, asList) {
        var container = document.createElement('div');
        container.className = 'balloon' + (asList ? ' balloon--list' : '');
        container.innerHTML = innerHTML;
        return new kakao.maps.CustomOverlay({
            position: position,
            content: container,
            yAnchor: 1,
            xAnchor: 0.5
        });
    }

    // 실제 지도 초기화 함수
    async function initMap() {
        map = new kakao.maps.Map(document.getElementById('map'), {
            center: new kakao.maps.LatLng(37.5665, 126.9780),
            level: 7
        });

        // 리사이즈 시 relayout
        var resizeTimeout;
        window.addEventListener('resize', function(){
            clearTimeout(resizeTimeout);
            resizeTimeout = setTimeout(function(){ safeRelayout(map); }, 200);
        });

        try {
            const res = await fetch('<c:url value="/api/facilities/markers"/>', {
                credentials:'same-origin',
                headers:{ 'Accept':'application/json' }
            });
            if (!res.ok) throw new Error('API 실패: ' + res.status);

            const data = await res.json();
            if (!data || data.length === 0) {
                document.getElementById('facility-list').innerHTML =
                    '<div class="loading">표시할 시설이 없습니다.</div>';
                createBalloonOverlay(map.getCenter(), '<div>표시할 시설이 없습니다.</div>', false).setMap(map);
                return;
            }

            facilityData = data;
            renderFacilityList(data);

            // 위치 그룹화
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

            var bounds = new kakao.maps.LatLngBounds();
            var locationCount = 0;

            // 마커 이미지
            var imgSingle = new kakao.maps.MarkerImage(
                'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png',
                new kakao.maps.Size(24, 35),
                { offset: new kakao.maps.Point(12, 35) }
            );
            var imgMulti = imgSingle;

            // 마커 + 오버레이 생성
            locationGroups.forEach(function(group){
                var lat = group.lat, lng = group.lng, facilities = group.facilities;
                var pos = new kakao.maps.LatLng(lat, lng);
                var marker = new kakao.maps.Marker({
                    position: pos,
                    image: facilities.length > 1 ? imgMulti : imgSingle,
                    map: map,
                    clickable: true
                });
                bounds.extend(pos);
                locationCount++;

                var overlay;
                if (facilities.length === 1) {
                    var f = facilities[0];
                    var name = f.name || '이름없음';
                    var addr = f.address || '';
                    var detailLink = '<c:url value="/facilities/"/>' + f.facilityId;
                    var html = '<div><b>' + name + '</b></div>' +
                        '<div style="color:#666">' + addr + '</div>' +
                        '<div style="margin-top:6px"><a href="' + detailLink + '">상세보기</a></div>';
                    overlay = createBalloonOverlay(pos, html, false);
                    overlays[f.originalIndex] = overlay;
                    markers[f.originalIndex]  = marker;
                } else {
                    var listHtml = '<div class="balloon-title">📍 이 위치의 시설들 (' + facilities.length + '개)</div>';
                    facilities.forEach(function(ff){
                        var nm = ff.name || '이름없음';
                        var ad = ff.address || '';
                        listHtml += '<div class="balloon-item" data-idx="' + ff.originalIndex + '">' +
                            '  <div class="name">' + nm + '</div>' +
                            '  <div class="addr">' + ad + '</div>' +
                            '</div>';
                    });
                    overlay = createBalloonOverlay(pos, listHtml, true);

                    var contentEl = overlay.getContent();
                    contentEl.addEventListener('click', function(e){
                        var itemEl = e.target.closest('.balloon-item');
                        if (!itemEl) return;
                        var idx = Number(itemEl.getAttribute('data-idx'));
                        overlays.forEach(function(ov){ ov && ov.setMap(null); });
                        selectFacility(idx);
                    });

                    facilities.forEach(function(f){
                        overlays[f.originalIndex] = overlay;
                        markers[f.originalIndex]  = marker;
                    });
                }

                kakao.maps.event.addListener(marker, 'click', function () {
                    overlays.forEach(function(ov){ ov && ov.setMap(null); });
                    overlay.setMap(map);
                });
            });

            // 지도 영역 조정
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
            createBalloonOverlay(map.getCenter(), '<div>오류: 시설 데이터를 불러오지 못했습니다.</div>', false).setMap(map);
        }
    }


    // SDK 준비 후 initMap 실행
    function ensureKakaoSdkAndInit() {
        if (window.kakao && kakao.maps && typeof kakao.maps.load === 'function') {
            kakao.maps.load(initMap);
            return;
        }
        var existed = document.querySelector('script[src*="dapi.kakao.com/v2/maps/sdk.js"]');
        if (existed) {
            existed.addEventListener('load', function(){ kakao.maps.load(initMap); });
        }
    }

    window.addEventListener('DOMContentLoaded', ensureKakaoSdkAndInit);
</script>
</body>
</html>
