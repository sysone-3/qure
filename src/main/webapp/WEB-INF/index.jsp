<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>설비 목록</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="<c:url value='/resources/css/facilities.css'/>" rel="stylesheet">
</head>
<body>
<div class="page">

    <!-- 검색/필터 -->
    <div class="card">
        <form class="toolbar" method="get" action="<c:url value='/facilities'/>">
            <div class="search">
                <input type="text" name="q" placeholder="검색: 시설명/도메인" value="${param.q}">
                <!-- 돋보기 -->
                <svg class="glass" viewBox="0 0 24 24" fill="none">
                    <path d="M21 21l-4.3-4.3M10.5 18a7.5 7.5 0 1 1 0-15 7.5 7.5 0 0 1 0 15Z" stroke="currentColor" stroke-width="1.6" stroke-linecap="round"/>
                </svg>
            </div>
            <button type="submit" class="btn">검색</button>
            <a class="btn btn-ghost" href="<c:url value='/facilities'/>">초기화</a>
        </form>

        <!-- 노란 헤더 바 -->
        <div class="banner">설비 목록</div>

        <!-- 상단 우측 설비 추가 -->
        <div class="top-actions">
            <a class="btn btn-brand" href="<c:url value='/facilities/new'/>">설비 추가</a>
        </div>

        <!-- 리스트 -->
        <div class="list">
            <c:choose>
                <c:when test="${not empty facilities}">
                    <c:forEach var="f" items="${facilities}">
                        <div class="row">
                            <div class="cell col-id">
                                <a href="<c:url value='/facilities/${f.facilityId}'/>">${f.facilityId}</a>
                            </div>
                            <div class="cell col-name">${f.name}</div>
                            <div class="cell col-domain">${f.domain}</div>
                            <div class="cell col-updated">
                                <c:choose>
                                    <c:when test="${not empty f.updatedAt}">
                                        <fmt:formatDate value="${f.updatedAt}" pattern="yyyy.MM.dd"/>
                                    </c:when>
                                    <c:otherwise>—</c:otherwise>
                                </c:choose>
                            </div>
                            <div class="cell col-status">
                                <c:set var="statusText" value="${empty f.status ? '미표기' : f.status}"/>
                                <c:set var="statusClass"
                                       value="${statusText eq '정상' ? 'ok' :
                               statusText eq '미흡' ? 'warn' :
                               statusText eq '긴급' ? 'danger' : 'warn'}"/>
                                <span class="status-pill">
                  <span class="dot ${statusClass}"></span>
                  <span>${statusText}</span>
                </span>
                            </div>
                            <div class="cell actions">
                                <form method="post" action="<c:url value='/facilities/${f.facilityId}/delete'/>"
                                      onsubmit="return confirm('삭제하시겠습니까? (ID: ${f.facilityId})');">
                                    <button type="submit" class="icon-btn" title="삭제">
                                        <!-- 휴지통 아이콘 -->
                                        <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                                            <path d="M4 7h16M9 7V5a2 2 0 0 1 2-2h2a2 2 0 0 1 2 2v2M6 7l1 13a2 2 0 0 0 2 2h6a2 2 0 0 0 2-2l1-13"
                                                  stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round"/>
                                        </svg>
                                    </button>
                                </form>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="empty">등록된 설비가 없습니다.</div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <c:if test="${not empty msg}">
        <p style="margin:16px;color:#6b7280">${msg}</p>
    </c:if>
</div>
</body>
</html>
