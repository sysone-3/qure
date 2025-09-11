<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>QR 발급</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="<c:url value='/assets/css/facilities_add.css'/>">
</head>
<body>
<div class="page">
    <div class="qr-card">
        <h2>설비가 등록되었습니다</h2>
        <p>아래 QR을 다운로드하세요</p>

        <div class="qr-box">
            <img src="<c:url value='/qr/${tag.tagId}.png'/>" alt="QR 코드">
        </div>

        <div class="actions">
            <a class="btn btn-brand" download="facility-${facility.facilityId}-qr.png"
               href="<c:url value='/qr/${tag.tagId}.png'/>">QR 다운로드</a>
            <a class="btn" href="<c:url value='/facilities/${facility.facilityId}'/>">완료</a>
        </div>
    </div>
</div>
</body>
</html>
