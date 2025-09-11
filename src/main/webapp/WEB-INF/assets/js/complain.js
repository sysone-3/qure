(function(){
  const frm = document.getElementById('frm');
  const title = document.getElementById('title');
  const email = document.getElementById('email');
  const desc = document.getElementById('desc');
  const btn = document.getElementById('btnSubmit');
  const errTitle = document.getElementById('errTitle');
  const errEmail = document.getElementById('errEmail');
  const errDesc = document.getElementById('errDesc');

  const EMAIL_MAX = 254;
  const emailRe = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/i;

  function t(v){ return (v || '').replace(/\s+/g,' ').trim(); }

  function validate(){
    errTitle.textContent = '';
    errEmail.textContent = '';
    errDesc.textContent = '';

    let ok = true;

    // 제목 필수
    if(t(title.value).length === 0){
      errTitle.textContent = '제목을 입력하세요.';
      ok = false;
    }

    // 이메일 선택 + 형식 검증
    const ev = t(email.value);
    if(ev.length > 0){
      if(ev.length > EMAIL_MAX){
        errEmail.textContent = '이메일 길이가 너무 깁니다.';
        ok = false;
      }else if(!emailRe.test(ev)){
        errEmail.textContent = '이메일 형식을 확인하세요.';
        ok = false;
      }
    }

    // 상세내용 필수
    if(t(desc.value).length === 0){
      errDesc.textContent = '상세내용을 입력하세요.';
      ok = false;
    }

    btn.disabled = !ok;
    return ok;
  }

  // textarea 자동 높이
  function autosize(){
    desc.style.height = 'auto';
    desc.style.height = Math.min(desc.scrollHeight, 800) + 'px';
  }

  ['input','change'].forEach(ev=>{
    title.addEventListener(ev, validate);
    email.addEventListener(ev, validate);
    desc.addEventListener(ev, ()=>{ validate(); autosize(); });
  });

  frm.addEventListener('submit', function(e){
    if(!validate()){ e.preventDefault(); return; }
    btn.disabled = true; // 중복 제출 방지
  });

  // 초기 상태
  autosize();
  validate();
})();
