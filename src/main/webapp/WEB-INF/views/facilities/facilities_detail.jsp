<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>${facility.name} 상세</title>
    <link rel="stylesheet" href="<c:url value='/resources/css/facilities.css'/>">
</head>
<body>
<div class="detail-page">

    <!-- 상단 카드 -->
    <div class="facility-card">
        <div class="facility-info">
            <h2>${facility.name} ${facility.floor}층 ${facility.zone}</h2>
            <p>${facility.address}</p>
        </div>
        <div class="actions">
            <a href="<c:url value='/facilities/${facility.facilityId}/edit'/>" class="btn-edit">수정</a>
        </div>
    </div>

    <!-- QR + 점검표 버튼 -->
    <div class="checklist-section">
        <div class="qr-box">
            <img src="<c:url value='/qrcode/${facility.facilityId}'/>" alt="QR 코드">
            <a href="<c:url value='/qrcode/${facility.facilityId}/download'/>" class="btn-download">⬇️</a>
        </div>
        <div class="checklist-buttons">
            <c:forEach var="t" items="${templates}">
                <a href="<c:url value='/checklist/${t.templateId}'/>" class="btn-template">${t.name}</a>
            </c:forEach>
        </div>
    </div>

    <!-- 기간 선택 -->
    <form method="get" action="<c:url value='/facilities/${facility.facilityId}'/>" class="date-range">
        <input type="date" name="start" value="${start}" onchange="this.form.submit()">
        ~
        <input type="date" name="end"   value="${end}"   onchange="this.form.submit()">
    </form>

    <!-- 점검 내역 테이블 -->
    <div class="inspection-table">
        <table>
            <thead>
            <tr>
                <th>유형</th>
                <th>점검일</th>
                <th>점검자 아이디</th>
                <th>점검 상태</th>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${not empty inspections}">
                    <c:forEach var="in" items="${inspections}">
                            <tr>
                                <td>${facility.domain}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty in.submittedAt}">
                                            <fmt:formatDate value="${in.submittedAt}" pattern="yyyy.MM.dd"/>
                                        </c:when>
                                        <c:otherwise>—</c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${in.inspectorId}</td>
                                <td>
                                    <c:set var="statusText" value="${empty in.result ? '미표기' : in.result}"/>
                                    <c:set var="statusClass"
                                           value="${statusText eq 'PASS' ? 'ok' :
                                                    statusText eq 'FAIL' ? 'danger' : 'warn'}"/>
                                    <span class="status-pill">
                                        <span class="dot ${statusClass}"></span>
                                        <span>${statusText}</span>
                                    </span>
                                </td>
                            </tr>
                    </c:forEach>
                </c:when>
            </c:choose>
            </tbody>
        </table>
    </div>

</div>
</body>
</html>
