<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>

<c:choose>
  <c:when test="${empty selected}">
    <div class="no-data">조회할 수 없습니다.</div>
  </c:when>
  <c:otherwise>
    <h2 class="detail-title"><c:out value="${selected.category}" default="제목 없음"/></h2>

    <div class="meta">
      <div><b>처리 여부 :</b>
		  <c:choose>
		    <c:when test="${selected.status=='PENDING'}">미처리</c:when>
		    <c:when test="${selected.status=='IN_PROGRESS'}">처리중</c:when>
		    <c:when test="${selected.status=='RESOLVED'}">완료</c:when>
		    <c:otherwise><c:out value="${selected.status}"/></c:otherwise>
		  </c:choose>
		</div>

      <div><b>시설명 :</b> <c:out value="${selected.name}" default=""/></div>
      <div><b>주소 :</b> <c:out value="${selected.address}" default=""/></div>
      <div><b>접수날짜 :</b>
        <c:if test="${selected.createdAt != null}">
          <c:out value="${fn:substring(selected.createdAt, 0, 10)}"/>
        </c:if>
      </div>
      <div style="grid-column:1 / span 2"><b>이메일 주소 :</b> <c:out value="${selected.email}" default=""/></div>
    </div>

    <div class="hr"></div>

    <div class="desc" id="detailDesc">
      <c:out value="${selected.description}" default=""/>
    </div>

    <form class="detail-foot" action="<c:url value='/admin/complain/status'/>" method="post">
	  <input type="hidden" name="id" value="${selected.citizenReportId}">
	  <select class="status-select" name="status">
	    <option value="IN_PROGRESS" <c:if test="${selected.status=='IN_PROGRESS'}">selected</c:if>>처리중</option>
	    <option value="RESOLVED"    <c:if test="${selected.status=='RESOLVED'}">selected</c:if>>완료</option>
	  </select>
	  <button class="btn ok" type="submit">확인</button>
	</form>

  </c:otherwise>
</c:choose>
