<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>점검자 리스트</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="<c:url value='/assets/css/inspectorsDetail.css'/>" />
</head>
<body>
    <div class="innerContainer">
        <div class="topContainer">
            <span class="title">작업자</span>
            <div class="statusContainer">
                <span class="subTitle">상세보기</span>
            </div>
        </div>
     <div class="boardContainer">
         <div class="infoContainer">
             <h3 class="sectionTitle">개인 정보</h3>
             <div class="infoRow">
                 <span class="infoLabel">이름</span>
                 <span class="infoValue">${inspector.name}</span>
             </div>
             <div class="infoRow">
                 <span class="infoLabel">연락처</span>
                 <span class="infoValue">${inspector.phone}</span>
             </div>
             <div class="infoRow">
                 <span class="infoLabel">담당 구역</span>
                 <div class="infoList">
                     <c:forEach var="f" items="${facilities}">
                         <span class="infoTag">${f.domain}</span>
                     </c:forEach>
                 </div>
             </div>
             <div class="infoRow">
                 <span class="infoLabel">담당 시설</span>
                 <div class="infoList">
                     <c:forEach var="f" items="${facilities}">
                         <span class="infoTag">${f.facility}</span>
                     </c:forEach>
                 </div>
             </div>
         </div>
     </div>
</div>
</body>
</html>
