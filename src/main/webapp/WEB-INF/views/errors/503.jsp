<%--작성자: 최이서--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8"/>
    <title>잠시 점검 중입니다</title>
    <link rel="stylesheet" href="<c:url value='/assets/css/errors.css'/>"/>
</head>
<body>
<div class="error-wrap">
    <div class="error-card" id="card" data-retry="<c:out value='${header[\"Retry-After\"]}' default='15'/>">
        <img class="error-illust" src="<c:url value='/assets/images/error-503.png'/>" alt="503 error">

        <h1 class="error-title">503 Service Unavailable</h1>
        <p class="error-sub">더 나은 서비스를 위해 시스템 점검 중입니다.</p>
        <p class="error-sub">잠시 후 다시 시도해주세요.</p>

        <div class="btns">
            <a class="btn" href="<c:url value='/login'/>">로그인</a>
            <a class="btn primary" href="<c:url value='/'/>">홈으로</a>
        </div>

        <p class="small" id="retryText"></p>
    </div>
</div>

<script>
    (function () {
        var el = document.getElementById('card');
        var retry = parseInt(el.getAttribute('data-retry'), 10);
        if (!isNaN(retry) && retry > 0) {
            var left = retry, label = document.getElementById('retryText');
            label.textContent = left + "초 후 자동으로 다시 시도합니다.";
            var timer = setInterval(function(){
                left--; if(left <= 0){ clearInterval(timer); location.reload(); }
                else { label.textContent = left + "초 후 자동으로 다시 시도합니다."; }
            }, 1000);
        }
    })();
</script>
</body>
</html>
