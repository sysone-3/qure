<%@ tag language="java" body-content="empty" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="action" required="true" %>
<%@ attribute name="cssClass" required="false" %>
<%@ attribute name="bgColor" required="false" %>

<input type="button"
       value="${label}"
       onclick="location.href='${action}'"
       style="
               width: 120px;
               border: none;
               border-radius: 50px;
               padding: 10px 24px;
               font-size: 16px;
               font-family: 'Pretendard', sans-serif;
               font-weight: 500;
               background-color:${bgColor};
               "
       class="btn ${cssClass}" />