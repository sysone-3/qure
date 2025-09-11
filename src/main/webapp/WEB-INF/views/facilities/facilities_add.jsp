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

                <div class="form-actions">
                    <button type="submit" class="btn btn-brand">등록</button>
                    <a class="btn" href="<c:url value='/facilities'/>">취소</a>
                </div>
            </form>
        </div>
    </div>

    <script>
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

    </script>
    </body>
</html>
