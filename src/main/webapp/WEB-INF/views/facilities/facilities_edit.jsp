<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>설비 수정</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- facilities 전용 CSS -->
    <link rel="stylesheet" href="<c:url value='/assets/css/facilities_add.css'/>">
</head>
<body>
<div class="page">
    <div class="form-card">
        <!-- 타이틀  -->
        <div class="title-row">
            <h2 class="form-title">설비 수정</h2>
        </div>

        <form id="facilityForm" action="<c:url value='/facilities/${facility.facilityId}'/>" method="post">
            <!-- PK hidden -->
            <input type="hidden" name="facilityId" value="${facility.facilityId}"/>

            <!-- 시설명 -->
            <div class="cell">시설명</div>
            <div class="input-wrapper">
                <input type="text" name="name" class="text-input"
                       value="${facility.name}" placeholder="예: 본관 전기실" required>
            </div>

            <!-- 설비 주소 (그리드 레이아웃) -->
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
                        <!-- SSR 기본값: 전체 주소를 일단 도로명에 박아둔다(보여주기 보장) -->
                        <input type="text" id="roadAddrPart1" name="roadAddrPart1"
                               class="text-input" value="${facility.address}">
                    </div>
                </div>

                <div class="address-row">
                    <label class="label" for="addrDetail">상세주소</label>
                    <div class="field">
                        <input type="text" id="addrDetail" name="addrDetail" class="text-input">
                    </div>
                </div>
            </div>
            <!-- DB에 저장/전송되는 최종 주소 -->
            <input type="hidden" id="address" name="address" value="${facility.address}">

            <!-- 도메인 -->
            <div class="cell">도메인</div>
            <div class="input-wrapper">
                <select name="domain" class="select-input">
                    <option value="청결" <c:if test="${facility.domain == '청결'}">selected</c:if>>청결</option>
                    <option value="순찰" <c:if test="${facility.domain == '순찰'}">selected</c:if>>순찰</option>
                    <option value="소방" <c:if test="${facility.domain == '소방'}">selected</c:if>>소방</option>
                </select>
            </div>

            <!-- 층 -->
            <div class="cell">층</div>
            <div class="row">
                <input type="text" name="floor" class="text-input"
                       value="${facility.floor}" placeholder="(예: B2)">
            </div>

            <!-- 상세 구역 -->
            <div class="cell">상세 구역</div>
            <div class="row">
                <input type="text" name="zone" class="text-input"
                       value="${facility.zone}" placeholder="(예: 로비)">
            </div>

            <!-- 점검자 ID -->
            <div class="cell">점검자 ID</div>
            <div class="row">
                <input type="text" name="inspectorId" class="text-input"
                       value="${facility.inspectorId}" placeholder="(예: 1)">
            </div>

            <!-- 점검표 선택 -->
            <div class="cell">점검표 선택</div>
            <div class="template-section">
                <button type="button" id="btnAddTemplate" class="btn-add-template">
                    <span class="add-plus">+</span> 점검표 추가
                </button>

                <div class="template-list" id="selectedTemplates">
                    <div class="empty-template">선택된 점검표가 없습니다</div>
                </div>

                <!-- 선택된 점검표 ID들을 저장할 hidden input -->
                <input type="hidden" id="templateIds" name="templateIds" value="">
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-brand">저장</button>
                <a class="btn" href="<c:url value='/facilities/${facility.facilityId}'/>">취소</a>
            </div>
        </form>
    </div>
</div>

<!-- 서버에서 전달받은 기존 점검표들을 저장할 숨김 영역 -->
<div id="server-templates" style="display:none;">
    <c:forEach var="template" items="${attachedTemplates}">
        <div class="template-data"
             data-id="${template.templateId}"
             data-name="<c:out value='${template.name}'/>"
             data-domain="<c:out value='${template.domain}'/>">
        </div>
    </c:forEach>
</div>

