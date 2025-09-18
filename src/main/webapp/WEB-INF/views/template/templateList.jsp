<%--작성자: 최이서--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>템플릿 목록</title>
    <link rel="stylesheet" href="<c:url value='/assets/css/reset.css'/>"/>
    <link rel="stylesheet" href="<c:url value='/assets/css/templateList.css?after'/>"/>

    <link rel="stylesheet" href="<c:url value='/assets/css/navigation.css'/>" />
    <link rel="stylesheet" href="<c:url value='/assets/css/components.css'/>" />
</head>
<body>
<c:set var="nav" value="template" scope="request"/>
<div class="layout">
<%@ include file="/WEB-INF/views/fragments/sidebar.jspf" %>
<main class="content">

<div class="container">
    <div class="inner-container">
        <p class="title">점검표 목록</p>
        <form id="filterForm" class="header" method="get" action="<c:url value='/template'/>">
            <div class="filter">
                <!-- 선택된 도메인 표시 + hidden 파라미터 -->
                <c:set var="domainLabel"
                       value="${param.domain=='CLEANING'?'미화':(param.domain=='FIRE'?'소방':(param.domain=='PATROL'?'순찰':'전체'))}" />
                <input type="hidden" name="domain" id="domainInput" value="${empty param.domain ? 'ALL' : param.domain}"/>

                <span id="selected-domain">${domainLabel}</span>
                <img src="<c:url value='/assets/images/arrow-down.svg'/>" alt="arrow" class="icon"/>

                <ul class="dropdown hidden" id="domain-dropdown">
                    <li data-value="ALL">전체</li>
                    <li data-value="CLEANING">미화</li>
                    <li data-value="FIRE">소방</li>
                    <li data-value="PATROL">순찰</li>
                </ul>
            </div>

            <div class="actions">
                <div class="keyword-row">
                    <div class="search-box">
                        <img src="<c:url value='/assets/images/search.svg'/>" alt="search" class="icon"/>
                        <!-- name=q 로 넘김 -->
                        <input type="text" name="q" value="${fn:escapeXml(param.q)}" placeholder="점검표 검색"/>
                    </div>
                    <button type="submit" class="btn-search">검색</button>
                </div>

                <my:iconButton label="추가하기"
                               icon="/assets/images/plus.svg"
                               action="/template/insert"
                               bgColor="#FEE39A"/>
            </div>
        </form>
        <div class="card-grid">
            <c:forEach var="temp" items="${templateList}">
                <a class="card-link"
                   href="<c:url value='/template/${temp.templateId}'/>"
                   aria-label="${fn:escapeXml(temp.name)} 수정 페이지로 이동">
                    <div class="card">
                        <div>
                            <div class="card-header">
                                <span class="badge">
                                    <img src="<c:url value='/assets/images/${fn:toLowerCase(temp.domain)}.svg'/>"
                                         alt="${temp.domain}" class="badge-icon"/>
                                    <c:choose>
                                        <c:when test="${temp.domain eq 'CLEANING'}">미화</c:when>
                                        <c:when test="${temp.domain eq 'PATROL'}">순찰</c:when>
                                        <c:when test="${temp.domain eq 'FIRE'}">소방</c:when>
                                    </c:choose>
                                </span>
                            </div>
                            <div class="card-body">
                                <h3 class="card-title">${temp.name}</h3>
                                <p class="card-cycle">
                                        ${temp.cycle}${temp.cycleUnit eq 'YEAR' ? '년' :
                                            temp.cycleUnit eq 'MONTH' ? '개월' :
                                                    temp.cycleUnit eq 'WEEK' ? '주' : '일'}에 한 번
                                </p>
                            </div>
                        </div>
                        <p class="card-date">
                            생성일: ${fn:substringBefore(temp.createdAt, 'T')}
                        </p>
                    </div>
                </a>
            </c:forEach>
        </div>
    </div>
</div>
</main>
</div>
<script>
    document.addEventListener("DOMContentLoaded", () => {
        const filter = document.querySelector(".filter");
        const dropdown = document.getElementById("domain-dropdown");
        const selected = document.getElementById("selected-domain");
        const form = document.getElementById("filterForm");
        const domainInput = document.getElementById("domainInput");

        const labelOf = (code) => (
            code === "CLEANING" ? "미화" :
                code === "FIRE"     ? "소방" :
                    code === "PATROL"   ? "순찰" : "전체"
        );

        filter.addEventListener("click", () => {
            dropdown.classList.toggle("hidden");
        });

        dropdown.querySelectorAll("li").forEach(li => {
            li.addEventListener("click", (e) => {
                e.stopPropagation();
                const code = li.getAttribute("data-value");   // ALL | CLEANING | FIRE | PATROL
                domainInput.value = code;
                selected.textContent = labelOf(code);
                dropdown.classList.add("hidden");
                form.submit(); 
            });
        });

        document.addEventListener("click", (e) => {
            if (!filter.contains(e.target)) dropdown.classList.add("hidden");
        });
    });
</script>
</body>
</html>
