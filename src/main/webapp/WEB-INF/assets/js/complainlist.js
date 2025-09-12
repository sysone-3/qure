// complainlist.js
document.addEventListener('DOMContentLoaded', function () {
  // --- CSRF 헬퍼 ---
  function withCsrf(headers) {
    var tEl = document.querySelector('meta[name="_csrf"]');
    var hEl = document.querySelector('meta[name="_csrf_header"]');
    var token = tEl && tEl.content ? tEl.content.trim() : '';
    var name  = hEl && hEl.content ? hEl.content.trim() : '';
    if (token && name) headers[name] = token;
    return headers;
  }

  // --- Confirm 모달 ---
  function setupConfirmModal() {
    var root = document.getElementById('confirmModal');
    if (!root) return null;
    var msg  = root.querySelector('#qm-message');
    var ok   = root.querySelector('[data-role="ok"]');
    var cancel = root.querySelector('[data-role="cancel"]');
    var lastFocused = null;

    function open(message) {
      msg.textContent = message || '확인하시겠습니까?';
      lastFocused = document.activeElement;
      root.hidden = false;
      root.setAttribute('aria-hidden','false');
      ok.focus();

      function keyHandler(e){
        if (e.key === 'Escape') { confirmResolve(false); }
        if (e.key === 'Tab') {
          // 간단 포커스 트랩
          var focusables = root.querySelectorAll('button');
          if (!focusables.length) return;
          var first = focusables[0], last = focusables[focusables.length-1];
          if (e.shiftKey && document.activeElement === first) { last.focus(); e.preventDefault(); }
          else if (!e.shiftKey && document.activeElement === last) { first.focus(); e.preventDefault(); }
        }
      }
      root.addEventListener('keydown', keyHandler);

      var resolver;
      function cleanup() {
        root.removeEventListener('keydown', keyHandler);
        root.hidden = true;
        root.setAttribute('aria-hidden','true');
        if (lastFocused && lastFocused.focus) lastFocused.focus();
        resolver = null;
      }
      function confirmResolve(val){
        if (!resolver) return;
        var r = resolver; resolver = null;
        cleanup(); r(val);
      }
      function onOk(){ confirmResolve(true); }
      function onCancel(){ confirmResolve(false); }

      ok.addEventListener('click', onOk, { once:true });
      cancel.addEventListener('click', onCancel, { once:true });

      return new Promise(function(resolve){ resolver = resolve; });
    }

    return { open: open };
  }
  var qmodal = setupConfirmModal();

  var detailPanel = document.getElementById('detailPanel');
  if (!detailPanel) return;
  var detailUrlBase = detailPanel.dataset.detailUrl || '/admin/complain/detail';

  // --- 행 클릭 → 상세 로드 ---
  var rows = document.querySelectorAll('tr.row');
  for (var i = 0; i < rows.length; i++) {
    rows[i].addEventListener('click', function (e) {
      if (e.target.closest && e.target.closest('.btn-del')) return;
      var id = this.dataset.id;
      if (!id) return;
      var url = detailUrlBase + '?id=' + encodeURIComponent(id);
      fetch(url, { headers: { 'X-Requested-With': 'XMLHttpRequest' } })
        .then(function (res) { return res.ok ? res.text() : null; })
        .then(function (html) { if (html != null) detailPanel.innerHTML = html; })
        .catch(function (err) { console.error('detail fetch failed', err); });
    });
  }

  // --- 삭제 버튼 → 커스텀 모달 확인 후 AJAX 삭제 ---
  var dels = document.querySelectorAll('.btn-del');
  for (var j = 0; j < dels.length; j++) {
    dels[j].addEventListener('click', function (e) {
      e.stopPropagation();
      var form = this.closest ? this.closest('form') : null;
      if (!form) return;
      var idInput = form.querySelector('input[name="id"]');
      if (!idInput) return;
      var id = idInput.value;

      var ask = qmodal && qmodal.open ? qmodal.open('삭제 하시겠습니까?') : Promise.resolve(window.confirm('삭제 하시겠습니까?'));
      ask.then(function(yes){
        if (!yes) return;

        fetch(form.action, {
          method: 'POST',
          headers: withCsrf({
            'Content-Type': 'application/x-www-form-urlencoded',
            'X-Requested-With': 'XMLHttpRequest'
          }),
          body: 'id=' + encodeURIComponent(id)
        })
        .then(function (r) { return r.json(); })
        .then(function (j) {
          if (!j || !j.ok) return;

          // 행 제거
          var tr = form.closest('tr');
          if (tr && tr.parentNode) tr.parentNode.removeChild(tr);

          // No 재정렬
          var leftRows = document.querySelectorAll('tbody tr.row');
          for (var i = 0; i < leftRows.length; i++) {
            var firstTd = leftRows[i].querySelector('td');
            if (firstTd) firstTd.textContent = String(i + 1);
          }

          // 우측 패널 초기화
          if (detailPanel) detailPanel.innerHTML = '<div class="no-data">민원 신고를 선택해 주세요.</div>';

          // 빈 목록 메시지
          if (leftRows.length === 0) {
            var tbody = document.querySelector('tbody');
            if (tbody) {
              var r = document.createElement('tr');
              var c = document.createElement('td');
              c.className = 'no-data';
              c.colSpan = 6;
              c.textContent = '데이터가 없습니다.';
              r.appendChild(c);
              tbody.appendChild(r);
            }
          }
        })
        .catch(function (err) { console.error('delete failed', err); });
      });
    });
  }

  // --- 상태 변경(디테일 패널 폼 위임) ---
  document.addEventListener('submit', function (e) {
    var f = e.target;
    if (!f.classList || !f.classList.contains('detail-foot')) return;
    e.preventDefault();

    var idEl = f.querySelector('input[name="id"]');
    var sel  = f.querySelector('select[name="status"]');
    if (!idEl || !sel) return;

    var id = idEl.value;
    var status = sel.value;

    fetch(f.action, {
      method: 'POST',
      headers: withCsrf({
        'Content-Type': 'application/x-www-form-urlencoded',
        'X-Requested-With': 'XMLHttpRequest'
      }),
      body: 'id=' + encodeURIComponent(id) + '&status=' + encodeURIComponent(status)
    })
    .then(function (r) { return r.json(); })
    .then(function (j) {
      if (!j || !j.ok) return;

      // 상태 셀 갱신
      var tr = document.querySelector('tr.row[data-id="' + id + '"]');
      if (tr) {
        var tds = tr.querySelectorAll('td');
        if (tds.length >= 5) {
          var html =
            status === 'PENDING'     ? '<span class="st pending">미처리</span>' :
            status === 'IN_PROGRESS' ? '<span class="st progress">처리중</span>' :
            status === 'RESOLVED'    ? '<span class="st resolved">완료</span>' :
                                        '<span class="st">' + status + '</span>';
          tds[4].innerHTML = html;
        }
      }

      // 우측 패널 프래그먼트 재로딩
      var base = detailPanel.dataset.detailUrl || '/admin/complain/detail';
      fetch(base + '?id=' + encodeURIComponent(id), {headers:{'X-Requested-With':'XMLHttpRequest'}})
        .then(function (r) { return r.text(); })
        .then(function (html) { detailPanel.innerHTML = html; });
    })
    .catch(function (err) { console.error('status update failed', err); });
  });
});