<script>
    // ===== 주소검색 팝업 =====
    function goPopup(){
        var url = "<c:url value='/popup/juso'/>";
        window.open(url, "pop", "width=570,height=420,scrollbars=yes,resizable=yes");
    }

    // juso 콜백
    function jusoCallBack(fullAddress, roadAddrPart1, addrDetail, zipNo) {
        document.getElementById('roadAddrPart1').value = roadAddrPart1 || fullAddress || '';
        document.getElementById('addrDetail').value    = addrDetail    || '';
        document.getElementById('zipNo').value         = zipNo         || '';
        document.getElementById('address').value =
            (fullAddress && fullAddress.trim().length > 0)
                ? fullAddress
                : [roadAddrPart1, addrDetail].filter(Boolean).join(' ');
    }

    // ===== 주소 프리필(안전모드) =====
    document.addEventListener('DOMContentLoaded', function () {
        const addrFullEl = document.getElementById('address');       // hidden full address
        const roadEl     = document.getElementById('roadAddrPart1'); // 도로명
        const detailEl   = document.getElementById('addrDetail');    // 상세

        if (!addrFullEl || !roadEl || !detailEl) {
            console.warn('[prefill] 필요한 엘리먼트를 못 찾았습니다.');
            return;
        }

        const full = (addrFullEl.value || '').trim();
        if (!full) {
            // 컨트롤러에서 facility.address가 비어있으면 여기서 종료
            return;
        }

        // 1) SSR에서 이미 도로명에 전체 주소를 박아뒀음(보장)
        // 2) 이제 상세주소 패턴이 보이면 분리 시도 → 실패하면 그대로 둠

        let road = full, detail = '';

        // 쉼표 기반: "도로명, 상세"
        if (full.includes(',')) {
            const parts = full.split(',');
            road = parts[0].trim();
            detail = parts.slice(1).join(',').trim();
        } else {
            // 괄호 상세: "도로명 (상세)"
            const m = full.match(/^(.*)\s*\((.+)\)\s*$/);
            if (m) {
                road = m[1].trim();
                detail = m[2].trim();
            } else {
                // 마지막 공백 분리(보조)
                const idx = full.lastIndexOf(' ');
                if (idx > 0) {
                    road = full.substring(0, idx).trim();
                    detail = full.substring(idx + 1).trim();
                }
            }
        }

        if (!detailEl.value && detail) detailEl.value = detail;
        if (!roadEl.value && road)     roadEl.value   = road;

        // hidden도 도로명+상세로 정리(서브밋 일관성)
        addrFullEl.value = [roadEl.value, detailEl.value].filter(Boolean).join(' ');
    });

    // ===== 점검표 선택 관리 =====
    let selectedTemplates = [];

    function loadExistingTemplates() {
        const serverTemplates = document.getElementById('server-templates');
        if (!serverTemplates) return;

        const templateElements = serverTemplates.querySelectorAll('.template-data');
        selectedTemplates = [];

        templateElements.forEach(element => {
            const templateId = element.getAttribute('data-id');
            const templateName = element.getAttribute('data-name');
            const templateDomain = element.getAttribute('data-domain');

            if (templateId && templateName) {
                selectedTemplates.push({
                    id: templateId,
                    name: templateName,
                    domain: templateDomain || ''
                });
            }
        });

        updateTemplateDisplay();
    }

    function openTemplateSelectPopup() {
        const url = "<c:url value='/template/select'/>?popup=true";
        window.open(url, "templateSelectPop", "width=720,height=640,scrollbars=yes,resizable=yes");
    }

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
            html +=
                '<div class="template-item">' +
                '<div class="template-info">' +
                '<div class="template-name">' + escapeHtml(template.name) + '</div>' +
                '<div class="template-domain">' + getDomainText(template.domain) + '</div>' +
                '</div>' +
                '<div class="template-actions">' +
                '<button type="button" class="btn-remove" onclick="removeTemplate(\'' + template.id + '\')">삭제</button>' +
                '</div>' +
                '</div>';
        });
        container.innerHTML = html;

        templateIdsInput.value = selectedTemplates.map(t => t.id).join(',');
    }

    function escapeHtml(text) {
        if (!text) return '';
        const map = { '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#039;' };
        return text.replace(/[&<>"']/g, m => map[m]);
    }

    function getDomainText(domain) {
        switch(domain) {
            case 'CLEANING': return '미화';
            case 'FIRE':     return '소방';
            case 'PATROL':   return '순찰';
            case '청결':     return '청결';
            case '소방':     return '소방';
            case '순찰':     return '순찰';
            default:         return domain || '';
        }
    }

    document.addEventListener("DOMContentLoaded", function() {
        loadExistingTemplates();
        const btnAddTemplate = document.getElementById("btnAddTemplate");
        if (btnAddTemplate) btnAddTemplate.addEventListener("click", openTemplateSelectPopup);
    });

    // 새로 등록된 점검표 파라미터 처리
    window.onload = function() {
        const urlParams = new URLSearchParams(window.location.search);
        const id = urlParams.get('newTemplateId');
        const name = urlParams.get('newTemplateName');
        const domain = urlParams.get('newTemplateDomain');

        if (id && name && domain) {
            addNewTemplateCallback(id, decodeURIComponent(name), domain);
            const cleanUrl = window.location.pathname;
            history.replaceState({}, document.title, cleanUrl);
        }
    };
</script>
</body>
</html>
