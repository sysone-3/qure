<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>점검표 등록</title>
    <link rel="stylesheet" href="<c:url value='/assets/css/reset.css'/>"/>
    <link rel="stylesheet" href="<c:url value='/assets/css/navigation.css'/>" />
    <link rel="stylesheet" href="<c:url value='/assets/css/components.css'/>" />

    <link rel="stylesheet" href="<c:url value='/assets/css/templateForm.css?after'/>"/>
</head>
<body>
<c:set var="nav" value="template" scope="request"/>
<div class="layout">
<%@ include file="/WEB-INF/views/fragments/sidebar.jspf" %>
<main class="content">
<div class="container">
    <div class="inner-container">
        <div class="title-row">
            <h2 class="title">점검표 등록</h2>
            <my:button label="등록하기" bgColor="#9de2d0" cssClass="js-submit"/>
        </div>

        <form action="<c:url value='/template/insert'/>" method="post" class="form">
            <sec:csrfInput/>
            <!-- 점검 유형 -->
            <div class="form-box">
                <span class="subtitle">점검 유형</span>
                <div class="domain-buttons">
                    <label class="domain-btn">
                        <input type="radio" name="domain" value="CLEANING" checked/>
                        <img src="<c:url value='/assets/images/cleaning.svg'/>" alt="미화"/>
                        <span>미화</span>
                    </label>
                    <label class="domain-btn">
                        <input type="radio" name="domain" value="FIRE"/>
                        <img src="<c:url value='/assets/images/fire.svg'/>" alt="소방"/>
                        <span>소방</span>
                    </label>
                    <label class="domain-btn">
                        <input type="radio" name="domain" value="PATROL"/>
                        <img src="<c:url value='/assets/images/patrol.svg'/>" alt="순찰"/>
                        <span>순찰</span>
                    </label>
                </div>
            </div>

            <!-- 점검표 이름 -->
            <div class="form-box">
                <div class="form-title">
                    <span class="subtitle">점검표 이름</span>
                    <span class="desc">점검표 이름은 추후 수정할 수 있어요</span>
                </div>
                <div class="input-wrapper">
                    <input type="text" id="templateName" name="name" maxlength="33"
                           placeholder="점검표 이름을 입력하세요"/>
                    <span class="char-count" id="nameCount">0/33</span>
                </div>
            </div>

            <!-- 점검 주기 -->
            <div class="form-group" style="gap: 20px;">
                <div class="form-title">
                    <span class="subtitle">점검 주기</span>
                    <span class="desc">점검이 반복될 주기를 입력해주세요</span>
                </div>
                <div style="display: flex; flex-direction: column; gap: 5px;">
                    <div class="cycle-inputs">
                        <input type="number" id="cycleNumber" name="cycle" min="1" value="1" />
                        <select id="cycleUnit" name="cycleUnit">
                            <option value="DAY">일</option>
                            <option value="WEEK">주</option>
                            <option value="MONTH">개월</option>
                            <option value="YEAR">년</option>
                        </select>
                    </div>
                    <span id="cycleDesc" class="cycle-desc">현재 설정: 1일에 한 번 점검</span>
                </div>
            </div>

            <!-- 점검 항목 -->
            <div class="form-group">
                <div class="form-title">
                    <span class="subtitle">점검 항목</span>
                    <span class="desc">작업자가 점검해야 하는 항목을 입력해주세요</span>
                </div>
                <!-- 리스트 컨테이너 -->
                <div id="checkItemList" class="check-item-list"></div>

                <!-- 항목 템플릿 -->
                <template id="checkItemTemplate">
                    <div class="check-item" data-index="{INDEX}">
                        <!-- 상단 옵션(타입) -->
                        <div class="option-row">
                            <div class="type-options" role="radiogroup" aria-label="항목 타입 선택">
                                <label class="type-option active">
                                    <input type="radio" name="items[{INDEX}].type" value="BOOLEAN" checked />
                                    <span class="opt-emoji">
                                        <span class="icon icon-check"></span>
                                    </span><span>예/아니오</span>
                                </label>
                                <label class="type-option">
                                    <input type="radio" name="items[{INDEX}].type" value="TEXT" />
                                    <span class="opt-emoji">
                                        <span class="icon icon-text"></span>
                                    </span><span>텍스트</span>
                                </label>
                                <label class="type-option">
                                    <input type="radio" name="items[{INDEX}].type" value="NUMBER" />
                                    <span class="opt-emoji">
                                        <span class="icon icon-number"></span>
                                    </span><span>숫자</span>
                                </label>
                                <label class="type-option">
                                    <input type="radio" name="items[{INDEX}].type" value="PHOTO" />
                                    <span class="opt-emoji">
                                        <span class="icon icon-image"></span>
                                    </span><span>사진</span>
                                </label>
                            </div>
                            <div class="delete-wrapper">
                                <img src="<c:url value='/assets/images/trash.svg'/>" alt="삭제" class="delete-icon"/>
                            </div>
                        </div>

                        <!-- 하단 인풋(항목 문구) -->
                        <div class="input-wrapper">
                            <input
                                    type="text"
                                    class="check-input"
                                    name="items[{INDEX}].label"
                                    maxlength="33"
                                    placeholder="점검 내용을 입력해주세요"
                                    aria-label="점검 항목 내용"
                            />
                            <span class="char-count">0/33</span>
                        </div>
                    </div>
                </template>

                <!-- 항목 추가 버튼 -->
                <my:iconButton label="항목 추가"
                               id="addItemBtn"
                               icon="/assets/images/plus.svg"
                               bgColor="#FEE39A"
                               cssClass="btn-inline"/>
            </div>
        </form>
    </div>
</div>
</main>
</div>
<script src="<c:url value='/assets/js/template.js?ver=20250910'/>"></script>
</body>
</html>
