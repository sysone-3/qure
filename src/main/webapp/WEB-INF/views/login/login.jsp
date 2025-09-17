<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>The Qure</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="<c:url value='/assets/css/login.css'/>" />
</head>
<body>
    <!-- 왼쪽 영역 -->
    <div class="left-container">
        <div class="text-container">
            QR 하나로 끝나는 <br>
            완벽한 시설 점검<br>
            <span class="highlight">THE QURE</span>
        </div>
       <a href="<c:url value='/oauth2/authorization/kakao'/>" class="kakao-login">
           <img src="<c:url value='/assets/images/kakao.svg'/>" alt="카카오 로그인 버튼" />
       </a>
        <div class="circle-bottom"></div>
    </div>

    <!-- 오른쪽 영역 -->
    <div class="inner-container">
        <div class="right-container">
            <div class="circle-right"></div>
        </div>
        <div class="dashboard">
            <img src="<c:url value='/assets/images/dashboard.webp'/>" alt="대시보드 미리보기" />
        </div>
    </div>
</body>
</html>
