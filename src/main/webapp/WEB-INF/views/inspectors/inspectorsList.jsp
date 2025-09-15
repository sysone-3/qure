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
    <link rel="stylesheet" href="<c:url value='/assets/css/navigation.css'/>" />
    <link rel="stylesheet" href="<c:url value='/assets/css/reset.css'/>"/>
    <link rel="stylesheet" href="<c:url value='/assets/css/components.css'/>" />
</head>

<body>
<c:set var="nav" value="inspectors" scope="request"/>
<div class="navigation">
    <%@ include file="../fragments/sidebar.jspf" %>
    <main class="content">
    <div class="container">
        <div class="innerContainer">
            <!-- 상단 영역 -->
            <div class="topContainer">
                <div class="boardContainer" style="
            margin-left: 20px;
            margin-right: 20px;
        ">
                    <div class="statusContainer">
                        <span class="subTitle">작업자 현황</span>
                        <div class="buttonContainer">
                            <button class="register">
                                <img src="<c:url value='/assets/images/plus.svg'/>" alt="+" class="icon" />
                                작업자 등록
                            </button>
                            <button type="button" class="delete" id="deleteBtn">
                                <img src="<c:url value='/assets/images/pencil.svg'/>" alt="+" class="icon" />
                                작업자 삭제
                            </button>
                        </div>
                    </div>

                    <span class="totalTitle">전체</span>
                    <span class="highlight">${totalCount}명</span>
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
                                <td>
                                    <label class="checkbox-cell">
                                        <input type="checkbox" name="selectedIds" value="${inspector.inspectorId}" />
                                        <span>${inspector.inspectorId}</span>
                                    </label>
                                </td>
                                <td onclick="location.href='${pageContext.request.contextPath}/inspectors/detail/${inspector.inspectorId}'" style="cursor:pointer;">
                                        ${inspector.name}
                                </td>
                                <td onclick="location.href='${pageContext.request.contextPath}/inspectors/detail/${inspector.inspectorId}'" style="cursor:pointer;">
                                        ${inspector.phone}
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <div class="pagination">
                        <a href="<c:out value='?page=${currentPage - 1}'/>"
                           class="circle-btn prev ${currentPage == 1 ? 'disabled' : ''}">
                            <img src="<c:url value='/assets/images/left.svg'/>" alt="left" class="icon" />
                        </a>

                        <span class="circle-btn active" id="pageNumber">${currentPage}</span>

                        <a href="?page=${currentPage + 1}" class="circle-btn next">
                            <img src="<c:url value='/assets/images/right.svg'/>" alt="right" class="icon" />
                        </a>
                    </div>
                    <span class="circle-btn active" id="pageNumber">${currentPage}</span>

                    <a href="?page=${currentPage + 1}"
                       class="circle-btn next ${currentPage == totalPages ? 'disabled' : ''}">
                       <img src="<c:url value='/assets/images/right.svg'/>" alt="right" class="icon"/>
                    </a>
                </div>

                <div id="inspectorModal" class="modal">
                    <div class="modal-content">
                        <h2>작업자 등록</h2>
                        <form action="<c:url value='/inspectors/register'/>" method="post">
                            <div class="form-group">
                                <label for="name">이름</label>
                                <input type="text" id="name" name="name" required />
                            </div>

                            <div class="form-group">
                                <label for="phone">연락처</label>
                                <input type="hidden" id="phone" name="phone" />
                                <div class="phone-input">
                                    <input type="text" id="phone1" maxlength="3" required oninput="onlyNumber(this)" />
                                    <span>-</span>
                                    <input type="text" id="phone2" maxlength="4" required oninput="onlyNumber(this)" />
                                    <span>-</span>
                                    <input type="text" id="phone3" maxlength="4" required oninput="onlyNumber(this)" />
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="domain">소속</label>
                                <select id="domain" name="domain" required/>
                                <option value="">소속을 선택하세요</option>
                                <option value="소방">소방</option>
                                <option value="순찰">순찰</option>
                                <option value="청결">청결</option>
                                </select>
                            </div>

                            <div class="form-group">
                                <label for="equipment">설비</label>
                                <select id="equipment" name="equipment" required/>
                                <option value="">소속을 먼저 선택하세요</option>
                                </select>
                            </div>
                            <div class="modal-buttons">
                                <button type="button" class="btn cancel">취소</button>
                                <button type="submit" class="btn submit">등록</button>
                            </div>
                        </form>
                    </div>
                </div>



            <!-- JS -->

<!-- 삭제 모달 -->
<div id="deleteModal" class="modal">
    <div class="modal-content delete-modal">
        <img src="<c:url value='/assets/images/warning.svg'/>" alt="경고" class="warning-icon" />
        <h2>삭제하시겠습니까?</h2>
        <p>한번 삭제한 데이터는 복구할 수 없습니다.</p>
        <form id="deleteForm" action="<c:url value='/inspectors/delete'/>" method="post">
            <input type="hidden" name="selectedIds" id="selectedIdsInput" />
            <div class="modal-buttons">
                <button type="button" class="btn cancel" id="deleteCancel">취소</button>
                <button type="submit" class="btn submit">삭제</button>
            </div>
        </form>
    </div>
</div>
<script src="<c:url value='/assets/js/inspectorsList.js'/>"></script>
</body>
</html>
