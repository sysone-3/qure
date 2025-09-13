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
                            <td>${inspector.inspectorId}</td>
                            <td>${inspector.name}</td>
                            <td>${inspector.phone}</td>
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
                        <div class="phone-input">
                            <input type="text" id="phone1" name="phone1" maxlength="3" pattern="\d{3}" required oninput="this.value=this.value.replace(/[^0-9]/g,'');"/>
                            <span>-</span>
                            <input type="text" id="phone2" name="phone2" maxlength="4" pattern="\d{3,4}" required oninput="this.value=this.value.replace(/[^0-9]/g,'');"/>
                            <span>-</span>
                            <input type="text" id="phone3" name="phone3" maxlength="4" pattern="\d{4}" required oninput="this.value=this.value.replace(/[^0-9]/g,'');"/>
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
    </div>
    <script src="<c:url value='/assets/js/inspectorsList.js'/>"></script>
</body>
</html>
