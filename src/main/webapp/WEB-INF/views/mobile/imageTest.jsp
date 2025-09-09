<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>이미지 테스트</title>
  <style>
    body{font-family:sans-serif;margin:20px}
    .grid{display:grid;grid-template-columns:repeat(auto-fill,minmax(220px,1fr));gap:16px}
    .card{border:1px solid #ddd;border-radius:8px;padding:8px}
    .meta{font-size:12px;color:#666;margin:6px 0}
    img{width:100%;height:auto;border-radius:6px;display:block;background:#f7f7f7}
  </style>
</head>
<body>
  <h1>이미지 테스트</h1>
  <form method="get" action="<c:url value='/mobile/test/images'/>">
    <label>개수:</label>
    <input type="number" name="limit" value="${limit}" min="1" max="200"/>
    <button type="submit">조회</button>
  </form>

  <div class="grid">
    <c:forEach var="im" items="${images}">
      <div class="card">
        <div class="meta">
          ID: ${im.imageId}<br/>
          MIME: ${im.mimeType}<br/>
          ResultId: ${im.resultId}
        </div>
        <img src="<c:url value='/mobile/file/${im.imageId}'/>" alt="image ${im.imageId}"/>
      </div>
    </c:forEach>
  </div>
</body>
</html>
