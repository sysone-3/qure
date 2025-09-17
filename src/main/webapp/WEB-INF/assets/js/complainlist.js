// 작성자 : 최온유
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
  function setupConfirmModal() {                                                     // 커스텀 확인 모달 팩토리
    var root = document.getElementById('confirmModal');                              // 모달 루트 DOM 찾기
    if (!root) return null;                                                          // 없으면 커스텀 모달 비활성(null)

    var msg    = root.querySelector('#qm-message');                                  // 메시지 표시 영역
    var ok     = root.querySelector('[data-role="ok"]');                             // 확인 버튼
    var cancel = root.querySelector('[data-role="cancel"]');                         // 취소(또는 백드롭) 버튼
    var lastFocused = null;                                                          // 열기 전 포커스 저장용

    function open(message) {                                                         // 모달 열기( Promise<boolean> 반환 )
      msg.textContent = message || '확인하시겠습니까?';                              // 안내 문구 설정
      lastFocused = document.activeElement;                                          // 기존 포커스 기억
      root.hidden = false;                                                           // 모달 표시
      root.setAttribute('aria-hidden','false');                                      // 접근성: 보임 상태로
      ok.focus();                                                                    // 첫 포커스를 OK 버튼으로

      function keyHandler(e){                                                        // 키보드 제어
        if (e.key === 'Escape') { confirmResolve(false); }                           // ESC → 취소로 닫기
        if (e.key === 'Tab') {                                                       // Tab 포커스 트랩
          var focusables = root.querySelectorAll('button');                          // 포커스 순환 대상
          if (!focusables.length) return;                                            // 버튼 없으면 무시
          var first = focusables[0], last = focusables[focusables.length-1];         // 첫/마지막 버튼
          if (e.shiftKey && document.activeElement === first) {                      // Shift+Tab에서
            last.focus(); e.preventDefault();                                        // 마지막으로 순환
          } else if (!e.shiftKey && document.activeElement === last) {               // Tab에서
            first.focus(); e.preventDefault();                                       // 처음으로 순환
          }
        }
      }
      root.addEventListener('keydown', keyHandler);                                  // 키 핸들러 등록

      var resolver;                                                                  // Promise resolver 저장
      function cleanup() {                                                           // 닫을 때 공통 정리
        root.removeEventListener('keydown', keyHandler);                             // 키 핸들러 제거
        root.hidden	 = true;                                                          // 숨김
        root.setAttribute('aria-hidden','true');                                     // 접근성: 숨김 상태
        if (lastFocused && lastFocused.focus) lastFocused.focus();                   // 이전 포커스로 복귀
        resolver = null;                                                             // 중복 호출 방지
      }
      function confirmResolve(val){                                                  // 최종 결정 전달
        if (!resolver) return;                                                       // 이미 처리되면 무시
        var r = resolver; resolver = null;                                           // 일회성 보장
        cleanup(); r(val);                                                           // 정리 후 true/false resolve
      }
      function onOk(){ confirmResolve(true); }                                       // 확인 클릭 → true
      function onCancel(){ confirmResolve(false); }                                  // 취소/백드롭 → false

      ok.addEventListener('click', onOk, { once:true });                              // 한 번만 수신
      cancel.addEventListener('click', onCancel, { once:true });                      // 한 번만 수신

      return new Promise(function(resolve){ resolver = resolve; });                  // 호출자에게 Promise 제공
    }

    return { open: open };                                                           // 외부에서 qmodal.open(...) 사용
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
  var dels = document.querySelectorAll('.btn-del');                                    // 삭제 버튼들 수집
  for (var j = 0; j < dels.length; j++) {                                             // 각 버튼에
    dels[j].addEventListener('click', function (e) {                                   // 클릭 핸들러 연결
      e.stopPropagation();                                                             // 행 클릭 이벤트로 전파 차단(상세패널 열림 방지)
      var form = this.closest ? this.closest('form') : null;                           // 버튼을 감싸는 form 찾기
      if (!form) return;                                                               // form 없으면 중단
      var idInput = form.querySelector('input[name="id"]');                            // 숨은 PK 입력 요소 찾기
      if (!idInput) return;                                                            // 없으면 중단
      var id = idInput.value;                                                          // 삭제할 PK 값

      var ask = qmodal && qmodal.open                                                  // 커스텀 모달이 있으면 사용
        ? qmodal.open('삭제 하시겠습니까?')                                        		   // 확인 모달 열고 Promise 반환
        : Promise.resolve(window.confirm('삭제 하시겠습니까?'));                    		   // 없으면 기본 confirm 대체

      ask.then(function(yes){                                                          // 사용자가 응답하면
        if (!yes) return;                                                              // 취소면 종료

        fetch(form.action, {                                                           // 폼 action(URL)으로
          method: 'POST',                                                              // POST 전송
          headers: withCsrf({                                                          // CSRF 헤더 포함
            'Content-Type': 'application/x-www-form-urlencoded',                       // 폼 URL 인코딩 방식
            'X-Requested-With': 'XMLHttpRequest'                                       // AJAX 요청 힌트
          }),
          body: 'id=' + encodeURIComponent(id)                                         // 전송 본문: id=...
        })
        .then(function (r) { return r.json(); })                                       // JSON 응답 파싱
        .then(function (j) {                                                           
          if (!j || !j.ok) return;                                                     // 실패면 아무것도 안 함

          var tr = form.closest('tr');                                                 // 현재 행 찾기
          if (tr && tr.parentNode) tr.parentNode.removeChild(tr);                      // DOM에서 행 제거

          var leftRows = document.querySelectorAll('tbody tr.row');                    // 남은 행들 재수집
          for (var i = 0; i < leftRows.length; i++) {                                  // 일련번호 재계산
            var firstTd = leftRows[i].querySelector('td');                             
            if (firstTd) firstTd.textContent = String(i + 1);                          // 1부터 다시 부여
          }

          if (detailPanel)                                                             // 우측 상세 패널
            detailPanel.innerHTML = '<div class="no-data">민원 신고를 선택해 주세요.</div>';  // 초기화

          if (leftRows.length === 0) {                                                 // 남은 행이 없으면
            var tbody = document.querySelector('tbody');                               
            if (tbody) {                                                               // "데이터가 없습니다." 한 줄 추가
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
        .catch(function (err) { console.error('delete failed', err); });               // 네트워크/서버 에러 로그
      });
    });
  }


  // --- 상태 변경(디테일 패널 폼 위임) ---
  // 문서 전체에서 "submit" 이벤트를 듣는다.
  // 이유: 우측 상세 패널의 폼이 AJAX로 로드되므로, 나중에 생긴 폼에도 적용되게 하려고(이벤트 위임).
  document.addEventListener('submit', function (e) {

    // 1) 이벤트가 실제로 날아온 폼 요소 잡기
    var f = e.target; // 사용자가 제출한 바로 그 <form>

    // 2) 우리가 처리할 폼만 거른다: class="detail-foot" 인 폼만
    //    다른 폼 제출이면 무시
    if (!f.classList || !f.classList.contains('detail-foot')) return;

    // 3) 브라우저의 기본 제출(페이지 이동)을 막고, JS로 AJAX 전송한다.
    e.preventDefault();

    // 4) 폼 안에서 숨은 id와 상태 select 요소를 찾는다.
    var idEl = f.querySelector('input[name="id"]');        // 어떤 신고인지(PK)
    var sel  = f.querySelector('select[name="status"]');   // 바꿀 상태값
    if (!idEl || !sel) return;                             // 없으면 종료

    // 5) 실제 전송할 값 꺼내기
    var id = idEl.value;           // 예: "123"
    var status = sel.value;        // 예: "IN_PROGRESS"

    // 6) 서버로 POST 전송
    //    - f.action: 폼의 action URL(예: /admin/complain/status)
    //    - Content-Type: x-www-form-urlencoded (폼 전송 방식)
    //    - CSRF 헤더도 같이 보냄
    fetch(f.action, {
      method: 'POST',
      headers: withCsrf({
        'Content-Type': 'application/x-www-form-urlencoded',
        'X-Requested-With': 'XMLHttpRequest' // AJAX 힌트
      }),
      body: 'id=' + encodeURIComponent(id) + '&status=' + encodeURIComponent(status) // id=...&status=...
    })

    // 7) 응답을 JSON으로 읽는다. (컨트롤러는 ok:true/false를 돌려준다고 가정)
    .then(function (r) { return r.json(); })

    // 8) 성공 처리
    .then(function (j) {
      if (!j || !j.ok) return; // 실패면 화면 변경 없이 끝

      // 8-1) 왼쪽 목록의 상태 셀도 즉시 반영
      //      테이블에서 data-id="해당 id" 인 행을 찾음
      var tr = document.querySelector('tr.row[data-id="' + id + '"]');
      if (tr) {
        var tds = tr.querySelectorAll('td'); // 그 행의 모든 셀
        if (tds.length >= 5) {
          // 상태값에 따라 뱃지 HTML을 만든다.
          var html =
            status === 'PENDING'     ? '<span class="st pending">미처리</span>' :
            status === 'IN_PROGRESS' ? '<span class="st progress">처리중</span>' :
            status === 'RESOLVED'    ? '<span class="st resolved">완료</span>' :
                                        '<span class="st">' + status + '</span>';

          // 목록 테이블에서 "상태" 열은 0부터 세어 다섯 번째(td[4])라고 가정
          tds[4].innerHTML = html; // 목록 UI 즉시 업데이트
        }
      }

      // 8-2) 우측 상세 패널도 서버에서 최신 HTML 조각을 다시 받아와 교체
      //      (상세 패널의 버튼/메타 영역이 상태에 따라 바뀔 수 있어서 재로딩)
      var base = detailPanel.dataset.detailUrl || '/admin/complain/detail';
      fetch(base + '?id=' + encodeURIComponent(id), { headers: { 'X-Requested-With': 'XMLHttpRequest' } })
        .then(function (r) { return r.text(); })             // JSP가 렌더링한 HTML 문자열
        .then(function (html) { detailPanel.innerHTML = html; }); // 우측 패널 통째로 교체
    })

    // 9) 네트워크 에러 등 예외는 콘솔에만 기록. 화면 변경 없음.
    .catch(function (err) { console.error('status update failed', err); });
  });
});
