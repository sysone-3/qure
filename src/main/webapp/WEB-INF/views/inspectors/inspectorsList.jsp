<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>점검자 리스트</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="<c:url value='/assets/css/inspectorsList.css'/>" />
</head>
<body>
    <div class="innerContainer">
        <div class="topContainer">
            <span class="title">작업자</span>
            <div class="statusContainer">
                <span class="subTitle">현황</span>
                <div class="buttonContainer">
                    <button class="register">
                         <img src="<c:url value='/assets/images/plus.svg'/>" alt="+" class="icon" />
                         작업자 등록
                     </button>
                    <button class="delete">
                        <img src="<c:url value='/assets/images/pencil.svg'/>" alt="+" class="icon" />
                        작업자 삭제
                    </button>
                </div>
            </div>
        </div>

        <div class="boardContainer">
            <span class="totalTitle">전체</span>
            <span class="highlight">0명</span>
            <table class="inspectorTable">
                 <thead>
                    <tr>
                        <th>아이디</th>
                        <th>이름</th>
                        <th>연락처</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="inspector" items="${inspectors}">
                        <tr>
                            <td>{inspector.id}</td>
                            <td>{inspector.name}</td>
                            <td>{inspector.phone}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <div class="pagination">
                 <button class="circle-btn prev">
                    <img src="<c:url value='/assets/images/left.svg'/>" alt="left" class="icon" />
                 </button>
                 <button class="circle-btn active" id="pageNumber">1</button>
                 <button class="circle-btn next">
                     <img src="<c:url value='/assets/images/right.svg'/>" alt="right" class="icon" />
                 </button>
            </div>
        </div>
    </div>
    <script src="<c:url value='/assets/js/inspectorsList.js'/>"></script>
</body>
</html>
