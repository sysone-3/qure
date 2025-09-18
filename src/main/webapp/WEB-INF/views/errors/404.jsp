<%--작성자: 최이서--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8"/>
    <title>페이지를 찾을 수 없습니다</title>
    <link rel="stylesheet" href="<c:url value='/assets/css/errors.css'/>"/>
</head>
<body>
<div class="error-wrap">
    <div class="error-card">
        <img class="error-illust" src="<c:url value='/assets/images/error-404.png'/>" alt="404 error">

        <h1 class="error-title">404 Not Found</h1>
        <p class="error-sub">존재하지 않는 주소를 입력하셨거나,</p>
        <p class="error-sub">요청하신 페이지의 주소가 변경, 삭제되어 찾을 수 없습니다.</p>

        <div class="btns">
            <a class="btn primary" href="<c:url value='/'/>">홈으로</a>
        </div>
    </div>
</div>
</body>
</html>
