// 작성자: 최이서
(function(){
    var wrap     = document.getElementById('status-filter');
    if(!wrap) return;

    var selected = document.getElementById('selected-status');
    var icon     = wrap.querySelector('.icon');
    var list     = document.getElementById('status-dropdown');
    var hidden   = document.getElementById('status');

    // 초기 표시(파라미터 유지)
    var labelMap = {'':'전체 상태','PASS':'완료','FAIL':'이상 발견'};
    selected.textContent = labelMap[hidden.value || ''] || '전체 상태';
    setActive(hidden.value || '');

    // 열기/닫기
    wrap.addEventListener('click', function(e){
        e.stopPropagation();
        list.classList.toggle('hidden');
    });

    // 항목 선택
    list.addEventListener('click', function(e){
        var li = e.target.closest('li'); if(!li) return;
        var val = li.dataset.value || '';
        hidden.value = val;
        selected.textContent = labelMap[val] || li.textContent.trim();
        setActive(val);
        list.classList.add('hidden');
    });

    // 바깥 클릭 닫기
        document.addEventListener('click', function(){
        list.classList.add('hidden');
    });

    function setActive(val){
        list.querySelectorAll('li').forEach(function(li){
        li.classList.toggle('active', (li.dataset.value||'') === val);
    });
}})();
