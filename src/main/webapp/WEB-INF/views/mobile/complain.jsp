<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, viewport-fit=cover">
<title>민원 신고</title>
<style>
  :root{ --brand:#9EDBCF; --line:#E5EBEF; --muted:#68707A; --danger:#E53935; }
  body{font-family:"Noto Sans KR",system-ui; margin:0; color:#1B1D1F;}
  .wrap{max-width:720px; margin:0 auto; padding:16px 16px 96px;}
  h1{font-size:20px; margin:8px 0;}
  .sub{color:var(--muted); font-size:13px; margin-bottom:8px;}
  .card{border:1px solid var(--line); border-radius:16px; padding:14px; margin:12px 0;}
  .row{display:flex; gap:8px; flex-wrap:wrap; align-items:center}
  label.l{display:block; font-weight:700; margin:6px 0}
  input[type=text], input[type=datetime-local], select, textarea{
    width:100%; border:1px solid var(--line); border-radius:12px; padding:10px 12px; font-size:15px;
  }
  textarea{min-height:120px; resize:vertical}
  .hint{font-size:12px; color:var(--muted)}
  .req{color:var(--danger); margin-left:4px}
  .dock{position:fixed; left:0; right:0; bottom:0; background:#fff; border-top:1px solid var(--line)}
  .dock-inner{max-width:720px; margin:0 auto; padding:10px 16px; display:flex; gap:10px}
  .btn{border:0; border-radius:14px; padding:12px 16px; font-weight:800;}
  .btn-primary{background:var(--brand); color:#083B33; margin-left:auto}
</style>
</head>
<body>
<div class="wrap">

  <h1>민원 신고</h1>
  <p class="sub">${facility.name} · ${facility.address}</p>

  <form id="reportForm" action="/citizen/report/submit" method="post" enctype="multipart/form-data" novalidate>
    <!-- 숨김값 -->
    <input type="hidden" name="facilityId" value="${facility.facilityId}">
    <input type="hidden" name="tagId" value="${tagInfo.tagId}">
    <input type="hidden" name="nonce" value="${requestScope.nonce}">
    <c:if test="${not empty _csrf}">
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    </c:if>
    <input type="hidden" name="gpsLat" id="gpsLat">
    <input type="hidden" name="gpsLng" id="gpsLng">

    <!-- 사건 요약 -->
    <div class="card">
      <label class="l">유형<span class="req">*</span></label>
      <select name="categoryCode" required>
        <option value="">선택하세요</option>
        <option value="DAMAGE">파손</option>
        <option value="LEAK">누수/악취</option>
        <option value="SAFETY">안전위험</option>
        <option value="FIRE">소방설비</option>
        <option value="ILLEGAL">불법투기</option>
        <option value="OTHER">기타</option>
      </select>

      <label class="l">심각도<span class="req">*</span></label>
      <select name="severity" required>
        <option value="">선택하세요</option>
        <option value="LOW">낮음</option>
        <option value="MEDIUM">보통</option>
        <option value="HIGH">높음</option>
        <option value="URGENT">긴급</option>
      </select>

      <label class="l">한 줄 요약</label>
      <input type="text" name="title" maxlength="60" placeholder="예: 남자화장실 세면대 파손">
    </div>

    <!-- 상세 내용 -->
    <div class="card">
      <label class="l">상세 내용<span class="req">*</span></label>
      <textarea name="content" required placeholder="무엇이, 어디서, 언제, 어떻게 발생했는지 적어주세요."></textarea>

      <label class="l">발생 시각</label>
      <input type="datetime-local" name="occurredAt">

      <label class="l">현장 사진</label>
      <input type="file" name="photos" accept="image/*" capture="environment" multiple>
      <div class="hint">최대 5장, 장당 10MB 이하 권장</div>
    </div>

    <!-- 위치/구역 -->
    <div class="card">
      <div class="row">
        <div style="flex:1">
          <label class="l">층</label>
          <input type="text" name="floor" value="${tagInfo.floor}" placeholder="예: 7층">
        </div>
        <div style="flex:2">
          <label class="l">구역/호수</label>
          <input type="text" name="roomOrArea" value="${tagInfo.roomOrArea}" placeholder="예: 화장실">
        </div>
      </div>
      <div class="hint">브라우저 위치 권한 허용 시 GPS 자동 저장</div>
    </div>

    <!-- 신고자 정보 -->
    <div class="card">
      <label class="l">익명 신고</label>
      <div class="row">
        <label><input type="radio" name="isAnonymous" value="true" checked> 예</label>
        <label><input type="radio" name="isAnonymous" value="false"> 아니오 (연락처 제공)</label>
      </div>

      <div id="contactBox" style="display:none; margin-top:8px">
        <label class="l">이름</label>
        <input type="text" name="reporterName" maxlength="30">
        <label class="l">연락처(휴대폰)</label>
        <input type="text" name="contactPhone" maxlength="20" placeholder="숫자만">
        <label class="l">이메일</label>
        <input type="text" name="contactEmail" maxlength="60">
        <label class="l">회신 방법</label>
        <select name="preferredContact">
          <option value="">선택 없음</option>
          <option value="CALL">전화</option>
          <option value="SMS">문자</option>
          <option value="EMAIL">이메일</option>
        </select>
      </div>
    </div>

    <!-- 동의 -->
    <div class="card">
      <label><input type="checkbox" name="agreePrivacy" required> (필수) 개인정보 수집·이용에 동의합니다.</label>
    </div>
  </form>
</div>

<div class="dock">
  <div class="dock-inner">
    <button class="btn btn-primary" form="reportForm">민원 제출</button>
  </div>
</div>

<script>
// 익명 여부에 따라 연락처 영역 토글
const radios = document.querySelectorAll('input[name=isAnonymous]');
const box = document.getElementById('contactBox');
radios.forEach(r=>r.addEventListener('change', ()=>{
  box.style.display = (document.querySelector('input[name=isAnonymous]:checked').value==='false') ? 'block':'none';
}));

// GPS 시도(권한 허용시 저장)
if(navigator.geolocation){
  navigator.geolocation.getCurrentPosition(pos=>{
    document.getElementById('gpsLat').value = pos.coords.latitude.toFixed(7);
    document.getElementById('gpsLng').value = pos.coords.longitude.toFixed(7);
  }, ()=>{}, {enableHighAccuracy:true, timeout:5000, maximumAge:60000});
}
</script>
</body>
</html>
