<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %> <!-- [추가] fn 사용시 필요 -->
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
        <!-- 상단 콘텐츠 -->
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

        <!-- 하단 버튼 그룹 (항상 카드 하단) -->
        <div class="card-actions">
          <a href="${complainUrl}" id="btnComplain" class="btn btn-complain">민원 신고</a>
          <a href="${inspectUrl}" id="btnInspect"  class="btn btn-inspect">시설 점검</a>
        </div>
      </section>
  </main>

  <!-- [추가] 서버에서 전달한 플래시 메시지 주입(일원화: window.FLASH) -->
  <script>
    window.FLASH = {
      success: "<c:out value='${successMsg}'/>",
      error: "<c:out value='${errorMsg}'/>"
    };
  </script>

  <!-- [수정] 캐시 무력화 위해 버전 쿼리 추가 -->
  <script defer src="<c:url value='/assets/js/mobileMain.js?v=2'/>"></script>
</body>
</html>
