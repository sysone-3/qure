<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <title>템플릿 생성</title>
</head>
<body>
<h1>템플릿 생성</h1>
<form action="save" method="post">
    <label>템플릿 이름: </label>
    <input type="text" name="name"/><br/><br/>

    <label>설명: </label><br/>
    <textarea name="desc" rows="4" cols="40"></textarea><br/><br/>

    <button type="submit">저장</button>
</form>

<a href="list.jsp">← 목록으로</a>
</body>
</html>
