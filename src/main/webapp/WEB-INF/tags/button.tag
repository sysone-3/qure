<%@ tag language="java" body-content="empty" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="action"   required="false" rtexprvalue="true" %>
<%@ attribute name="cssClass" required="false" %>
<%@ attribute name="bgColor" required="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:choose>
    <c:when test="${not empty action}">
        <c:url var="resolvedAction" value="${action}"/>
        <input type="button"
               value="${label}"
               data-action="${resolvedAction}"
               class="btn ${cssClass}"
               style="
                       width: 120px;
                       height:40px;
                       border: none;
                       border-radius: 50px;
                       padding: 0 24px;
                       font-size: 16px;
                       font-family: 'Pretendard', sans-serif;
                       font-weight: 500;
                       background-color:${bgColor};
                       " />
    </c:when>
    <c:otherwise>
        <input type="button"
               value="${label}"
               class="btn ${cssClass}"
               style="
                       width: 120px;
                       height:40px;
                       border: none;
                       border-radius: 50px;
                       padding: 0 24px;
                       font-size: 16px;
                       font-family: 'Pretendard', sans-serif;
                       font-weight: 500;
                       background-color:${bgColor};
                       " />
    </c:otherwise>
</c:choose>
