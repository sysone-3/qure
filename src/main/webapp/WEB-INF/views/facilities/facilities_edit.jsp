<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>설비 수정</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="<c:url value='/assets/css/facilities_add.css'/>">
</head>
<body>
<div class="page">
    <div class="form-card">
        <form id="facilityForm"  action="<c:url value='/facilities/${facility.facilityId}'/>"
              method="post">

            <!-- PK hidden -->
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
                        <td>
                            <input type="text" id="roadAddrPart1" name="roadAddrPart1" style="width:85%">
                        </td>
                    </tr>
                    <tr>
                        <th>상세주소</th>
                        <td>
                            <input type="text" id="addrDetail" name="addrDetail" style="width:40%">
                        </td>
                    </tr>
                    </tbody>
                </table>
                <!-- DB에 저장/전송되는 최종 주소 -->
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

            <!-- 점검표 선택 섹션 추가 -->
            <div class="cell">점검표 선택</div>
            <div class="template-section">
                <button type="button" id="btnAddTemplate" class="btn-add-template">
                    <span class="plus-icon"></span>
                    점검표 추가
                </button>

                <div class="template-list" id="selectedTemplates">
                    <div class="empty-template">
                        선택된 점검표가 없습니다
                    </div>
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
    // 선택된 점검표들을 저장할 배열
    let selectedTemplates = [];

    function goPopup(){
        var url = "<c:url value='/popup/juso'/>";
        window.open(url, "pop", "width=570,height=420,scrollbars=yes,resizable=yes");
    }

    // juso 콜백
    function jusoCallBack(fullAddress, roadAddrPart1, addrDetail, zipNo) {
        document.getElementById('roadAddrPart1').value = roadAddrPart1 || '';
        document.getElementById('addrDetail').value    = addrDetail    || '';
        document.getElementById('zipNo').value         = zipNo         || '';
        document.getElementById('address').value =
            (fullAddress && fullAddress.trim().length > 0)
                ? fullAddress
                : [roadAddrPart1, addrDetail].filter(Boolean).join(' ');
    }

    // 페이지 최초 진입 시 DB 주소를 보조필드에 풀어넣기(단순 분리)
    (function prefillAddress(){
        var full = document.getElementById('address').value || '';
        if (!full) return;
        // 마지막 공백 뒤를 상세주소로 가정
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

    // 서버에서 전달된 기존 점검표 데이터를 selectedTemplates 배열에 로드
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

    // 점검표 선택 팝업 열기
    function openTemplateSelectPopup() {
        const url = "<c:url value='/template/select'/>?popup=true";
        window.open(url, "templateSelectPop", "width=720,height=640,scrollbars=yes,resizable=yes");
    }

    // 점검표 선택 팝업에서 호출되는 콜백
    function selectTemplateCallback(templateId, templateName, domain) {
        // 이미 선택된 점검표인지 확인
        if (selectedTemplates.find(t => t.id === templateId)) {
            alert('이미 선택된 점검표입니다.');
            return;
        }

        // 새 점검표 추가
        selectedTemplates.push({
            id: templateId,
            name: templateName,
            domain: domain
        });

        updateTemplateDisplay();
    }

    // 새로 등록된 점검표 추가용 콜백
    function addNewTemplateCallback(templateId, templateName, domain) {
        selectedTemplates.push({
            id: templateId,
            name: templateName,
            domain: domain
        });

        updateTemplateDisplay();
    }

    // 점검표 제거
    function removeTemplate(templateId) {
        selectedTemplates = selectedTemplates.filter(t => t.id !== templateId);
        updateTemplateDisplay();
    }

    // 점검표 목록 화면 업데이트
    function updateTemplateDisplay() {
        const container = document.getElementById('selectedTemplates');
        const templateIdsInput = document.getElementById('templateIds');

        if (selectedTemplates.length === 0) {
            container.innerHTML = '<div class="empty-template">선택된 점검표가 없습니다</div>';
            templateIdsInput.value = '';
        } else {
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

            // 선택된 템플릿 ID들을 쉼표로 구분하여 hidden input에 설정
            templateIdsInput.value = selectedTemplates.map(t => t.id).join(',');
        }
    }

    // HTML 이스케이프 함수
    function escapeHtml(text) {
        if (!text) return '';
        const map = {
            '&': '&amp;',
            '<': '&lt;',
            '>': '&gt;',
            '"': '&quot;',
            "'": '&#039;'
        };
        return text.replace(/[&<>"']/g, function(m) { return map[m]; });
    }

    // 도메인 텍스트 변환
    function getDomainText(domain) {
        switch(domain) {
            case 'CLEANING': return '미화';
            case 'FIRE': return '소방';
            case 'PATROL': return '순찰';
            case '청결': return '청결';
            case '소방': return '소방';
            case '순찰': return '순찰';
            default: return domain || '';
        }
    }

    // DOM 로드 완료 후 실행
    document.addEventListener("DOMContentLoaded", function() {
        // 기존 점검표 로드
        loadExistingTemplates();

        // 점검표 추가 버튼 이벤트
        const btnAddTemplate = document.getElementById("btnAddTemplate");
        if (btnAddTemplate) {
            btnAddTemplate.addEventListener("click", function() {
                openTemplateSelectPopup();
            });
        }
    });

    // 페이지 로드 시 URL 파라미터에서 새로 등록된 점검표 정보 확인
    window.onload = function() {
        const urlParams = new URLSearchParams(window.location.search);
        const newTemplateId = urlParams.get('newTemplateId');
        const newTemplateName = urlParams.get('newTemplateName');
        const newTemplateDomain = urlParams.get('newTemplateDomain');

        if (newTemplateId && newTemplateName && newTemplateDomain) {
            addNewTemplateCallback(newTemplateId, decodeURIComponent(newTemplateName), newTemplateDomain);

            // URL에서 파라미터 제거 (브라우저 기록에 남지 않도록)
            const cleanUrl = window.location.pathname;
            history.replaceState({}, document.title, cleanUrl);
        }
    };
</script>
</body>
</html>