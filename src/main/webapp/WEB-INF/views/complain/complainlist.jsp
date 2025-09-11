<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<!doctype html>
<html lang="ko">
<head>
<meta charset="utf-8">
<title>민원 관리</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="_csrf"        content="${_csrf.token}">
<meta name="_csrf_header" content="${_csrf.headerName}">
<link rel="stylesheet" href="<c:url value='/assets/css/complainlist.css'/>" />
<script defer src="<c:url value='/assets/js/complainlist.js'/>"></script>

<!-- Pager-only styles (외부 CSS는 그대로 두고 이 블록만 추가) -->
<style>
  .pager{
  display:flex;
  gap:6px;
  align-items:center;
  margin:12px 0 0
  }
  .pager a,.pager span{
    min-width:34px;
    height:32px;
    padding:0 12px;
    border:1px solid var(--line);
    border-radius:8px;
    background:#fff;
    color:var(--text);
    text-decoration:none;
    display:inline-flex;
    align-items:center;
    justify-content:center;
    font-weight:600
  }
  .pager a:hover{
  	background:#fffdfa
  	}
  .pager .active{
  	background:var(--accent);
  	border-color:var(--accent);
  	color:var(--accent-text)}
  .pager .disabled{
  	opacity:.45;
  	pointer-events:none;
  	border-style:dashed
  	}
  .pager .gap{
  	border:none;
  	background:transparent;
  	pointer-events:none
  	}
