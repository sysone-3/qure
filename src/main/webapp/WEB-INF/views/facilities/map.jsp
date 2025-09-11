<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>Kakao 지도 시작하기</title>
    <style>
        html, body {
            margin: 0;
            padding: 0;
            height: 100%;   /* 부모 요소 높이 */
        }
        #map {
            width: 100%;    /* 가로 전체 */
            height: 100%;   /* 세로 전체 */
        }
    </style>
</head>
<body>

<!-- 컨트롤러에서 전달한 kakaoAppKey -->
<script type="text/javascript"
        src="//dapi.kakao.com/v2/maps/sdk.js?appkey=${kakaoAppKey}">
</script>

<script>
    var container = document.getElementById('map');
    var options = {
        center: new kakao.maps.LatLng(33.450701, 126.570667),
        level: 3
    };
    var map = new kakao.maps.Map(container, options);
</script>
</body>
</html>
