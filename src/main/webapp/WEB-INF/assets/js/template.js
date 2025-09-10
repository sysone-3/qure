// insert form 전용: 이름 카운트, 주기 문구, 항목 추가, 타입/도메인 토글 + 폼 검증
(function () {
    'use strict';

    document.addEventListener('DOMContentLoaded', function () {
        initDomainChips();
        initNameCounter();
        initCycle();
        initChecklist();
        initFormValidation(); // 폼 검증 초기화
    });

    // 도메인 라디오 -> 칩 .active 반영
    function initDomainChips() {
        var group = document.querySelector('.domain-buttons');
        if (!group) return;

        var apply = function () {
            group.querySelectorAll('.domain-btn').forEach(function (l) { l.classList.remove('active'); });
            var checked = group.querySelector('input[name="domain"]:checked');
            if (checked) {
                var lbl = checked.closest('.domain-btn');
                if (lbl) lbl.classList.add('active');
            }
        };

        group.addEventListener('change', apply);
        apply(); // 초기 반영
    }

    // 이름 글자수
    function initNameCounter() {
        var nameInput = document.getElementById('templateName');
        var nameCount = document.getElementById('nameCount');
        if (!nameInput || !nameCount) return;

        var update = function () {
            nameCount.textContent = (nameInput.value.length || 0) + '/33';
        };
        nameInput.addEventListener('input', update);
        update();
    }

    // 주기 설명
    function initCycle() {
        var cycleNumber = document.getElementById('cycleNumber');
        var cycleUnit = document.getElementById('cycleUnit');
        var cycleDesc = document.getElementById('cycleDesc');
        if (!cycleNumber || !cycleUnit || !cycleDesc) return;

        function unitText(v) {
            switch (v) {
                case 'DAY': return '일';
                case 'WEEK': return '주';
                case 'MONTH': return '개월';
                case 'YEAR': return '년';
                default: return '일';
            }
        }

        function update() {
            var num = parseInt(cycleNumber.value, 10) || 1;
            if (num <= 0) num = 1;
            var txt = '현재 설정: ' + num + unitText(cycleUnit.value) + '에 한 번 점검';
            cycleDesc.textContent = txt;
        }

        cycleNumber.addEventListener('input', update);
        cycleNumber.addEventListener('change', update);
        cycleUnit.addEventListener('change', update);
        setTimeout(update, 0);
    }

    // 항목 추가/타입 토글/글자수 카운트
    function initChecklist() {
        var listEl = document.getElementById('checkItemList');
        var addBtn = document.getElementById('addItemBtn');
        var tpl = document.getElementById('checkItemTemplate');
        if (!listEl || !addBtn || !tpl) return;

        var index = 0;

        function makeItem(idx) {
            var html = tpl.innerHTML.replaceAll('{INDEX}', String(idx));
            var wrap = document.createElement('div');
            wrap.innerHTML = html.trim();
            var itemEl = wrap.firstElementChild;

            // 타입 옵션 active + 라디오 연동
            var typeWrap = itemEl.querySelector('.type-options');
            if (typeWrap) {
                typeWrap.addEventListener('click', function (e) {
                    var label = e.target.closest('.type-option');
                    if (!label) return;
                    typeWrap.querySelectorAll('.type-option').forEach(function (el) { el.classList.remove('active'); });
                    label.classList.add('active');
                    var radio = label.querySelector('input[type="radio"]');
                    if (radio) radio.checked = true;
                });
            }

            // 글자수 카운트
            var input = itemEl.querySelector('.check-input');
            var counter = itemEl.querySelector('.char-count');
            if (input && counter) {
                var updateCount = function () { counter.textContent = (input.value.length || 0) + '/33'; };
                input.addEventListener('input', updateCount);
                updateCount();

                // 🔴 입력 시 에러 상태 제거
                input.addEventListener('input', function() {
                    clearInputError(input);
                });
            }

            // 🔴 삭제 버튼 처리
            attachDelete(itemEl);

            return itemEl;
        }

        function attachDelete(itemEl) {
            var del = itemEl.querySelector('.delete-wrapper');
            if (!del) return;

            del.addEventListener('click', function () {
                // (선택) 최소 1개 보장하려면 아래 가드 주석 해제
                // if (listEl.children.length <= 1) { alert('최소 1개 항목은 필요합니다.'); return; }

                itemEl.remove();
                reindexItems();
            });
        }

        // 🔁 남은 항목들의 name/index 재정렬 (Spring MVC 바인딩용)
        function reindexItems() {
            var items = listEl.querySelectorAll('.check-item');
            items.forEach(function (el, i) {
                el.dataset.index = i;

                // 각 항목 내 라디오 그룹 name 업데이트: items[old].type -> items[i].type
                el.querySelectorAll('input[type="radio"][name^="items["]').forEach(function (r) {
                    r.name = r.name.replace(/items\[\d+\]\.type/, 'items[' + i + '].type');
                });

                // 텍스트 인풋 name 업데이트: items[old].label -> items[i].label
                var labelInput = el.querySelector('.check-input[name^="items["]');
                if (labelInput) {
                    labelInput.name = 'items[' + i + '].label';
                }
            });

            // 다음 추가될 index 갱신 (연속 번호 유지)
            index = items.length;
        }

        function addItem() {
            var el = makeItem(index++);
            listEl.appendChild(el);
        }

        // 최초 1개 기본 추가
        addItem();

        // [항목 추가] 버튼
        addBtn.addEventListener('click', addItem);
    }

    // 🆕 폼 검증 기능
    function initFormValidation() {
        var form = document.querySelector('.form');
        var submitBtn = document.querySelector('.js-submit');

        if (!form || !submitBtn) return;

        // 등록하기 버튼 클릭 시 검증
        submitBtn.addEventListener('click', function(e) {
            e.preventDefault(); // 기본 폼 제출 방지

            if (validateForm()) {
                // 검증 통과 시 폼 제출
                form.submit();
            }
        });

        // 점검표 이름 입력 시 에러 상태 제거
        var nameInput = document.getElementById('templateName');
        if (nameInput) {
            nameInput.addEventListener('input', function() {
                clearInputError(nameInput);
            });
        }
    }

    // 폼 검증 함수
    function validateForm() {
        var isValid = true;
        var firstErrorInput = null;

        // 1. 점검표 이름 검증
        var nameInput = document.getElementById('templateName');
        if (nameInput && nameInput.value.trim() === '') {
            showInputError(nameInput);
            isValid = false;
            if (!firstErrorInput) firstErrorInput = nameInput;
        }

        // 2. 점검 항목들 검증
        var checkItems = document.querySelectorAll('.check-input');
        checkItems.forEach(function(input) {
            if (input.value.trim() === '') {
                showInputError(input);
                isValid = false;
                if (!firstErrorInput) firstErrorInput = input;
            }
        });

        // 검증 실패 시 첫 번째 에러 필드로 스크롤
        if (!isValid && firstErrorInput) {
            firstErrorInput.focus();
            firstErrorInput.scrollIntoView({
                behavior: 'smooth',
                block: 'center'
            });
        }

        return isValid;
    }

    // 인풋 에러 상태 표시
    function showInputError(input) {
        input.classList.add('error');
    }

    // 인풋 에러 상태 제거
    function clearInputError(input) {
        input.classList.remove('error');
    }
})();