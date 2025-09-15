(function () {
  const form     = document.getElementById('chkForm');
  const btn      = document.getElementById('btnSubmit');
  const doneCnt  = document.getElementById('doneCnt');   // 채워진 "필수" 개수
  const totalCnt = document.getElementById('totalCnt');  // 전체 "필수" 개수
  const cards    = Array.from(document.querySelectorAll('.card'));

  // 기본 방어. 필수 요소 없으면 종료
  if (!form || !btn) return;

  // GPS 정보
  var latEl = form.querySelector('[name=submitLat]');
  var lngEl = form.querySelector('[name=submitLng]');

  // GPS 충족 여부 헬퍼
  const gpsOk = () => !!(latEl && lngEl && latEl.value && lngEl.value);

  // 위도,경도 채워져야만 제출 버튼 활성화
  // 별도 함수 제거하고 calcDone에서 GPS와 함께 판단하도록 변경.
  btn.disabled = true; // 초기 비활성

  navigator.geolocation.getCurrentPosition(function(p){ // 현재위치 받아옴
    latEl.value = p.coords.latitude; // 받아온 위도 경도 채움
    lngEl.value = p.coords.longitude;
    calcDone(); // 버튼 활성화 판단을 calcDone으로 위임
  }, function(){ // 위치 확인 안될 시 메세지
    alert('현장 위치 확인이 필요합니다. 위치 권한을 허용해 주세요.');
    calcDone(); // 실패 시에도 상태 재평가
  }, {enableHighAccuracy:true, timeout:8000, maximumAge:0});

  function isRequired(card){
    const v = (card.dataset.required || '').toString().trim().toLowerCase(); // '1'/'0' or 'true'/'false'
    return v === '1' || v === 'true' || v === 'y';
  }

  function isFilled(card){
    const t = (card.dataset.type || '').toUpperCase(); // 'BOOL' | 'NUM' | 'TEXT' | 'IMAGE'
    switch (t) {
      case 'BOOL': {
        return !!card.querySelector('input[type=radio]:checked');
      }
      case 'NUM': {
        const el = card.querySelector('input[type=number]');
        return !!(el && el.value !== '');
      }
      case 'TEXT': {
        const el = card.querySelector('textarea');
        return !!(el && el.value.trim().length > 0);
      }
      case 'IMAGE': {
        const el = card.querySelector('input[type=file]');
        return !!(el && el.files && el.files.length > 0);
      }
      default:
        return false;
    }
  }

  function clearErrors(){
    cards.forEach(card => {
      const err = card.querySelector('.err');
      if (err) err.textContent = '';
    });
  }

  function calcDone(){
    let reqTotal = 0, reqFilled = 0;

    clearErrors();

    cards.forEach(card => {
      if (isRequired(card)) {
        reqTotal++;
        if (isFilled(card)) reqFilled++;
      }
    });

    if (doneCnt)  doneCnt.textContent  = String(reqFilled);
    if (totalCnt) totalCnt.textContent = String(reqTotal);

    // GPS가 있고 필수항목이 모두 채워져야만 활성화
    btn.disabled = !(gpsOk() && reqFilled === reqTotal);
  }

  // 초기화 및 변경 감지
  if (form){
    form.addEventListener('input',  calcDone, { passive: true });
    form.addEventListener('change', calcDone, { passive: true }); // radio/file 대응
  }
  calcDone();

  // 제출 처리
  if (btn){
	btn.addEventListener('click', function (e) {
	  e.preventDefault();
	  if (btn.disabled) return;

	  btn.disabled = true;
	  btn.textContent = '위치 확보 중...';

	  navigator.geolocation.getCurrentPosition(function (p) {
	    var acc = p.coords.accuracy || 9999; // m
	    // 정확도 2000m 이내만 허용. 벗어나면 거부.
	    if (acc > 2000) {
	      btn.disabled = false;
	      btn.textContent = '점검 결과 제출';
	      alert('위치 정확도가 낮습니다(약 ' + Math.round(acc) + 'm). 실외에서 다시 시도하세요.');
	      return;
	    }
	    latEl.value = p.coords.latitude;
	    lngEl.value = p.coords.longitude;
	    var accEl = form.querySelector('[name=gpsAcc]');
	    if (accEl) accEl.value = acc;

	    btn.textContent = '제출 중...';
	    form.submit();
	  }, function () {
	    btn.disabled = false;
	    btn.textContent = '점검 결과 제출';
	    alert('현재 위치를 가져오지 못했습니다.');
	  }, { enableHighAccuracy: true, timeout: 10000, maximumAge: 0 });
	});
  }
})();
