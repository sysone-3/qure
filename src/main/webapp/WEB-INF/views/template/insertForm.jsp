<!-- 팝업 관련 작성자: 김민서 -->

<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>점검표 등록</title>
    <link rel="stylesheet" href="<c:url value='/assets/css/reset.css'/>"/>
    <c:if test="${!popup}">
        <link rel="stylesheet" href="<c:url value='/assets/css/navigation.css'/>" />
        <link rel="stylesheet" href="<c:url value='/assets/css/components.css'/>" />
    </c:if>
    <link rel="stylesheet" href="<c:url value='/assets/css/templateForm.css?after'/>"/>
</head>
<body>
<c:set var="nav" value="template" scope="request"/>

<c:choose>
    <c:when test="${!popup}">
        <div class="layout">
            <%@ include file="/WEB-INF/views/fragments/sidebar.jspf" %>
            <main class="content">
                <jsp:include page="/WEB-INF/views/template/_insertBody.jsp"/>
            </main>
        </div>
    </c:when>

    <c:otherwise>
        <main class="content" style="padding:20px; max-width:840px; margin:0 auto;">
            <jsp:include page="/WEB-INF/views/template/_insertBody.jsp"/>
        </main>
    </c:otherwise>
</c:choose>

<script src="<c:url value='/assets/js/template.js?ver=20250910'/>"></script>
</body>

</html>


