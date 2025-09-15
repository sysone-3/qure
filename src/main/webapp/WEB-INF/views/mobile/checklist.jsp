<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"  uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, viewport-fit=cover" />
  <title>${template.name} · 점검 체크리스트</title>
  <link rel="stylesheet" href="<c:url value='/assets/css/checklist.css'/>" />
</head>
<body>
<div class="wrap">

  <!-- 상단 정보 -->
  <div class="head">
    <span class="pill">QR 템플릿 v${template.version}</span>
    <span class="pill">시설ID: ${tagInfo.facilityId}</span>
  </div>
  
  <h1>${tagInfo.floor}층 ${tagInfo.zone} ${tagInfo.name}</h1>
  <p class="sub">${tagInfo.address}</p>

  <div class="sec-title">점검 항목</div>

  <!-- form: multipart (사진 업로드 대비) -->
  <form id="chkForm"
        action="<c:url value='/mobile/${tagId}/checklist'/>"
        method="post"	
        enctype="multipart/form-data"
        novalidate>
        
        <input type="hidden" name="nonce" value="${requestScope.nonce}">

    <!-- 동적 항목 렌더링 -->
    <c:forEach var="it" items="${items}" varStatus="s">
      <div class="card"
           data-item-id="${it.itemId}"
           data-type="${it.type}"
           data-required="${it.required}"
           data-note="${fn:escapeXml(it.note)}"
           data-guide="${fn:escapeXml(it.guide)}">
        <h3>
          <span>${it.orderNo}. ${it.label}</span>
          <c:if test="${it.required}"><span class="req">＊필수</span></c:if>
        </h3>

        <div class="field">
          <!-- BOOLEAN -->
          <c:if test="${it.type == 'BOOL'}">
            <div class="row">
              <label class="radio">
                <input type="radio" name="results[${s.index}].value" value="Y">
                <span>적합</span>
              </label>
              <label class="radio">
                <input type="radio" name="results[${s.index}].value" value="N">
                <span>부적합</span>
              </label>
            </div>
          </c:if>

          <!-- NUMBER -->
          <c:if test="${it.type == 'NUM'}">
            <div class="num">
              <input type="number"
                     name="results[${s.index}].value"
                     placeholder="값 입력"
                     inputmode="decimal">
            </div>
          </c:if>

          <!-- TEXT -->
          <c:if test="${it.type == 'TEXT'}">
            <textarea name="results[${s.index}].value" placeholder="메모 / 특이사항 입력"></textarea>
          </c:if>

          <!-- PHOTO -->
          <c:if test="${it.type == 'IMAGE'}">
            <div class="photos">
              <input type="file" name="results[${s.index}].photos" accept="image/*" multiple>
              <div class="hint">현장 사진을 첨부하세요. (여러 장 가능)</div>
            </div>
          </c:if>

          <!-- 공통 hidden -->
          <input type="hidden" name="results[${s.index}].itemId" value="${it.itemId}" />

          <!-- 가이드/비고 -->
          <c:if test="${not empty it.guide}">
            <div class="guide">가이드: ${it.guide}</div>
          </c:if>
          <c:if test="${not empty it.note}">
            <div class="guide">비고: ${it.note}</div>
          </c:if>

          <div class="err" aria-live="polite"></div>
        </div>
      </div>
    </c:forEach>
    <!-- GPS 위치 정보 -->
    <input type="hidden" name="submitLat">
	<input type="hidden" name="submitLng">
	<input type="hidden" name="gpsAcc">
  </form>
</div>

<!-- 고정 제출 바 -->
<div class="dock">
  <div class="dock-inner">
    <div class="stat">
      완료 <span id="doneCnt">0</span>/<span id="totalCnt"><c:out value="${fn:length(items)}"/></span>
    </div>
    <button class="submit" id="btnSubmit" form="chkForm" disabled>점검 결과 제출</button>
  </div>
</div>

<script defer src="<c:url value='/assets/js/checklist.js'/>"></script>
</body>
</html>