</style>
</head>
<body>
<div class="layout">
	
  <c:set var="uri" value="${pageContext.request.requestURI}"/>
  <aside class="sidebar">
  <!-- 로고 -->
  <a href="#" class="nav-btn" aria-label="??">
    <img src="<c:url value='/assets/images/LOGO.svg'/>" alt="" class="nav-img">
  </a>
  <a href="#" class="nav-btn" aria-label="??">
    <img src="<c:url value='/assets/images/ICON_1.svg'/>" alt="" class="nav-img">
  </a>
  <a href="#" class="nav-btn" aria-label="??">
    <img src="<c:url value='/assets/images/ICON_2.svg'/>" alt="" class="nav-img">
  </a>
  <a href="#" class="nav-btn" aria-label="??">
    <img src="<c:url value='/assets/images/ICON_3.svg'/>" alt="" class="nav-img">
  </a>
  <a href="#" 
	   class="nav-btn ${fn:contains(uri,'/admin/complain') ? 'active' : ''}"
	   aria-label="민원관리"
	   aria-current="${fn:contains(uri,'/admin/complain') ? 'page' : ''}">
	  <img src="<c:url value='/assets/images/ICON_4.svg'/>" alt="민원관리" class="nav-img">
  </a>
  <a href="#" class="nav-btn" aria-label="지도">
    <img src="<c:url value='/assets/images/ICON_5.svg'/>" alt="" class="nav-img">
  </a>
  

  <div class="spacer"></div>
  <a href="#" class="nav-btn" aria-label="로그아웃">
    <img src="<c:url value='/assets/images/ICON_6.svg'/>" alt="" class="nav-img">
    </a>
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
              <option value="" ${empty param.status ? 'selected' : ''}>전체</option>
              <option value="PENDING"     <c:if test="${param.status=='PENDING'}">selected</c:if>>미처리</option>
              <option value="IN_PROGRESS" <c:if test="${param.status=='IN_PROGRESS'}">selected</c:if>>처리중</option>
              <option value="RESOLVED"    <c:if test="${param.status=='RESOLVED'}">selected</c:if>>완료</option>
            </select>
          </div>
          <div class="fld">
            <label>기간</label>
            <input type="date" name="from" value="${param.from}"><span>~</span>
            <input type="date" name="to" value="${param.to}">
          </div>
          <div class="fld">
            <label>페이지당</label>
            <select name="size" onchange="this.form.submit()">
              <option value="10"  <c:if test="${size==10}">selected</c:if>>10</option>
              <option value="20"  <c:if test="${size==20}">selected</c:if>>20</option>
              <option value="50"  <c:if test="${size==50}">selected</c:if>>50</option>
            </select>
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
                  <td>${(page-1)*size + st.index + 1}</td>
                  <td><c:out value="${fn:substring(r.createdAt, 0, 10)}"/></td>
                  <td>${fn:escapeXml(r.name)}</td>
                  <td>${fn:escapeXml(r.category)}</td>
                  <td>
                    <c:choose>
                      <c:when test="${r.status=='PENDING'}"><span class="st pending">미처리</span></c:when>
                      <c:when test="${r.status=='IN_PROGRESS'}"><span class="st progress">처리중</span></c:when>
                      <c:when test="${r.status=='RESOLVED'}"><span class="st resolved">완료</span></c:when>
                      <c:otherwise><span class="st">${fn:escapeXml(r.status)}</span></c:otherwise>
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

      <!-- 페이지 네비게이션 -->
      <c:if test="${pages > 1}">
        <c:set var="start" value="${page-2 < 1 ? 1 : page-2}"/>
        <c:set var="end"   value="${start+4}"/>
        <c:if test="${end > pages}">
          <c:set var="end" value="${pages}"/>
          <c:set var="start" value="${end-4 < 1 ? 1 : end-4}"/>
        </c:if>

        <nav class="pager">
          <!-- 이전 -->
          <c:choose>
            <c:when test="${page > 1}">
              <c:url var="prevUrl" value="/admin/complain">
                <c:param name="page" value="${page-1}"/>
                <c:param name="size" value="${size}"/>
                <c:param name="q" value="${param.q}"/>
                <c:param name="status" value="${param.status}"/>
                <c:param name="from" value="${param.from}"/>
                <c:param name="to" value="${param.to}"/>
              </c:url>
              <a href="${prevUrl}">이전</a>
            </c:when>
            <c:otherwise><span class="disabled">이전</span></c:otherwise>
          </c:choose>

          <!-- 처음 구간 생략 표시 -->
          <c:if test="${start > 1}">
            <c:url var="firstUrl" value="/admin/complain">
              <c:param name="page" value="1"/>
              <c:param name="size" value="${size}"/>
              <c:param name="q" value="${param.q}"/>
              <c:param name="status" value="${param.status}"/>
              <c:param name="from" value="${param.from}"/>
              <c:param name="to" value="${param.to}"/>
            </c:url>
            <a href="${firstUrl}">1</a><span class="gap">…</span>
          </c:if>

          <!-- 페이지 숫자 -->
          <c:forEach var="p" begin="${start}" end="${end}">
            <c:url var="pUrl" value="/admin/complain">
              <c:param name="page" value="${p}"/>
              <c:param name="size" value="${size}"/>
              <c:param name="q" value="${param.q}"/>
              <c:param name="status" value="${param.status}"/>
              <c:param name="from" value="${param.from}"/>
              <c:param name="to" value="${param.to}"/>
            </c:url>
            <a href="${pUrl}" class="${p==page?'active':''}">${p}</a>
          </c:forEach>

          <!-- 끝 구간 생략 표시 -->
          <c:if test="${end < pages}">
            <span class="gap">…</span>
            <c:url var="lastUrl" value="/admin/complain">
              <c:param name="page" value="${pages}"/>
              <c:param name="size" value="${size}"/>
              <c:param name="q" value="${param.q}"/>
              <c:param name="status" value="${param.status}"/>
              <c:param name="from" value="${param.from}"/>
              <c:param name="to" value="${param.to}"/>
            </c:url>
            <a href="${lastUrl}">${pages}</a>
          </c:if>

          <!-- 다음 -->
          <c:choose>
            <c:when test="${page < pages}">
              <c:url var="nextUrl" value="/admin/complain">
                <c:param name="page" value="${page+1}"/>
                <c:param name="size" value="${size}"/>
                <c:param name="q" value="${param.q}"/>
                <c:param name="status" value="${param.status}"/>
                <c:param name="from" value="${param.from}"/>
                <c:param name="to" value="${param.to}"/>
              </c:url>
              <a href="${nextUrl}">다음</a>
            </c:when>
            <c:otherwise><span class="disabled">다음</span></c:otherwise>
          </c:choose>
        </nav>
      </c:if>
    </section>

    <!-- 상세 패널: JS가 data-detail-url을 읽어 사용 -->
    <section class="right" id="detailPanel"
             data-detail-url="${pageContext.request.contextPath}/admin/complain/detail">
      <div class="no-data">민원 신고를 선택해 주세요.</div>
    </section>
  </main>
</div>
</body>
</html>
