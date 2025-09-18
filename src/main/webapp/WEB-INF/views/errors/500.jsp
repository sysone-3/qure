<%--작성자: 최이서--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8"/>
    <title>일시적인 오류가 발생했습니다</title>
    <link rel="stylesheet" href="<c:url value='/assets/css/errors.css'/>"/>
</head>
<body>
<div class="error-wrap">
    <div class="error-card">
        <img class="error-illust" src="<c:url value='/assets/images/error-500.png'/>" alt="500 error">

        <h1 class="error-title">500 Internal Server Error</h1>
        <p class="error-sub">서비스 이용에 불편을 드려 죄송합니다.</p>
        <p class="error-sub">시스템 에러로 인해 페이지를 표시할 수 없습니다. 잠시 후 시도하세요.</p>

        <div class="btns">
            <button class="btn" onclick="location.reload()">다시 시도</button>
            <a class="btn primary" href="<c:url value='/'/>">홈으로</a>
        </div>

        <p class="small">요청ID: <c:out value="${requestScope.requestId}"/></p>
    </div>
</div>
</body>
</html>
