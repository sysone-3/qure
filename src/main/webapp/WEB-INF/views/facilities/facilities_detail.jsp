<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>${facility.name} 상세</title>
    <link rel="stylesheet" href="<c:url value='/assets/css/facilities.css'/>" />
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
            <form method="post" action="<c:url value='/facilities/${facility.facilityId}/delete'/>"
                  onsubmit="return confirm('삭제하시겠습니까? 한 번 삭제 후 복구 불가능합니다. (ID: ${facility.facilityId})');">
                <button type="submit" class="icon-btn" title="삭제">삭제</button>
            </form>
            <a href="<c:url value='/facilities/${facility.facilityId}/edit'/>" class="icon-btn">수정</a>
        </div>
    </div>

    <!-- QR + 점검표 버튼 -->
    <div class="checklist-section qr-box-card">
        <div class="qr-box">
            <img src="<c:url value='/qr/${tag.tagId}.png'/>" alt="QR 코드">
            <a class="btn btn-circle" download="facility-${facility.facilityId}-qr.png"
               href="<c:url value='/qr/${tag.tagId}.png'/>" aria-label="QR 다운로드">다운로드</a>
        </div>
        <div class="checklist-buttons">
            <c:forEach var="t" items="${templates}">
                <a href="<c:url value='/checklist/${t.templateId}'/>" class="btn-template">${t.name}</a>
            </c:forEach>
        </div>
    </div>


    <!-- 기간 선택 -->
    <div class ="date-range-card">
        <form id="rangeForm" method="get" action="<c:url value='/facilities/${facility.facilityId}'/>" class="date-range">
            <input type="date" name="start" id="start" value="${start}">
            ~
            <input type="date" name="end"   id="end"   value="${end}">
        </form>
    </div>

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

<script>
    (function(){
        const f = document.getElementById('rangeForm');
        const s = document.getElementById('start');
        const e = document.getElementById('end');
        function maybeSubmit(){
            if(!s.value || !e.value) return;               // 둘 다 있을 때만
            if(e.value < s.value){                         // 역전 방지
                alert('끝 날짜가 시작 날짜보다 빠릅니다.');
                return;
            }
            f.submit();
        }
        s.addEventListener('change', maybeSubmit);
        e.addEventListener('change', maybeSubmit);
    })();
</script>
</body>
</html>
