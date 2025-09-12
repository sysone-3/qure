<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>설비 수정</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 공용 CSS만 링크 (스타일 섹션은 제거) -->
    <link rel="stylesheet" href="<c:url value='/assets/css/facilities_add.css'/>">
</head>
<body>
<div class="page">
    <div class="form-card">
        <form id="facilityForm" action="<c:url value='/facilities/${facility.facilityId}'/>" method="post">
            <!-- PK -->
            <input type="hidden" name="facilityId" value="${facility.facilityId}"/>

            <div class="cell">시설명</div>
            <input type="text" name="name" value="${facility.name}" placeholder="예: 본관 전기실" required>

            <div class="cell">설비 주소</div>
            <div id="addressBlock">
                <table>
                    <colgroup><col style="width:20%"><col></colgroup>
                    <tbody>
                    <tr>
                        <th>우편번호</th>
                        <td>
                            <input type="text" id="zipNo" name="zipNo" readonly style="width:100px">
                            <input type="button" value="주소검색" onclick="goPopup();">
                        </td>
                    </tr>
                    <tr>
                        <th>도로명주소</th>
                        <td><input type="text" id="roadAddrPart1" name="roadAddrPart1" style="width:85%"></td>
                    </tr>
                    <tr>
                        <th>상세주소</th>
                        <td><input type="text" id="addrDetail" name="addrDetail" style="width:40%"></td>
                    </tr>
                    </tbody>
                </table>
                <!-- DB로 최종 전송 -->
                <input type="hidden" id="address" name="address" value="${facility.address}">
            </div>

            <div class="cell">도메인</div>
            <label>
                <select name="domain">
                    <option value="청결" <c:if test="${facility.domain == '청결'}">selected</c:if>>청결</option>
                    <option value="순찰" <c:if test="${facility.domain == '순찰'}">selected</c:if>>순찰</option>
                    <option value="소방" <c:if test="${facility.domain == '소방'}">selected</c:if>>소방</option>
                </select>
            </label>

            <div class="cell">층</div>
            <div class="row">
                <input type="text" name="floor" value="${facility.floor}" placeholder="(예: B2)">
            </div>

            <div class="cell">상세 구역</div>
            <div class="row">
                <input type="text" name="zone" value="${facility.zone}" placeholder="(예: 로비)">
            </div>

            <div class="cell">점검자 ID</div>
            <div class="row">
                <input type="text" name="inspectorId" value="${facility.inspectorId}" placeholder="(예: 1)">
            </div>

            <!-- 점검표 선택 -->
            <div class="cell">점검표 선택</div>
            <div class="template-section">
                <button type="button" id="btnAddTemplate" class="btn-add-template">
                    점검표 추가
                </button>

                <div class="template-list" id="selectedTemplates">
                    <div class="empty-template">선택된 점검표가 없습니다</div>
                </div>

                <!-- 선택된 점검표 ID 콤마로 전송 -->
                <input type="hidden" id="templateIds" name="templateIds" value="">
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-brand">저장</button>
                <a class="btn" href="<c:url value='/facilities/${facility.facilityId}'/>">취소</a>
            </div>
        </form>
    </div>
</div>

<!-- 서버에서 내려주는 초기 연결 템플릿들 (숨김) -->
<div id="server-templates" style="display:none">
    <!-- attachedTemplates가 있을 때 -->
    <c:forEach var="t" items="${attachedTemplates}">
        <div class="t"
             data-id="${t.templateId}"
             data-name="<c:out value='${t.name}'/>"
             data-domain="<c:out value='${t.domain}'/>"></div>
    </c:forEach>

    <!-- attachedTemplates가 비어있고 facility.templates를 쓰는 경우(대체 경로) -->
    <c:if test="${empty attachedTemplates}">
        <c:forEach var="t" items="${facility.templates}">
            <div class="t"
                 data-id="${t.templateId}"
                 data-name="<c:out value='${t.name}'/>"
                 data-domain="<c:out value='${t.domain}'/>"></div>
        </c:forEach>
    </c:if>
</div>

