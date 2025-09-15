<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>설비 수정</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 기본 레이아웃 CSS 추가 -->
    <link rel="stylesheet" href="<c:url value='/assets/css/navigation.css'/>" />
    <link rel="stylesheet" href="<c:url value='/assets/css/components.css'/>" />
    <!-- facilities 전용 CSS -->
    <link rel="stylesheet" href="<c:url value='/assets/css/facilities_add.css'/>">
</head>
<body>
<!-- nav 변수 설정 (사이드바에서 '설비' 메뉴 활성화) -->
<c:set var="nav" value="facilities" scope="request"/>

<div class="layout">
    <!-- 사이드바 포함 -->
    <%@ include file="../fragments/sidebar.jspf" %>

    <!-- 메인 콘텐츠 -->
    <main class="content">
        <div class="page">
        <div class="form-card">
        <!-- 타이틀  -->
        <div class="title-row">
            <h2 class="form-title">설비 수정</h2>
        </div>

        <form id="facilityForm" action="<c:url value='/facilities/${facility.facilityId}'/>" method="post">
            <sec:csrfInput/>
            <!-- PK hidden -->
            <input type="hidden" name="facilityId" value="${facility.facilityId}"/>

            <!-- 시설명 -->
            <div class="cell">시설명</div>
            <div class="input-wrapper">
                <input type="text" name="name" class="text-input"
                       value="${facility.name}" placeholder="예: 본관 전기실" required>
            </div>

            <!-- 설비 주소 (그리드 레이아웃) -->
            <div class="form-box">
                <div class="address-header">
                    <label class="label">주소</label>
                    <button type="button" class="btn-inline-action" onclick="goPopup()">주소검색</button>
                </div>

                <div id="addressBlock" class="address-grid">
                    <div class="address-row">
                        <label class="label" for="roadAddrPart1">도로명주소</label>
                        <input type="text" id="roadAddrPart1" name="roadAddrPart1"
                               class="text-input" placeholder="도로명주소" readonly>
                    </div>


                    <div class="address-row">
                        <label class="label" for="addrDetail">상세주소</label>
                        <div class="field">
                            <input type="text" id="addrDetail" name="addrDetail" placeholder="상세주소"  class="text-input">
                        </div>
                    </div>
                </div>

                <!-- 서버 전송용 합쳐진 주소 -->
                <input type="hidden" id="address" name="address"
                       value="<c:out value='${facility.address}'/>">
            </div>

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

            <!-- 점검표 선택 (단일 선택으로 수정) -->
            <div class="cell">점검표 선택</div>
            <div class="template-section">
                <button type="button" id="btnAddTemplate" class="btn-add-template">
                    <span class="add-plus">+</span> 점검표 선택
                </button>

                <div class="template-list" id="selectedTemplates">
                    <div class="empty-template">점검표를 선택해주세요</div>
                </div>

                <!-- 선택된 점검표 ID (기존 구조 유지하되 단일 값만) -->
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
    </main>
</div>

