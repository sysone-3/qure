<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>설비 추가</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="<c:url value='/resources/css/facilities_add.css'/>">
</head>
<body>
<div class="page">
    <div class="form-card">
        <form action="<c:url value='/facilities'/>" method="post">
            <!-- (스프링 시큐리티 쓰면 CSRF 토큰) -->
            <c:if test="${not empty _csrf}">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </c:if>

            <div class="cell">시설명</div>
            <input type="text" name="name" placeholder="예: 본관 전기실" required>

            <div class="cell">설비 주소</div>
            <input type="text" name="address" placeholder="예: 서울시 xx구 xx로 123">

            <div class="cell">도메인</div>
            <!-- 고정 도메인이면 셀렉트로 -->
            <select name="domain">
                <option value="청결">청결</option>
                <option value="순찰">순찰</option>
                <option value="소방">소방</option>
            </select>

            <div class="cell">층/존</div>
            <div class="row">
                <input type="text" name="floor" placeholder="층 (예: B2)">
                <input type="text" name="zone"  placeholder="존 (예: Z-3)">
            </div>

            <div class="cell">좌표</div>
            <div class="row">
                <input type="number" step="0.000001" name="gpsLat" placeholder="위도 (예: 37.566535)">
                <input type="number" step="0.000001" name="gpsLng" placeholder="경도 (예: 126.977969)">
            </div>

            <div class="cell">상태</div>
            <select name="status">
                <option value="ACTIVE">ACTIVE</option>
                <option value="INACTIVE">INACTIVE</option>
            </select>

            <div class="cell">메모</div>
            <textarea name="memo" rows="4" placeholder="특이사항 등"></textarea>

            <div class="cell">담당 관리자 ID</div>
            <input type="number" name="managersId" placeholder="예: 1">

            <div class="cell">점검자 ID</div>
            <input type="number" name="inspectorId" placeholder="예: 1">

            <!-- 필요하면 점검표 선택(템플릿) 등은 별도 화면/팝업으로 -->
            <!--
            <div class="cell">점검표 선택</div>
            <a class="btn btn-brand" href="<c:url value='/templates/select'/>">점검표 추가</a>
            -->

            <div class="form-actions">
                <button type="submit" class="btn btn-brand">등록</button>
                <a class="btn" href="<c:url value='/facilities'/>">취소</a>
            </div>
        </form>
    </div>
</div>
</body>
</html>
