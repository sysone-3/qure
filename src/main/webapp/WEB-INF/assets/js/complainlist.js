// complainlist.js
// HTML에서 detailPanel에 엔드포인트 주입:
// <section id="detailPanel" data-detail-url="${pageContext.request.contextPath}/admin/complain/detail"></section>

document.addEventListener('DOMContentLoaded', function () {
  // CSRF 헤더 주입 헬퍼
  function withCsrf(headers) {
    var tEl = document.querySelector('meta[name="_csrf"]');
    var hEl = document.querySelector('meta[name="_csrf_header"]');
    var token = tEl && tEl.content ? tEl.content.trim() : '';
    var name  = hEl && hEl.content ? hEl.content.trim() : '';
    if (token && name) headers[name] = token; // 둘 다 있을 때만
    return headers;
  }


  var detailPanel = document.getElementById('detailPanel');
  if (!detailPanel) return;

  var detailUrlBase = detailPanel.dataset.detailUrl || '/admin/complain/detail';

  // 목록 행 클릭 → 상세 프래그먼트 로드
  var rows = document.querySelectorAll('tr.row');
  for (var i = 0; i < rows.length; i++) {
    rows[i].addEventListener('click', function (e) {
      if (e.target.closest && e.target.closest('.btn-del')) return;
      var id = this.dataset.id;
      if (!id) return;

      var url = detailUrlBase + '?id=' + encodeURIComponent(id);
      fetch(url, { headers: { 'X-Requested-With': 'XMLHttpRequest' } })
        .then(function (res) { if (!res.ok) return null; return res.text(); })
        .then(function (html) { if (html == null) return; detailPanel.innerHTML = html; })
        .catch(function (err) { console.error('detail fetch failed', err); });
    });
  }

  // 삭제 버튼 제출 (AJAX)
  var dels = document.querySelectorAll('.btn-del');
  for (var j = 0; j < dels.length; j++) {
    dels[j].addEventListener('click', function (e) {
      e.stopPropagation();
      var form = this.closest ? this.closest('form') : null;
      if (!form) return;
      var idInput = form.querySelector('input[name="id"]');
      if (!idInput) return;
      var id = idInput.value;

      if (!confirm('이 민원을 삭제하시겠습니까?')) return;

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
        var rows = document.querySelectorAll('tbody tr.row');
        for (var i = 0; i < rows.length; i++) {
          var firstTd = rows[i].querySelector('td');
          if (firstTd) firstTd.textContent = String(i + 1);
        }

        // 우측 패널 초기화
        var panel = document.getElementById('detailPanel');
        if (panel) panel.innerHTML = '<div class="no-data">민원 신고를 선택해 주세요.</div>';

        // 빈 목록 메시지
        if (rows.length === 0) {
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
  }
  
  // 상태 변경: detail-foot 폼 위임 처리
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
          tds[4].innerHTML = html; // 상태 열 인덱스
        }
      }

      // 우측 패널 프래그먼트 재로딩
      var panel = document.getElementById('detailPanel');
      if (panel) {
        var base = panel.dataset.detailUrl || '/admin/complain/detail';
        fetch(base + '?id=' + encodeURIComponent(id), {headers:{'X-Requested-With':'XMLHttpRequest'}})
          .then(function (r) { return r.text(); })
          .then(function (html) { panel.innerHTML = html; });
      }
    })
    .catch(function (err) { console.error('status update failed', err); });
  });

});
