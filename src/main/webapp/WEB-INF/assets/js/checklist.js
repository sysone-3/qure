// /assets/js/checklist.js
(function () {
  const form     = document.getElementById('chkForm');
  const btn      = document.getElementById('btnSubmit');
  const doneCnt  = document.getElementById('doneCnt');   // 채워진 "필수" 개수
  const totalCnt = document.getElementById('totalCnt');  // 전체 "필수" 개수
  const cards    = Array.from(document.querySelectorAll('.card'));

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

    if (btn) btn.disabled = (reqFilled !== reqTotal);
  }

  // 초기화 및 변경 감지
  if (form){
    form.addEventListener('input',  calcDone, { passive: true });
    form.addEventListener('change', calcDone, { passive: true }); // radio/file 대응
  }
  calcDone();

  // 제출 처리
  if (btn){
    btn.addEventListener('click', function(e){
      if (btn.disabled){
        e.preventDefault();
        return;
      }

      let hasErr = false, firstBad = null;
      cards.forEach(card => {
        if (isRequired(card) && !isFilled(card)){
          const err = card.querySelector('.err');
          if (err) err.textContent = '필수 항목입니다.';
          if (!firstBad) firstBad = card;
          hasErr = true;
        }
      });

      if (hasErr){
        e.preventDefault();
        if (firstBad && typeof firstBad.scrollIntoView === 'function') {
          firstBad.scrollIntoView({ behavior: 'smooth', block: 'center' });
        }
        return;
      }

      btn.disabled = true;
      btn.textContent = '제출 중...';
      form.submit();
    });
  }
})();
