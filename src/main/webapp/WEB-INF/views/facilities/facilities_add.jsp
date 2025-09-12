<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>설비 추가</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="<c:url value='/assets/css/facilities_add.css'/>">
</head>
<body>
<div class="page">
    <div class="form-card">
        <form id="facilityForm" action="<c:url value='/facilities'/>" method="post">
            <div class="cell">시설명</div>
            <input type="text" name="name" placeholder="예: 본관 전기실" required>

            <div class="cell">설비 주소</div>
            <div id="addressBlock">
                <table>
                    <colgroup>
                        <col style="width:20%"><col>
                    </colgroup>
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
                <input type="hidden" id="address" name="address" value="">
            </div>

            <div class="cell">도메인</div>
            <label>
                <select name="domain">
                    <option value="청결">청결</option>
                    <option value="순찰">순찰</option>
                    <option value="소방">소방</option>
                </select>
            </label>

            <div class="cell">층</div>
            <div class="row">
                <input type="text" name="floor" placeholder="(예: B2)">
            </div>
            <div class="cell">상세 구역</div>
            <div class="row">
                <input type="text" name="zone"  placeholder="(예: 로비)">
            </div>

            <div class="cell">점검자 ID</div>
            <div class="row">
                <input type="text" name="inspectorId" placeholder="(예: 1)">
            </div>

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
                <button type="submit" class="btn btn-brand">등록</button>
                <a class="btn" href="<c:url value='/facilities'/>">취소</a>
            </div>
        </form>
    </div>
</div>

<script>
    // 선택된 점검표들을 저장할 배열
    let selectedTemplates = [];

    // 팝업 열기 (컨텍스트 패스 자동 포함)
    function goPopup(){
        var url = "<c:url value='/popup/juso'/>";
        window.open(url, "pop", "width=570,height=420,scrollbars=yes,resizable=yes");
    }

    // juso 팝업에서 선택 후 호출되는 콜백
    function jusoCallBack(fullAddress, roadAddrPart1, addrDetail) {
        document.getElementById('roadAddrPart1').value = roadAddrPart1 || '';
        document.getElementById('addrDetail').value    = addrDetail    || '';

        // 팝업에서 합친 주소를 그대로 저장 (fallback로 직접 합치기)
        document.getElementById('address').value =
            (fullAddress && fullAddress.trim().length > 0)
                ? fullAddress
                : [roadAddrPart1, addrDetail].filter(Boolean).join(' ');
    }

    document.addEventListener("DOMContentLoaded", function() {
        const btn = document.getElementById("btnAddTemplate");
        if (btn) btn.addEventListener("click", function() {
            const url = "<c:url value='/template/select'/>?popup=true"; // ← 여기로 통일
            window.open(url, "templateSelectPop", "width=720,height=640,scrollbars=yes,resizable=yes");
        });
    });


    function openTemplatePopup() {
        var returnUrl = encodeURIComponent(window.location.href);
        var url = "<c:url value='/template/insert'/>?returnUrl=" + returnUrl + "&popup=true";
        window.open(url, "templatePop", "width=800,height=600,scrollbars=yes,resizable=yes");
    }

    function openFacilityAddPopup() {
        const returnUrl = encodeURIComponent(window.location.href);
        const url = "<c:url value='/facilities/new'/>?popup=true&returnUrl=" + returnUrl;
        window.open(url, "facAddPop", "width=900,height=700,scrollbars=yes,resizable=yes");
    }


    // 기존 점검표 선택 팝업 열기
    function openTemplateSelectPopup() {
        const url = "<c:url value='/template/select'/>?popup=true";
        window.open(url, "templateSelectPop",
            "width=720,height=640,scrollbars=yes,resizable=yes");
    }

    // 점검표 선택 팝업에서 호출되는 콜백 (기존 점검표 선택용)
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
                    '<div class="template-name">' + template.name + '</div>' +
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
    }

    // 도메인 텍스트 변환
    function getDomainText(domain) {
        switch(domain) {
            case 'CLEANING': return '미화';
            case 'FIRE': return '소방';
            case 'PATROL': return '순찰';
            default: return domain;
        }
    }

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