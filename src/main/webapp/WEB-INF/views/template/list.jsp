<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
</head>
<body>
<div style="width: 900px; margin-left:auto; margin-right: auto;">
    <h1>템플릿 목록</h1>
    <table border="1" style="width:100%">
        <thead>
        <tr>
            <th>TEMPLATE_ID</th>
            <th>DOMAIN</th>
            <th>NAME</th>
            <th>VERSION</th>
            <th>IS_ACTIVE</th>
            <th>CREATED_AT</th>
            <th>FACILITY_ID</th>
            <th>CYCLE</th>
            <th>CYCLE_UNIT</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="temp" items="${templateList }">
            <tr>
                <td>${temp.templateId }</td>
                <td>${temp.domain}</td>
                <td>${temp.name}</td>
                <td>${temp.version}</td>
                <td>${temp.isActive}</td>
                <td>${temp.createdAt}</td>
                <td>${temp.facilityId}</td>
                <td>${temp.cycle}</td>
                <td>${temp.cycleUnit}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>