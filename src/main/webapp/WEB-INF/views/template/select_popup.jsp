<!-- 작성자: 김민서 -->

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8"/>
    <title>점검표 선택</title>
    <style>
        body { font-family: Arial, sans-serif; margin:20px; }
        .header { display:flex; justify-content:space-between; align-items:center; margin-bottom:16px; }
        .search { display:flex; gap:8px; }
        .list { display:flex; flex-direction:column; gap:10px; }
        .item {
            border:1px solid #F5D564; background:#FFFDF3; border-radius:999px;
            padding:12px 16px; display:flex; justify-content:space-between; align-items:center;
        }
        .left { display:flex; align-items:center; gap:10px; }
        .badge {
            font-size:12px; padding:2px 8px; border-radius:999px; background:#fff; border:1px solid #eee;
        }
        .name { font-weight:600; }
        .select-btn {
            border:1px solid #F5D564; background:#FEE39A; padding:6px 12px; border-radius:8px; cursor:pointer;
        }
        .select-btn:hover { background:#F5D564; }
        .name a { text-decoration:none; color:#333; }
        .name a:hover { text-decoration:underline; }
    </style>
</head>
<body>
<div class="header">
    <h3>점검표 선택</h3>
    <button type="button" class="select-btn" onclick="openCreate()">새로 만들기</button>
</div>

<div class="list">
    <c:forEach var="t" items="${templateList}">
        <c:url var="detailUrl" value="/template/${t.templateId}">
            <!-- 팝업 컨텍스트 유지 -->
            <c:param name="popup" value="true"/>
            <c:param name="returnUrl" value="${backUrl}"/>
        </c:url>

        <div class="item" role="listitem">
            <div class="left">
      <span class="badge">
        <c:choose>
            <c:when test="${t.domain eq 'CLEANING'}">미화</c:when>
            <c:when test="${t.domain eq 'FIRE'}">소방</c:when>
            <c:when test="${t.domain eq 'PATROL'}">순찰</c:when>
            <c:otherwise>${t.domain}</c:otherwise>
        </c:choose>
      </span>

                <!-- ⬇⬇ 여기: 이름을 링크로 -->
                <span class="name">
        <a href="${detailUrl}" class="btn-template">${fn:escapeXml(t.name)}</a>
      </span>
            </div>

            <button class="select-btn"
                    onclick="choose('${t.templateId}', '${fn:escapeXml(t.name)}', '${t.domain}')">
                선택
            </button>
        </div>
    </c:forEach>

    <c:if test="${empty templateList}">
        <p>등록된 점검표가 없습니다. “새로 만들기”를 눌러 추가하세요.</p>
    </c:if>
</div>

<script>
    function choose(id, name, domain) {
        if (window.opener && typeof window.opener.selectTemplateCallback === 'function') {
            window.opener.selectTemplateCallback(id, name, domain);
        }
        window.close();
    }

    function openCreate() {
        // JSP에서 컨텍스트 경로 주입
        var ctx = '${pageContext.request.contextPath}'; // "/qure"
        var path = window.location.pathname;            // "/qure/template/select"
        if (path.startsWith(ctx)) path = path.substring(ctx.length); // "/template/select"
        var q = window.location.search || '';           // "?popup=true"

        const returnUrl = encodeURIComponent(path + q); // "/template/select?popup=true"
        const url = "<c:url value='/template/insert'/>?popup=true&returnUrl=" + returnUrl;

        window.location.href = url;
    }
</script>
</body>
</html>
