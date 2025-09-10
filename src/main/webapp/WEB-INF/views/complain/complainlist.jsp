<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<!doctype html>
<html lang="ko">
<head>
<meta charset="utf-8">
<title>민원 관리</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="<c:url value='/assets/css/complainlist.css'/>" />
<script defer src="<c:url value='/assets/js/complainlist.js'/>"></script>

</head>
<body>
<div class="layout">
  <aside class="sidebar">
    <div class="logo"><div></div><div></div><div></div><div></div></div>
    <div class="nav-ico"></div><div class="nav-ico"></div><div class="nav-ico"></div>
    <div class="nav-ico"></div><div class="nav-ico"></div>
    <div class="spacer"></div>
    <div class="nav-ico"></div>
  </aside>

  <main class="content">
    <section class="left">
      <form class="filters" method="get" action="<c:url value='/admin/complain'/>">
        <div class="filters-row top">
          <div class="searchbox">
            <input type="text" name="q" value="${fn:escapeXml(param.q)}" placeholder="제목 검색">
          </div>
          <button class="btn search" type="submit">검색</button>
        </div>
        <div class="filters-row bottom">
          <div class="fld">
            <label>처리상태</label>
            <select name="status">
			  <option value=""      ${empty param.status ? 'selected' : ''}>전체</option>
			  <option value="PENDING"      <c:if test="${param.status=='PENDING'}">selected</c:if>>미처리</option>
			  <option value="IN_PROGRESS"  <c:if test="${param.status=='IN_PROGRESS'}">selected</c:if>>처리중</option>
			  <option value="RESOLVED"     <c:if test="${param.status=='RESOLVED'}">selected</c:if>>완료</option>
			</select>
          </div>
          <div class="fld">
            <label>기간</label>
            <input type="date" name="from" value="${param.from}"><span>~</span>
            <input type="date" name="to" value="${param.to}">
          </div>
        </div>
      </form>

      <div class="table-wrap">
        <table>
          <thead>
            <tr>
              <th style="width:64px">No</th>
              <th style="width:140px">접수일자</th>
              <th style="width:180px">시설명</th>
              <th>제목</th>
              <th style="width:100px">처리상태</th>
              <th style="width:84px">삭제</th>
            </tr>
          </thead>
          <tbody>
          <c:choose>
            <c:when test="${empty complainlist}">
              <tr><td class="no-data" colspan="6">데이터가 없습니다.</td></tr>
            </c:when>
            <c:otherwise>
              <c:forEach var="r" items="${complainlist}" varStatus="st">
                <tr data-id="${r.citizenReportId}" class="row">
                  <td>${st.index+1}</td>
                  <td><c:out value="${fn:substring(r.createdAt, 0, 10)}"/></td>
                  <td>${fn:escapeXml(r.name)}</td>
                  <td>${fn:escapeXml(r.category)}</td>
                  <td>
				  <c:choose>
				    <c:when test="${r.status=='PENDING'}">미처리</c:when>
				    <c:when test="${r.status=='IN_PROGRESS'}">처리중</c:when>
				    <c:when test="${r.status=='RESOLVED'}">완료</c:when>
				    <c:otherwise>${fn:escapeXml(r.status)}</c:otherwise>
				  </c:choose>
				</td>
                  <td>
                    <form method="post" action="<c:url value='/admin/complain/delete'/>" onsubmit="return false;">
                      <input type="hidden" name="id" value="${r.citizenReportId}">
                      <button type="button" class="btn del btn-del">삭제</button>
                    </form>
                  </td>
                </tr>
              </c:forEach>
            </c:otherwise>
          </c:choose>
          </tbody>
        </table>
      </div>
    </section>

    <section class="right" id="detailPanel">
      <div class="no-data">민원 신고를 선택해 주세요.</div>
    </section>
  </main>
</div>
</body>
</html>
