<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>점검표 수정</title>
    <link rel="stylesheet" href="<c:url value='/assets/css/reset.css'/>"/>
    <link rel="stylesheet" href="<c:url value='/assets/css/templateForm.css?after'/>"/>
</head>
<body>
<div class="container">
    <div class="inner-container">
        <div class="title-row">
            <h2 class="title">점검표 수정</h2>
            <my:button label="수정하기" bgColor="#9de2d0" cssClass="js-submit"/>
        </div>

        <!-- 버저닝 저장 엔드포인트: copy-on-write로 새 버전 생성 -->
        <form action="<c:url value='/template/update'/>" method="post" class="form">
            <!-- 템플릿 식별/버전 (낙관적 락 용) -->
            <input type="hidden" name="templateId" value="${template.templateId}"/>
            <input type="hidden" name="version" value="${template.version}"/>

            <!-- (선택) Spring Security CSRF -->
            <c:if test="${not empty _csrf}">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </c:if>

            <!-- 점검 유형 -->
            <div class="form-box">
                <span class="subtitle">점검 유형</span>
                <div class="domain-buttons">
                    <label class="domain-btn ${template.domain == 'CLEANING' ? 'active' : ''}">
                        <input type="radio" name="domain" value="CLEANING" ${template.domain == 'CLEANING' ? 'checked' : ''}/>
                        <img src="<c:url value='/assets/images/cleaning.svg'/>" alt="미화"/>
                        <span>미화</span>
                    </label>
                    <label class="domain-btn ${template.domain == 'FIRE' ? 'active' : ''}">
                        <input type="radio" name="domain" value="FIRE" ${template.domain == 'FIRE' ? 'checked' : ''}/>
                        <img src="<c:url value='/assets/images/fire.svg'/>" alt="소방"/>
                        <span>소방</span>
                    </label>
                    <label class="domain-btn ${template.domain == 'PATROL' ? 'active' : ''}">
                        <input type="radio" name="domain" value="PATROL" ${template.domain == 'PATROL' ? 'checked' : ''}/>
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
                           value="${fn:escapeXml(template.name)}"
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
                        <input type="number" id="cycleNumber" name="cycle" min="1" value="${template.cycle}"/>
                        <select id="cycleUnit" name="cycleUnit">
                            <option value="DAY"   ${template.cycleUnit == 'DAY'   ? 'selected' : ''}>일</option>
                            <option value="WEEK"  ${template.cycleUnit == 'WEEK'  ? 'selected' : ''}>주</option>
                            <option value="MONTH" ${template.cycleUnit == 'MONTH' ? 'selected' : ''}>개월</option>
                            <option value="YEAR"  ${template.cycleUnit == 'YEAR'  ? 'selected' : ''}>년</option>
                        </select>
                    </div>
                    <span id="cycleDesc" class="cycle-desc"></span>
                </div>
            </div>

            <!-- 점검 항목 -->
            <div class="form-group">
                <div class="form-title">
                    <span class="subtitle">점검 항목</span>
                    <span class="desc">작업자가 점검해야 하는 항목을 입력해주세요</span>
                </div>

                <!-- 리스트 컨테이너 (서버 데이터로 초기 렌더링) -->
                <div id="checkItemList" class="check-item-list">
                    <c:forEach var="item" items="${items}" varStatus="st">
                        <c:set var="idx" value="${st.index}"/>
                        <!-- DB ENUM → 폼 값 매핑: BOOL→BOOLEAN, NUM→NUMBER, IMAGE→PHOTO, TEXT→TEXT -->
                        <c:set var="typeForm"
                               value="${item.type == 'BOOL' ? 'BOOLEAN' : (item.type == 'NUM' ? 'NUMBER' : (item.type == 'IMAGE' ? 'PHOTO' : 'TEXT'))}"/>

                        <div class="check-item" data-index="${idx}">
                            <!-- 상단 옵션(타입) -->
                            <div class="option-row">
                                <div class="type-options" role="radiogroup" aria-label="항목 타입 선택">
                                    <label class="type-option ${typeForm == 'BOOLEAN' ? 'active' : ''}">
                                        <input type="radio" name="items[${idx}].type" value="BOOLEAN" ${typeForm == 'BOOLEAN' ? 'checked' : ''}/>
                                        <span class="opt-emoji"><span class="icon icon-check"></span></span><span>예/아니오</span>
                                    </label>
                                    <label class="type-option ${typeForm == 'TEXT' ? 'active' : ''}">
                                        <input type="radio" name="items[${idx}].type" value="TEXT" ${typeForm == 'TEXT' ? 'checked' : ''}/>
                                        <span class="opt-emoji"><span class="icon icon-text"></span></span><span>텍스트</span>
                                    </label>
                                    <label class="type-option ${typeForm == 'NUMBER' ? 'active' : ''}">
                                        <input type="radio" name="items[${idx}].type" value="NUMBER" ${typeForm == 'NUMBER' ? 'checked' : ''}/>
                                        <span class="opt-emoji"><span class="icon icon-number"></span></span><span>숫자</span>
                                    </label>
                                    <label class="type-option ${typeForm == 'PHOTO' ? 'active' : ''}">
                                        <input type="radio" name="items[${idx}].type" value="PHOTO" ${typeForm == 'PHOTO' ? 'checked' : ''}/>
                                        <span class="opt-emoji"><span class="icon icon-image"></span></span><span>사진</span>
                                    </label>
                                </div>
                                <div class="delete-wrapper">
                                    <img src="<c:url value='/assets/images/trash.svg'/>" alt="삭제" class="delete-icon"/>
                                </div>
                            </div>

                            <!-- 하단 인풋(항목 문구) -->
                            <div class="input-wrapper">
                                <input type="text" class="check-input"
                                       name="items[${idx}].label"
                                       value="${fn:escapeXml(item.label)}"
                                       maxlength="33"
                                       placeholder="점검 내용을 입력해주세요"
                                       aria-label="점검 항목 내용"/>
                                <span class="char-count">0/33</span>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <!-- 항목 템플릿(신규 추가용) -->
                <template id="checkItemTemplate">
                    <div class="check-item" data-index="{INDEX}">
                        <div class="option-row">
                            <div class="type-options" role="radiogroup" aria-label="항목 타입 선택">
                                <label class="type-option active">
                                    <input type="radio" name="items[{INDEX}].type" value="BOOLEAN" checked />
                                    <span class="opt-emoji"><span class="icon icon-check"></span></span><span>예/아니오</span>
                                </label>
                                <label class="type-option">
                                    <input type="radio" name="items[{INDEX}].type" value="TEXT" />
                                    <span class="opt-emoji"><span class="icon icon-text"></span></span><span>텍스트</span>
                                </label>
                                <label class="type-option">
                                    <input type="radio" name="items[{INDEX}].type" value="NUMBER" />
                                    <span class="opt-emoji"><span class="icon icon-number"></span></span><span>숫자</span>
                                </label>
                                <label class="type-option">
                                    <input type="radio" name="items[{INDEX}].type" value="PHOTO" />
                                    <span class="opt-emoji"><span class="icon icon-image"></span></span><span>사진</span>
                                </label>
                            </div>
                            <div class="delete-wrapper">
                                <img src="<c:url value='/assets/images/trash.svg'/>" alt="삭제" class="delete-icon"/>
                            </div>
                        </div>

                        <div class="input-wrapper">
                            <input type="text" class="check-input"
                                   name="items[{INDEX}].label"
                                   maxlength="33"
                                   placeholder="점검 내용을 입력해주세요"
                                   aria-label="점검 항목 내용"/>
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

<script src="<c:url value='/assets/js/template.js?ver=20250910'/>"></script>
<script>
    // 최초 카운터/주기 텍스트 초기화에 필요한 값이 이미 채워져 있으므로,
    // 기존 template.js의 init들이 DOMContentLoaded 후 정상 동작합니다.
</script>
</body>
</html>
