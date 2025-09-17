<%-- 작성자: 최온유 --%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1, viewport-fit=cover" />
  <title>민원 제출</title>
  <link rel="stylesheet" href="<c:url value='/assets/css/complain.css'/>">
  <script defer src="<c:url value='/assets/js/complain.js'/>"></script>
</head>
<body>
<div class="wrap">
  <div class="topbar">
    <button type="button" class="btn-back" onclick="history.back()">이전</button>
  </div>

  <div class="card">
    <header class="head">
      <h1><c:out value="${tagInfo.floor}층 ${tagInfo.zone} ${tagInfo.name}"/></h1>
      <p class="addr"><c:out value="${tagInfo.address}"/></p>
    </header>

    <form id="frm" action="<c:url value='/mobile/${tagId}/complain'/>" method="post" novalidate>
      <input type="hidden" name="facilityId" value="<c:out value='${tagInfo.facilityId}'/>"/>

      <div class="field">
        <label class="label" for="title">제목 <span class="req">*</span></label>
        <input id="title" name="category" type="text" class="inp" maxlength="50"
               placeholder="예: 스프링쿨러가 떨어졌어요" autocomplete="off" />
        <div class="hint">최대 50자</div>
        <div class="err" id="errTitle"></div>
      </div>

      <div class="field">
        <label class="label" for="email">이메일(선택)</label>
        <input id="email" name="email" type="email" class="inp" maxlength="254"
               inputmode="email" autocomplete="email"
               placeholder="답변받을 이메일 주소" />
        <div class="hint">회신이 필요할 때만 입력</div>
        <div class="err" id="errEmail"></div>
      </div>

      <div class="divider"></div>

      <div class="field">
        <label class="label" for="desc">상세내용 <span class="req">*</span></label>
        <textarea id="desc" name="description" class="inp" maxlength="4000"
                  placeholder="상세 위치, 현상, 위험 여부를 입력하세요."></textarea>
        <div class="err" id="errDesc"></div>
      </div>

      <div class="submitbar">
        <button id="btnSubmit" class="btn-submit" type="submit" disabled>제출</button>
      </div>
    </form>
  </div>
</div>
</body>
</html>
