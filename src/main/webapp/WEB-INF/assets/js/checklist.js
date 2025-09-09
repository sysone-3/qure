
(function () {
  const form     = document.getElementById('chkForm');
  const btn      = document.getElementById('btnSubmit');
  const doneCnt  = document.getElementById('doneCnt');   // 채워진 필수 개수
  const totalCnt = document.getElementById('totalCnt');  // 전체 필수 개수
  const cards    = Array.from(document.querySelectorAll('.card'));

  function isFilled(card){
    const t = card.dataset.type;
    if (t === 'BOOL') return !!card.querySelector('input[type=radio]:checked');
    if (t === 'NUMBER')  { const el = card.querySelector('input[type=number]'); return !!(el && el.value !== ''); }
    if (t === 'TEXT')    { const el = card.querySelector('textarea');           return !!(el && el.value.trim().length > 0); }
    if (t === 'PHOTO')   { const el = card.querySelector('input[type=file]');   return !!(el && el.files && el.files.length > 0); }
    return false;
  }

  function calcDone(){
    let reqTotal = 0;
    let reqFilled = 0;

    cards.forEach(card => {
      const err = card.querySelector('.err');
      if (err) err.textContent = '';

      const required = card.dataset.required === 'true';
      if (required) {
        reqTotal++;
        if (isFilled(card)) reqFilled++;
      }
    });

    if (doneCnt)  doneCnt.textContent  = String(reqFilled);
    if (totalCnt) totalCnt.textContent = String(reqTotal);

    btn.disabled = (reqFilled !== reqTotal);
  }

  if (form){
    form.addEventListener('input',  calcDone);
    form.addEventListener('change', calcDone);
  }
  calcDone();

  if (btn){
    btn.addEventListener('click', function(e){
      if (btn.disabled){
        e.preventDefault();
        return;
      }

      // 최종 검증 시 에러 메시지 표시
      let hasErr = false;
      cards.forEach(card => {
        if (card.dataset.required === 'true' && !isFilled(card)){
          const err = card.querySelector('.err');
          if (err) err.textContent = '필수 항목입니다.';
          hasErr = true;
        }
      });
      if (hasErr){
        e.preventDefault();
        return;
      }

      btn.disabled = true;
      btn.textContent = '제출 중...';
      form.submit();
    });
  }
})();
