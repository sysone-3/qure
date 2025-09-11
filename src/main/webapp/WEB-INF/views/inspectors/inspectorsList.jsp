<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>점검자 리스트</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="<c:url value='/assets/css/inspectorsList.css'/>" />
</head>
<body>
    <div class="innerContainer">
        <div class="topContainer">
            <span class="title">작업자</span>
            <div class="statusContainer">
                <span class="subTitle">현황</span>
                <div class="buttonContainer">
                    <button class="register">작업자 등록</button>
                    <button class="delete">작업자 삭제</button>
                </div>
            </div>
        </div>

        <div class="boardContainer">
            <span class="totalTitle">전체</span>
            <span class="highlight">0명</span>
        </div>
    </div>
</body>
</html>
