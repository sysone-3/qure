<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>설비 추가</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- facilities 전용 CSS만 사용 -->
    <link rel="stylesheet" href="<c:url value='/assets/css/facilities_add.css'/>">
</head>
<body>
<div class="page">
    <div class="form-card">

        <!-- 타이틀  -->
        <div class="title-row">
            <h2 class="form-title">설비 수정</h2>
        </div>

        <form id="facilityForm" action="<c:url value='/facilities'/>" method="post">
            <!-- 시설명 -->
            <div class="form-box">
                <div class="cell">시설명</div>
                <div class="input-wrapper">
                    <input type="text" name="name" class="text-input" placeholder="예: 본관 전기실" required>
                </div>
            </div>

            <!-- 설비 주소 (그리드 레이아웃로 전면 개편) -->
            <div class="form-box">
                <div class="cell">설비 주소</div>

                <div id="addressBlock" class="address-grid">
                    <div class="address-row">
                        <label class="label" for="zipNo">우편번호</label>
                        <div class="field with-action">
                            <input type="text" id="zipNo" name="zipNo" class="text-input" readonly>
                            <button type="button" class="btn-inline-action" onclick="goPopup()">주소검색</button>
                        </div>
                    </div>

                    <div class="address-row">
                        <label class="label" for="roadAddrPart1">도로명주소</label>
                        <div class="field">
                            <input type="text" id="roadAddrPart1" name="roadAddrPart1" class="text-input">
                        </div>
                    </div>

                    <div class="address-row">
                        <label class="label" for="addrDetail">상세주소</label>
                        <div class="field">
                            <input type="text" id="addrDetail" name="addrDetail" class="text-input">
                        </div>
                    </div>
                </div>

                <!-- 서버 전송용 합쳐진 주소 -->
                <input type="hidden" id="address" name="address" value="">
            </div>

            <!-- 도메인 -->
            <div class="form-box">
                <div class="cell">도메인</div>
                <div class="input-wrapper">
                    <select name="domain" class="select-input">
                        <option value="청결">청결</option>
                        <option value="순찰">순찰</option>
                        <option value="소방">소방</option>
                    </select>
                </div>
            </div>

            <!-- 층 / 상세 구역 (2열) -->
            <div class="form-box">
                <div class="cell">층</div>
                <div class="two-col">
                    <div class="input-wrapper">
                        <input type="text" name="floor" class="text-input" placeholder="(예: B2)">
                    </div>
                </div>

                <div class="cell">상세 구역</div>
                <div class="two-col">
                    <div class="input-wrapper">
                        <input type="text" name="zone" class="text-input" placeholder="(예: 로비)">
                    </div>
                </div>
            </div>

            <!-- 점검자 ID -->
            <div class="form-box">
                <div class="cell">점검자 ID</div>
                <div class="input-wrapper">
                    <input type="text" name="inspectorId" class="text-input" placeholder="(예: 1)">
                </div>
            </div>

            <!-- 점검표 선택 -->
            <div class="form-box">
                <div class="cell">점검표 선택</div>

                <div class="template-section">
                    <button type="button" id="btnAddTemplate" class="btn-add-template">
                        <span class="add-plus">+</span> 점검표 추가
                    </button>

                    <div class="template-list" id="selectedTemplates">
                        <div class="empty-template">선택된 점검표가 없습니다</div>
                    </div>

                    <!-- 선택된 점검표 ID들 -->
                    <input type="hidden" id="templateIds" name="templateIds" value="">
                </div>
            </div>

            <!-- 액션 버튼 -->
            <div class="form-actions">
                <button type="submit" class="btn btn-brand">등록</button>
                <a class="btn" href="<c:url value='/facilities'/>">취소</a>
            </div>
        </form>
    </div>
</div>

<script>
    // ===== 주소 검색 팝업 =====
    function goPopup(){
        var url = "<c:url value='/popup/juso'/>";
        window.open(url, "pop", "width=570,height=420,scrollbars=yes,resizable=yes");
    }
    // 팝업 콜백
    function jusoCallBack(fullAddress, roadAddrPart1, addrDetail) {
        document.getElementById('roadAddrPart1').value = roadAddrPart1 || '';
        document.getElementById('addrDetail').value    = addrDetail    || '';
        document.getElementById('address').value =
            (fullAddress && fullAddress.trim().length > 0)
                ? fullAddress
                : [roadAddrPart1, addrDetail].filter(Boolean).join(' ');
    }

    // ===== 점검표 선택 관리 =====
    let selectedTemplates = [];

    document.addEventListener("DOMContentLoaded", function() {
        const btn = document.getElementById("btnAddTemplate");
        if (btn) btn.addEventListener("click", function() {
            const url = "<c:url value='/template/select'/>?popup=true";
            window.open(url, "templateSelectPop", "width=720,height=640,scrollbars=yes,resizable=yes");
        });
    });

    // 팝업에서 호출될 콜백들
    function selectTemplateCallback(templateId, templateName, domain) {
        if (selectedTemplates.find(t => t.id === templateId)) {
            alert('이미 선택된 점검표입니다.');
            return;
        }
        selectedTemplates.push({ id: templateId, name: templateName, domain });
        updateTemplateDisplay();
    }
    function addNewTemplateCallback(templateId, templateName, domain) {
        selectedTemplates.push({ id: templateId, name: templateName, domain });
        updateTemplateDisplay();
    }
    function removeTemplate(templateId) {
        selectedTemplates = selectedTemplates.filter(t => t.id !== templateId);
        updateTemplateDisplay();
    }
    function updateTemplateDisplay() {
        const container = document.getElementById('selectedTemplates');
        const templateIdsInput = document.getElementById('templateIds');

        if (selectedTemplates.length === 0) {
            container.innerHTML = '<div class="empty-template">선택된 점검표가 없습니다</div>';
            templateIdsInput.value = '';
            return;
        }
        let html = '';
        selectedTemplates.forEach(template => {
            html += `
              <div class="template-item">
                <div class="template-info">
                  <div class="template-name">${template.name}</div>
                  <div class="template-domain">
                    <c:choose>
                        <c:when test="${t.domain eq 'CLEANING'}">미화</c:when>
                        <c:when test="${t.domain eq 'FIRE'}">소방</c:when>
                        <c:when test="${t.domain eq 'PATROL'}">순찰</c:when>
                        <c:otherwise>${t.domain}</c:otherwise>
                    </c:choose>
                   </div>
                </div>
                <div class="template-actions">
                  <button type="button" class="btn-remove" onclick="removeTemplate('${template.id}')">삭제</button>
                </div>
              </div>`;
        });
        container.innerHTML = html;
        templateIdsInput.value = selectedTemplates.map(t => t.id).join(',');
    }
    function getDomainText(domain) {
        switch(domain) {
            case 'CLEANING': return '미화';
            case 'FIRE':     return '소방';
            case 'PATROL':   return '순찰';
            default:         return domain;
        }
    }

    // 새로 등록된 템플릿 파라미터 처리(있을 때만)
    window.onload = function() {
        const urlParams = new URLSearchParams(window.location.search);
        const newTemplateId = urlParams.get('newTemplateId');
        const newTemplateName = urlParams.get('newTemplateName');
        const newTemplateDomain = urlParams.get('newTemplateDomain');
        if (newTemplateId && newTemplateName && newTemplateDomain) {
            addNewTemplateCallback(newTemplateId, decodeURIComponent(newTemplateName), newTemplateDomain);
            const cleanUrl = window.location.pathname;
            history.replaceState({}, document.title, cleanUrl);
        }
    };
</script>
</body>
</html>
