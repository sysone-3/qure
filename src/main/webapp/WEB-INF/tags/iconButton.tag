<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ tag language="java" body-content="empty" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="icon" required="true" %>      <%-- 아이콘 경로 --%>
<%@ attribute name="action" required="false" %>   <%-- 이동 주소 --%>
<%@ attribute name="id" required="false" %>
<%@ attribute name="cssClass" required="false" %> <%-- 추가 클래스 --%>
<%@ attribute name="bgColor" required="false" %>

<button type="button"
        class="icon-btn ${cssClass}"
        <c:if test="${not empty id}">id="${id}"</c:if>
        onclick="<c:if test='${not empty action}'>location.href='<c:url value='${action}'/>'</c:if>"
        style="
        <c:if test='${not empty bgColor}'>background-color:${bgColor};</c:if>
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 6px;
            border-radius: 50px;
            border: none;
            padding: 10px 16px 10px 13px;
        ">

    <c:if test="${not empty icon}">
        <img src="<c:url value='${icon}'/>" alt="" class="btn-icon" style="width: 21px; height: 21px;"/>
    </c:if>

    <span class="btn-label" style="
        font-family: 'Pretendard', sans-serif;
        font-size: 16px;
        font-weight: 500;
        color: #404040;
    ">${label}</span>
</button>
