(function () {
    'use strict';

    document.addEventListener('DOMContentLoaded', function () {
        initDomainChips();
        initNameCounter();
        initCycle();
        initChecklist();
        initFormValidation();
        initActionButtons();
    });

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
        apply();
    }

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

    function initChecklist() {
        var listEl = document.getElementById('checkItemList');
        var addBtn = document.getElementById('addItemBtn');
        var tpl = document.getElementById('checkItemTemplate');
        if (!listEl || !addBtn || !tpl) return;

        var index = 0;
        index = listEl.querySelectorAll('.check-item').length;

        function wireUpItem(itemEl) {
            // 타입 토글
            var typeWrap = itemEl.querySelector('.type-options');
            if (typeWrap) {
                typeWrap.addEventListener('click', function (e) {
                    var label = e.target.closest('.type-option');
                    if (!label) return;
                    typeWrap.querySelectorAll('.type-option').forEach(function (t) {
                        t.classList.remove('active');
                    });
                    label.classList.add('active');
                    var radio = label.querySelector('input[type="radio"]');
                    if (radio) radio.checked = true;
                });
            }

            // 글자수 카운터 + 에러 클리어
            var input = itemEl.querySelector('.check-input');
            var counter = itemEl.querySelector('.char-count');
            if (input) {
                var updateCount = function () {
                    if (counter) counter.textContent = (input.value.length || 0) + '/33';
                    clearInputError(input);
                };
                input.addEventListener('input', updateCount);
                updateCount(); // 초기 표시
            }
            attachDelete(itemEl);
        }

        // 기존 항목(수정 화면) 초기화
        listEl.querySelectorAll('.check-item').forEach(function (el, i) {
            el.dataset.index = i;

            // 라디오 name 정규화
            el.querySelectorAll('input[type="radio"][name^="items["]').forEach(function (r) {
                r.name = r.name.replace(/items\[\d+\]\.type/, 'items[' + i + '].type');
            });

            // 라벨 name 정규화
            var labelInput = el.querySelector('.check-input[name^="items["]');
            if (labelInput) {
                labelInput.name = 'items[' + i + '].label';
            }

            wireUpItem(el);
        });

        // 생성 화면: 초기 없으면 1개 추가
        if (index === 0) addItem();

        addBtn.addEventListener('click', addItem);

        function addItem() {
            var html = tpl.innerHTML.replaceAll('{INDEX}', String(index));
            var wrap = document.createElement('div');
            wrap.innerHTML = html.trim();
            var itemEl = wrap.firstElementChild;

            listEl.appendChild(itemEl);
            wireUpItem(itemEl);
            itemEl.dataset.index = index;
            index++;

            var input = itemEl.querySelector('.check-input');
            if (input) input.focus();
        }

        function attachDelete(itemEl) {
            var del = itemEl.querySelector('.delete-wrapper');
            if (!del) return;
            del.addEventListener('click', function () {
                itemEl.remove();
                // 재인덱스 & name 정규화
                var items = listEl.querySelectorAll('.check-item');
                items.forEach(function (el, i) {
                    el.dataset.index = i;
                    el.querySelectorAll('input[type="radio"][name^="items["]').forEach(function (r) {
                        r.name = r.name.replace(/items\[\d+\]\.type/, 'items[' + i + '].type');
                    });
                    var inp = el.querySelector('.check-input[name^="items["]');
                    if (inp) inp.name = 'items[' + i + '].label';
                });
                index = items.length;
            });
        }
    }



    function initFormValidation() {
        var form = document.querySelector('.form');
        var submitBtn = document.querySelector('.js-submit');

        if (!form || !submitBtn) return;

        submitBtn.addEventListener('click', function(e) {
            e.preventDefault(); // 기본 폼 제출 방지

            if (validateForm()) {
                form.submit();
            }
        });

        var nameInput = document.getElementById('templateName');
        if (nameInput) {
            nameInput.addEventListener('input', function() {
                clearInputError(nameInput);
            });
        }
    }

    function validateForm() {
        var isValid = true;
        var firstErrorInput = null;

        var nameInput = document.getElementById('templateName');
        if (nameInput && nameInput.value.trim() === '') {
            showInputError(nameInput);
            isValid = false;
            if (!firstErrorInput) firstErrorInput = nameInput;
        }

        var checkItems = document.querySelectorAll('.check-input');
        checkItems.forEach(function(input) {
            if (input.value.trim() === '') {
                showInputError(input);
                isValid = false;
                if (!firstErrorInput) firstErrorInput = input;
            }
        });

        if (!isValid && firstErrorInput) {
            firstErrorInput.focus();
            firstErrorInput.scrollIntoView({
                behavior: 'smooth',
                block: 'center'
            });
        }

        return isValid;
    }

    function showInputError(input) {
        input.classList.add('error');
    }

    function clearInputError(input) {
        input.classList.remove('error');
    }

    function initActionButtons() {
        var form = document.querySelector('.form');
        if (!form) return;

        var buttons = document.querySelectorAll('.btn[data-action]');
        buttons.forEach(function (btn) {
            btn.addEventListener('click', function () {
                var action = btn.dataset.action;
                if (!action) return;

                if (action.includes('/delete')) {
                    if (confirm("정말 삭제하시겠습니까?")) {
                        form.action = action;
                        form.method = 'post';
                        form.submit();
                    }
                } else if (action.includes('/update')) {
                    if (validateForm()) {
                        form.action = action;
                        form.method = 'post';
                        form.submit();
                    }
                }
            });
        });
    }
})();