<script>
    /* ===== 주소 검색 & 프리필 ===== */
    function goPopup(){
        var url = "<c:url value='/popup/juso'/>";
        window.open(url, "pop", "width=570,height=420,scrollbars=yes,resizable=yes");
    }

    function jusoCallBack(fullAddress, roadAddrPart1, addrDetail, zipNo) {
        document.getElementById('roadAddrPart1').value = roadAddrPart1 || '';
        document.getElementById('addrDetail').value    = addrDetail    || '';
        document.getElementById('zipNo').value         = zipNo         || '';
        document.getElementById('address').value =
            (fullAddress && fullAddress.trim().length > 0)
                ? fullAddress
                : [roadAddrPart1, addrDetail].filter(Boolean).join(' ');
    }

    (function prefillAddress(){
        var full = document.getElementById('address').value || '';
        if (!full) return;
        var idx = full.lastIndexOf(' ');
        if (idx > 0) {
            var road = full.substring(0, idx);
            var detail = full.substring(idx + 1);
            if (!document.getElementById('roadAddrPart1').value) document.getElementById('roadAddrPart1').value = road;
            if (!document.getElementById('addrDetail').value)    document.getElementById('addrDetail').value    = detail;
        } else {
            if (!document.getElementById('roadAddrPart1').value) document.getElementById('roadAddrPart1').value = full;
        }
    })();

    /* ===== 점검표 선택 로직 (JSP EL 없이 순수 JS) ===== */
    let selectedTemplates = [];

    function escapeHtml(str) {
        if (!str) return '';
        return str.replace(/[&<>"']/g, m => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[m]));
    }

    function getDomainText(domain) {
        switch (String(domain).toUpperCase()) {
            case 'CLEANING': return '미화';
            case 'PATROL':   return '순찰';
            case 'FIRE':     return '소방';
            default:         return domain || '';
        }
    }

    // 서버에서 숨겨둔 data-* 를 읽어서 selectedTemplates 세팅
    function initSelectedTemplatesFromServer() {
        const wrap = document.getElementById('server-templates');
        if (!wrap) return;
        selectedTemplates = [];
        wrap.querySelectorAll('.t').forEach(el => {
            selectedTemplates.push({
                id: String(el.dataset.id),
                name: el.dataset.name || '',
                domain: el.dataset.domain || ''
            });
        });
    }

    function updateTemplateDisplay() {
        const container = document.getElementById('selectedTemplates');
        const templateIdsInput = document.getElementById('templateIds');

        if (!selectedTemplates.length) {
            container.innerHTML = '<div class="empty-template">선택된 점검표가 없습니다</div>';
            templateIdsInput.value = '';
            return;
        }

        let html = '';
        selectedTemplates.forEach(t => {
            html += `
        <div class="template-item">
          <div class="template-info">
            <div class="template-name">${escapeHtml(t.name)}</div>
            <div class="template-domain">${escapeHtml(getDomainText(t.domain))}</div>
          </div>
          <div class="template-actions">
            <button type="button" class="btn-remove" onclick="removeTemplate('${t.id}')">삭제</button>
          </div>
        </div>`;
        });

        container.innerHTML = html;
        templateIdsInput.value = selectedTemplates.map(t => t.id).join(',');
    }

    function removeTemplate(templateId) {
        selectedTemplates = selectedTemplates.filter(t => String(t.id) !== String(templateId));
        updateTemplateDisplay();
    }

    // 팝업에서 선택 시 호출될 콜백 (기존 선택 팝업 재사용)
    function selectTemplateCallback(templateId, templateName, domain) {
        if (selectedTemplates.find(t => String(t.id) === String(templateId))) {
            alert('이미 선택된 점검표입니다.');
            return;
        }
        selectedTemplates.push({ id: String(templateId), name: templateName || '', domain: domain || '' });
        updateTemplateDisplay();
    }

    // 신규 생성 팝업에서 돌아올 때 호출
    function addNewTemplateCallback(templateId, templateName, domain) {
        selectedTemplates.push({ id: String(templateId), name: templateName || '', domain: domain || '' });
        updateTemplateDisplay();
    }

    document.addEventListener('DOMContentLoaded', () => {
        // 초기 템플릿 채우기
        initSelectedTemplatesFromServer();
        updateTemplateDisplay();

        // 선택 팝업 열기
        const btn = document.getElementById('btnAddTemplate');
        if (btn) {
            btn.addEventListener('click', () => {
                const url = "<c:url value='/template/select'/>?popup=true";
                window.open(url, "templateSelectPop", "width=720,height=640,scrollbars=yes,resizable=yes");
            });
        }
    });

    // URL 파라미터에 새 템플릿이 있으면 추가 후 URL 정리
    window.addEventListener('load', () => {
        const p = new URLSearchParams(location.search);
        const id = p.get('newTemplateId');
        const name = p.get('newTemplateName');
        const domain = p.get('newTemplateDomain');
        if (id && name && domain) {
            addNewTemplateCallback(id, decodeURIComponent(name), domain);
            history.replaceState({}, document.title, location.pathname);
        }
    });
</script>
</body>
</html>