<script>
    // ===== 주소검색 팝업 =====
    function goPopup(){
        var url = "<c:url value='/popup/juso'/>";
        window.open(url, "pop", "width=570,height=420,scrollbars=yes,resizable=yes");
    }

    // juso 콜백 - 완전히 새로운 주소로 덮어쓰기
    function jusoCallBack(fullAddress, roadAddrPart1, addrDetail) {
        console.log('주소 검색 결과:', { fullAddress, roadAddrPart1, addrDetail });

        // 모든 주소 필드를 새로운 값으로 완전히 덮어쓰기
        document.getElementById('roadAddrPart1').value = roadAddrPart1 || '';
        document.getElementById('addrDetail').value = addrDetail || '';


        // hidden address 필드도 새로운 전체 주소로 완전히 교체
        let finalAddress = '';
        if (fullAddress) {
            finalAddress = fullAddress;
        } else {
            // fullAddress가 없으면 도로명 + 상세주소 조합
            const parts = [];
            if (roadAddrPart1) parts.push(roadAddrPart1);
            if (addrDetail) parts.push(addrDetail);
            finalAddress = parts.join(' ');
        }

        document.getElementById('address').value = finalAddress;
        console.log('최종 설정된 주소:', finalAddress);
    }

    // ===== 주소 프리필 (수정 페이지용) =====
    document.addEventListener('DOMContentLoaded', function () {
        const addrFullEl = document.getElementById('address');
        const roadEl = document.getElementById('roadAddrPart1');
        const detailEl = document.getElementById('addrDetail');
        const zipEl = document.getElementById('zipNo');

        if (!addrFullEl || !roadEl || !detailEl) {
            console.warn('[prefill] 필요한 엘리먼트를 못 찾았습니다.');
            return;
        }

        const fullAddress = (addrFullEl.value || '').trim();
        console.log('페이지 로드 시 전체 주소:', fullAddress);

        if (!fullAddress) {
            return; // 주소가 없으면 프리필 안함
        }

        // 기존에 이미 분리된 값들이 있는지 확인
        const existingRoad = roadEl.value?.trim();
        const existingDetail = detailEl.value?.trim();

        // 이미 적절히 분리되어 있다면 프리필 건너뜀
        if (existingRoad && existingRoad !== fullAddress) {
            console.log('이미 분리된 주소가 있음, 프리필 건너뜀');
            return;
        }

        // 전체 주소를 도로명주소와 상세주소로 분리 시도
        let roadAddress = fullAddress;
        let detailAddress = '';

        // 패턴 1: "주소, 상세" 형태
        if (fullAddress.includes(',')) {
            const parts = fullAddress.split(',');
            roadAddress = parts[0].trim();
            detailAddress = parts.slice(1).join(',').trim();
        }
        // 패턴 2: "주소 (상세)" 형태
        else {
            const parenMatch = fullAddress.match(/^(.*?)\s*\((.+)\)\s*$/);
            if (parenMatch) {
                roadAddress = parenMatch[1].trim();
                detailAddress = parenMatch[2].trim();
            }
            // 패턴 3: 마지막이 숫자면 상세주소로 분리
            else {
                const lastSpaceIndex = fullAddress.lastIndexOf(' ');
                if (lastSpaceIndex > 0) {
                    const lastPart = fullAddress.substring(lastSpaceIndex + 1).trim();
                    // 숫자, 숫자-숫자, 숫자층, 숫자호 등의 패턴
                    if (lastPart.match(/^\d+(-\d+)?[층호]?$/)) {
                        roadAddress = fullAddress.substring(0, lastSpaceIndex).trim();
                        detailAddress = lastPart;
                    }
                }
            }
        }

        // 분리된 주소로 필드 설정
        roadEl.value = roadAddress;
        detailEl.value = detailAddress;

        // hidden 필드는 원본 전체 주소 유지
        addrFullEl.value = fullAddress;

        console.log('주소 프리필 완료:', {
            원본: fullAddress,
            도로명: roadAddress,
            상세: detailAddress
        });
    });

    // ===== 폼 제출 전 주소 최종 정리 =====
    // DOMContentLoaded 이후에 이벤트 리스너 등록
    document.addEventListener('DOMContentLoaded', function() {
        const form = document.getElementById('facilityForm');
        if (form) {
            form.addEventListener('submit', function(e) {
                const roadEl = document.getElementById('roadAddrPart1');
                const detailEl = document.getElementById('addrDetail');
                const addressEl = document.getElementById('address');

                if (roadEl && detailEl && addressEl) {
                    // 현재 입력된 값들로 최종 주소 조합
                    const roadValue = roadEl.value?.trim() || '';
                    const detailValue = detailEl.value?.trim() || '';

                    const finalAddress = [roadValue, detailValue].filter(Boolean).join(' ');
                    addressEl.value = finalAddress;

                    console.log('폼 제출 - 최종 주소:', {
                        도로명: roadValue,
                        상세: detailValue,
                        최종: finalAddress
                    });
                }
            });
        }

        // 사용자가 직접 입력 필드를 수정할 때도 hidden 필드 업데이트
        const roadEl = document.getElementById('roadAddrPart1');
        const detailEl = document.getElementById('addrDetail');
        const addressEl = document.getElementById('address');

        if (roadEl && detailEl && addressEl) {
            function updateHiddenAddress() {
                const roadValue = roadEl.value?.trim() || '';
                const detailValue = detailEl.value?.trim() || '';
                const combined = [roadValue, detailValue].filter(Boolean).join(' ');
                addressEl.value = combined;
                console.log('주소 필드 업데이트:', combined);
            }

            roadEl.addEventListener('blur', updateHiddenAddress);
            detailEl.addEventListener('blur', updateHiddenAddress);
        }
    });

    // ===== 점검표 선택 관리 (변경 추적 포함) =====
    let selectedTemplates = []; // 현재 선택된 점검표들
    let originalTemplateIds = []; // 페이지 로드 시 원본 점검표 ID들 (변경 추적용)

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

        // 원본 ID 목록 저장 (변경 추적용)
        originalTemplateIds = selectedTemplates.map(t => t.id);
        console.log('원본 점검표 IDs:', originalTemplateIds);

        updateTemplateDisplay();
    }

    function openTemplateSelectPopup() {
        const url = "<c:url value='/template/select'/>?popup=true";
        const popup = window.open(url, "templateSelectPop", "width=720,height=640,scrollbars=yes,resizable=yes");

        const checkClosed = setInterval(function() {
            if (popup.closed) {
                clearInterval(checkClosed);
                console.log('Popup closed, current selectedTemplates:', selectedTemplates);
                updateChangeTracking(); // 변경사항 추적 업데이트
            }
        }, 1000);
    }

    // 팝업에서 점검표 선택/추가 시 호출
    function selectTemplateCallback(templateId, templateName, domain) {
        // 단일 선택으로 전체 교체
        selectedTemplates = [{ id: templateId, name: templateName, domain }];
        updateTemplateDisplay();
        updateChangeTracking();
    }

    function addNewTemplateCallback(templateId, templateName, domain) {
        selectedTemplates = [{ id: templateId, name: templateName, domain }];
        updateTemplateDisplay();
        updateChangeTracking();
    }

    function removeTemplate(templateId) {
        selectedTemplates = selectedTemplates.filter(t => t.id !== templateId);
        updateTemplateDisplay();
        updateChangeTracking();
    }

    function updateTemplateDisplay() {
        const container = document.getElementById('selectedTemplates');
        const templateIdsInput = document.getElementById('templateIds');
        const btnAddTemplate = document.getElementById('btnAddTemplate');

        if (selectedTemplates.length === 0) {
            container.innerHTML = '<div class="empty-template">점검표를 선택해주세요</div>';
            templateIdsInput.value = '';
            btnAddTemplate.innerHTML = '<span class="add-plus">+</span> 점검표 선택';
            return;
        }

        container.innerHTML = '';
        selectedTemplates.forEach(template => {
            const templateItem = document.createElement('div');
            templateItem.className = 'template-item';

            const templateInfo = document.createElement('div');
            templateInfo.className = 'template-info';

            const templateName = document.createElement('div');
            templateName.className = 'template-name';
            templateName.textContent = template.name;

            const templateDomain = document.createElement('div');
            templateDomain.className = 'template-domain';
            templateDomain.textContent = getDomainText(template.domain);

            const templateActions = document.createElement('div');
            templateActions.className = 'template-actions';

            const removeButton = document.createElement('button');
            removeButton.type = 'button';
            removeButton.className = 'btn-remove';
            removeButton.textContent = '삭제';
            removeButton.onclick = () => removeTemplate(template.id);

            templateInfo.appendChild(templateName);
            templateInfo.appendChild(templateDomain);
            templateActions.appendChild(removeButton);
            templateItem.appendChild(templateInfo);
            templateItem.appendChild(templateActions);
            container.appendChild(templateItem);
        });

        templateIdsInput.value = selectedTemplates.map(t => t.id).join(',');
        btnAddTemplate.innerHTML = '<span class="add-plus">+</span> 점검표 변경';
    }

    // 변경사항 추적 및 hidden input 업데이트
    function updateChangeTracking() {
        const currentIds = selectedTemplates.map(t => t.id);

        // 삭제된 것들: 원본에는 있었지만 현재에는 없는 것
        const removedIds = originalTemplateIds.filter(id => !currentIds.includes(id));

        // 추가된 것들: 현재에는 있지만 원본에는 없는 것
        const addedIds = currentIds.filter(id => !originalTemplateIds.includes(id));

        console.log('변경사항 추적:', {
            original: originalTemplateIds,
            current: currentIds,
            removed: removedIds,
            added: addedIds
        });

        // hidden input 업데이트
        updateHiddenInputs(removedIds, addedIds);
    }

    function updateHiddenInputs(removedIds, addedIds) {
        // 기존 hidden input 제거
        removeExistingHiddenInputs();

        // 새로운 hidden input 추가
        const form = document.getElementById('facilityForm');

        // 삭제된 템플릿 IDs
        if (removedIds.length > 0) {
            const removedInput = document.createElement('input');
            removedInput.type = 'hidden';
            removedInput.name = 'removedTemplateIds';
            removedInput.value = removedIds.join(',');
            form.appendChild(removedInput);
        }

        // 추가된 템플릿 IDs
        if (addedIds.length > 0) {
            const addedInput = document.createElement('input');
            addedInput.type = 'hidden';
            addedInput.name = 'addedTemplateIds';
            addedInput.value = addedIds.join(',');
            form.appendChild(addedInput);
        }
    }

    function removeExistingHiddenInputs() {
        const form = document.getElementById('facilityForm');
        const existingRemoved = form.querySelector('input[name="removedTemplateIds"]');
        const existingAdded = form.querySelector('input[name="addedTemplateIds"]');

        if (existingRemoved) existingRemoved.remove();
        if (existingAdded) existingAdded.remove();
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