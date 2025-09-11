<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>도로명주소 검색</title>
    <%
        String inputYn       = request.getParameter("inputYn");
        String roadFullAddr  = request.getParameter("roadFullAddr");
        String roadAddrPart1 = request.getParameter("roadAddrPart1");
        String zipNo         = request.getParameter("zipNo");
        String addrDetail    = request.getParameter("addrDetail");

    %>
    <script>
        // 필요 시 도메인 설정(양쪽 동일하게)
        // document.domain = "example.com";

        function init(){
            var inputYn = "<%= (inputYn == null ? "" : inputYn) %>";
            if(inputYn !== "Y"){
                // 1) 최초 진입: JUSO 검색 페이지로 POST 제출
                var f = document.getElementById('jusoForm');
                f.confmKey.value  = "devU01TX0FVVEgyMDI1MDkwOTEwMzUxNjExNjE2MTk="; // !!반드시 본인 승인키로 교체!!
// 절대경로로 (내 서버) 세팅
                f.returnUrl.value = "<%= request.getScheme() %>://<%= request.getServerName() %>:<%= request.getServerPort() %><%= request.getContextPath() %>/popup/juso";
                f.resultType.value = "4";
                f.action = "https://business.juso.go.kr/addrlink/addrLinkUrl.do";
                f.submit();
            } else {
                // 2) 검색 후 복귀: 부모 창에 값 전달
                if(window.opener && typeof window.opener.jusoCallBack === 'function'){
                    var fullAddress = "<%= request.getParameter("roadAddrPart1") %>" + " "
                        + "<%= request.getParameter("addrDetail") %>";

                    window.opener.jusoCallBack(
                        fullAddress, // 합쳐진 주소
                        "<%= request.getParameter("roadAddrPart1") %>",
                        "<%= request.getParameter("addrDetail")    %>",
                        "<%= request.getParameter("zipNo")         %>"
                        // 필요 없는 값들은 생략 가능
                    );
                }
                window.close();
            }
        }
    </script>
</head>
<body onload="init()">
<form id="jusoForm" method="post">
    <input type="hidden" id="confmKey"   name="confmKey"   value="">
    <input type="hidden" id="returnUrl"  name="returnUrl"  value="">
    <input type="hidden" id="resultType" name="resultType" value="">
    <!-- EUC-KR 사용 환경이면 아래 주석 해제
    <input type="hidden" id="encodingType" name="encodingType" value="EUC-KR">
    -->
</form>
</body>
</html>
