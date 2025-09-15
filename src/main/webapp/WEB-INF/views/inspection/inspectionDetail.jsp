<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>점검 결과</title>
    <link rel="stylesheet" href="<c:url value='/assets/css/reset.css'/>"/>
    <link rel="stylesheet" href="<c:url value='/assets/css/navigation.css'/>" />
    <link rel="stylesheet" href="<c:url value='/assets/css/components.css'/>" />
    <link rel="stylesheet" href="<c:url value='/assets/css/inspectionDetail.css'/>"/>
</head>
<body>
<c:set var="nav" value="inspectors" scope="request"/>
<div class="layout">
<%@ include file="../fragments/sidebar.jspf" %>
<main class="content">
<div class="card">
    <h3 style="font-size:23px;font-weight:600">점검 결과</h3>

    <div style="margin-top: 50px; display: flex; flex-direction: column; gap: 40px">
        <div class="section">
            <div class="subtitle">점검 일시</div>
            <div class="desc">${submittedAtStr}</div>
        </div>

        <div class="section">
            <div class="subtitle">점검 설비</div>
            <div class="desc">${detail.facility.floor}층 ${detail.facility.zone} 구역 ${detail.facility.name}</div>
        </div>

        <div class="section">
            <div class="subtitle">점검자 정보</div>
            <div class="desc">${detail.inspector.name} · ${detail.inspector.phone}</div>
        </div>

        <div class="section">
            <div class="subtitle" style="margin-bottom:5px;">점검 내역</div>

            <div class="list">
                <c:forEach var="it" items="${detail.items}" varStatus="st">
                    <div class="row">
                        <span class="badge ${it.status eq '이상' ? 'badge-bad' : 'badge-ok'}">
                            ${it.status}
                        </span>
                        <div style="display: flex; flex-direction: column; gap: 10px; margin-top: 3px">
                            <div class="q">Q${st.count}. ${it.label}</div>
                            <div class="a">
                                <c:choose>
                                    <c:when test="${it.type eq 'IMAGE'}">
                                        <c:if test="${empty it.imagePaths}">
                                            <span class="muted">이미지 없음</span>
                                        </c:if>
                                        <c:forEach var="p" items="${it.imagePaths}">
                                            <a href="<c:url value='${p}'/>" target="_blank">
                                                <img class="thumb" src="<c:url value='${p}'/>" alt="첨부 이미지"/>
                                            </a>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        A${st.count}. ${it.answerText}
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</div>
</main>
</body>
</html>
