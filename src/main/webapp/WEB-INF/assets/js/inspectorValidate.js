(function(){
  const inputs=[...document.querySelectorAll('.pin input')];
  const pad=document.querySelector('.pad');
  const submit=document.getElementById('submitBtn');
  const pinHidden=document.getElementById('pinValue');
  const err=document.getElementById('err');
  const form=document.getElementById('pinForm');

  // 키보드 방지 전략:
  // - input은 readonly + inputmode=none
  // - 실제 포커스 대신 .active 클래스로 강조만 표시
  let idx = 0; // 현재 채울 칸 인덱스(0~3)

  function setActive(i){
    idx = Math.max(0, Math.min(3, i));
    inputs.forEach(el=>el.classList.remove('active'));
    inputs[idx].classList.add('active');
  }

  // 입력칸 탭 시 해당 칸을 활성화(키보드는 뜨지 않음)
  inputs.forEach((el,i)=>{
    el.addEventListener('pointerdown', (e)=>{ e.preventDefault(); setActive(i); });
  });
  setActive(0);

  // 키패드 클릭
  pad.addEventListener('click',e=>{
    if(!e.target.classList.contains('key')) return;

    if(e.target.id === 'clear'){
      inputs.forEach(i=>i.value='');
      setActive(0);
      sync();
      return;
    }
    if(e.target.id === 'back'){
      backspace();
      return;
    }

    const val=e.target.textContent.trim();
    if(/^\d$/.test(val)) putDigit(val);
  });

  function putDigit(d) {
    inputs[idx].value = d;
    idx = Math.min(idx + 1, 3);
    setActive(idx);
    sync();
  }

  function backspace() {
    if (idx > 0 && !inputs[idx].value) {
      idx--;
    }
	
    inputs[idx].value = '';
    setActive(idx);
    sync();
  }

  function sync(){
    const val=inputs.map(i=>i.value).join('');
    pinHidden.value=val;
    const ok=/^\d{4}$/.test(val);
    submit.disabled=!ok;
  }

  // 제출(프론트에선 4자리 완성만 확인)
  form.addEventListener('submit',(e)=>{
    const pin=pinHidden.value;
    if(!/^\d{4}$/.test(pin)){
      e.preventDefault();
      return;
    }
    submit.disabled=true;
  });
})();
