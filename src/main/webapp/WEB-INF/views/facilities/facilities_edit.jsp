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

            <div class="form-actions">
                <button type="submit" class="btn btn-brand">저장</button>
                <a class="btn" href="<c:url value='/facilities/${facility.facilityId}'/>">취소</a>
            </div>
        </form>
    </div>
</div>

<script>
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
</script>
</body>
</html>
