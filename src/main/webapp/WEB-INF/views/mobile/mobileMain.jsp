<%-- 작성자: 최온유 --%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1, viewport-fit=cover" />
  <title>Mobile Main Page</title>

  <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/variable/pretendardvariable.css">
  <link rel="stylesheet" href="<c:url value='/assets/css/mobileMain.css'/>">
</head>
<body>
  <main id="landingRoot" class="wrap">

    <spring:url var="inspectUrl" value="/mobile/{tagId}/inspect">
      <spring:param name="tagId" value="${tagInfo.tagId}" />
    </spring:url>

    <spring:url var="complainUrl" value="/mobile/{tagId}/complain">
      <spring:param name="tagId" value="${tagInfo.tagId}" />
    </spring:url>

    <section class="card">
      <div class="card-top">
        <div class="block text-center">
          <img class="logo" src="<c:url value='/assets/images/QURE_LOGO.png'/>" alt="QURE 로고">
        </div>
        <h1 class="block title text-center">
          <c:out value="${tagInfo.floor}"/>층&nbsp;
          <c:out value="${tagInfo.zone}"/>&nbsp;
          <c:out value="${tagInfo.name}"/>
        </h1>
        <p class="block subtitle text-center"><c:out value="${tagInfo.address}"/></p>
      </div>

      <div class="card-actions">
        <a href="${complainUrl}" id="btnComplain" class="btn btn-complain">민원 신고</a>
        <a href="${inspectUrl}" id="btnInspect"  class="btn btn-inspect">시설 점검</a>
      </div>
    </section>
  </main>

  <!-- 기존 점검 플래시 메시지 -->
  <script>
    window.FLASH = {
      success: "<c:out value='${successMsg}'/>",
      error: "<c:out value='${errorMsg}'/>"
    };
  </script>

  <!-- 민원 플래시(컨트롤러: complainStatus/complainMsg) → __flash로 주입 -->
  <c:if test="${not empty complainStatus}">
    <script>
      window.__flash = {
        type: '<c:out value="${complainStatus}"/>' === 'OK' ? 'success' : 'error',
        text: '<c:out value="${complainMsg}"/>'
      };
    </script>
  </c:if>

  <script defer src="<c:url value='/assets/js/mobileMain.js?v=2'/>"></script>
</body>
</html>
