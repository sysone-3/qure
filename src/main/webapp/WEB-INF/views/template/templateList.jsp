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
    <link rel="stylesheet" href="<c:url value='/assets/css/template.css'/>"/>
</head>
<body>
<div class="container">
    <div class="inner-container">
        <p class="title">점검표 목록</p>
        <div class="header">
            <div class="filter">
                <span id="selected-domain">전체</span>
                <img src="<c:url value='/assets/images/arrow-down.svg'/>" alt="arrow" class="icon"/>

                <!-- 드롭다운 메뉴 -->
                <ul class="dropdown hidden" id="domain-dropdown">
                    <li data-value="전체">전체</li>
                    <li data-value="미화">미화</li>
                    <li data-value="소방">소방</li>
                    <li data-value="순찰">순찰</li>
                </ul>
            </div>
            <div class="actions">
                <div class="search-box">
                    <img src="<c:url value='/assets/images/search.svg'/>" alt="search" class="icon"/>
                    <input type="text" placeholder="점검표 검색"/>
                </div>
                <my:iconButton label="추가하기"
                               icon="/assets/images/plus.svg"
                               action="/addForm.do"
                               bgColor="#FEE39A"/>
            </div>
        </div>
        <div class="card-grid">
            <c:forEach var="temp" items="${templateList}">
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
                                temp.cycleUnit eq 'MONTH' ? '월' :
                                temp.cycleUnit eq 'WEEK' ? '주' : '일'}에 한 번
                            </p>
                        </div>
                    </div>
                    <p class="card-date">
                        생성일: ${fn:substringBefore(temp.createdAt, 'T')}
                    </p>
                </div>
            </c:forEach>
        </div>
    </div>
</div>
<script>
    document.addEventListener("DOMContentLoaded", () => {
        const filter = document.querySelector(".filter");
        const dropdown = document.getElementById("domain-dropdown");
        const selected = document.getElementById("selected-domain");

        filter.addEventListener("click", () => {
            dropdown.classList.toggle("hidden");
        });

        dropdown.querySelectorAll("li").forEach(li => {
            li.addEventListener("click", (e) => {
                e.stopPropagation();
                const value = li.getAttribute("data-value");
                selected.textContent = value;
                dropdown.classList.add("hidden");

                // TODO: 도메인별 검색 API 호출
                console.log("선택된 도메인:", value);
            });
        });

        document.addEventListener("click", (e) => {
            if (!filter.contains(e.target)) {
                dropdown.classList.add("hidden");
            }
        });
    });
</script>
</body>
</html>
