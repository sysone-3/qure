<%-- 작성자: 최온유 --%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1, viewport-fit=cover" />
  <title>점검 PIN 입력</title>

  <link rel="stylesheet" href="<c:url value='/assets/css/inspectorValidate.css'/>">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/variable/pretendardvariable.css">
  <script defer src="<c:url value='/assets/js/inspectorValidate.js'/>"></script>
</head>
<body>
  <div class="wrap">

    <div class="back-btn">
      <button class="btn-icon" onclick="history.back()" aria-label="뒤로"><b>이전</b></button>
    </div>

    <div class="topbar">
      <img class="logo" src="<c:url value='/assets/images/QURE_LOGO.png'/>" alt="QURE 로고">
    </div>

    <div class="sub-div">
      <p class="sub">점검자 본인 확인을 위해</p>
      <p class="sub">휴대폰 번호 <b>뒷 4자리</b>를 입력하세요.</p>
    </div>

    <form
      id="pinForm"
      action="<c:url value='/mobile/${tagId}/inspect'/>"
      method="post"
      autocomplete="one-time-code"
    >
      <div class="pin" id="pinBox">
        <!-- 키보드 방지: readonly + inputmode=none -->
        <input type="password" inputmode="none" maxlength="1" aria-label="첫째 자리" readonly />
        <input type="password" inputmode="none" maxlength="1" aria-label="둘째 자리" readonly />
        <input type="password" inputmode="none" maxlength="1" aria-label="셋째 자리" readonly />
        <input type="password" inputmode="none" maxlength="1" aria-label="넷째 자리" readonly />
      </div>

      <div
        id="err"
        class="err ${not empty errorMsg ? 'show' : ''}"
        role="alert"
        aria-live="polite"
      >
        <c:out value="${errorMsg}"/>
      </div>

      <div class="pad" aria-label="숫자 키패드">
        <button type="button" class="key">1</button>
        <button type="button" class="key">2</button>
        <button type="button" class="key">3</button>
        <button type="button" class="key">4</button>
        <button type="button" class="key">5</button>
        <button type="button" class="key">6</button>
        <button type="button" class="key">7</button>
        <button type="button" class="key">8</button>
        <button type="button" class="key">9</button>

        <button type="button" class="key key--ghost" id="clear">C</button>
        <button type="button" class="key">0</button>
        <button type="button" class="key key--ghost" id="back">←</button>

        <button id="submitBtn" class="submit" disabled>점검 시작</button>
      </div>

      <input type="hidden" name="pin" id="pinValue" />
    </form>
  </div>
</body>
</html>
