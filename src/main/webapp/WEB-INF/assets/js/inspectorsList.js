// inspectorsList.js
function onlyNumber(el) {
    el.value = el.value.replace(/[^0-9]/g, '');

    const p1 = document.getElementById("phone1").value;
    const p2 = document.getElementById("phone2").value;
    const p3 = document.getElementById("phone3").value;

    document.getElementById("phone").value =
        (p1 && p2 && p3) ? `${p1}-${p2}-${p3}` : "";
}
    const modal = document.getElementById("inspectorModal");
    const openModalBtn = document.querySelector(".register");
    const cancelBtn = document.querySelector(".btn.cancel");
    const form = modal ? modal.querySelector("form") : null;

    // 모달 열기
    if (openModalBtn) {
        openModalBtn.addEventListener("click", (e) => {
            e.preventDefault();
            modal.style.display = "flex";
        });
    }

    // 취소 버튼 눌렀을 때 닫기 + reset
    if (cancelBtn) {
        cancelBtn.addEventListener("click", (e) => {
            e.preventDefault();
            if (form) form.reset();
            document.getElementById("phone").value = ""; // hidden 필드도 초기화
            modal.style.display = "none";
        });
    }

    // 바깥 클릭 시 닫기
    if (modal) {
        modal.addEventListener("click", (e) => {
            if (e.target === modal) {
                if (form) form.reset();
                document.getElementById("phone").value = "";
                modal.style.display = "none";
            }
        });
    }

    // 소속-설비 매핑
    const equipmentOptions = {
        "소방": ["스프링쿨러", "소화기", "화재경보기"],
        "순찰": ["CCTV", "비상벨", "출입통제"],
        "청결": ["분리수거함", "화장실", "소독기"],
    };

    const domainSelect = document.getElementById("domain");
    const equipmentSelect = document.getElementById("equipment");

    if (domainSelect && equipmentSelect) {
        domainSelect.addEventListener("change", function () {
            const selectDomain = this.value;

            equipmentSelect.innerHTML = "";

            if (selectDomain && equipmentOptions[selectDomain]) {
                const defaultOption = document.createElement("option");
                defaultOption.value = "";
                defaultOption.textContent = "설비를 선택하세요";
                equipmentSelect.appendChild(defaultOption);

                equipmentOptions[selectDomain].forEach((equip) => {
                    const option = document.createElement("option");
                    option.value = equip;
                    option.textContent = equip;
                    equipmentSelect.appendChild(option);
                });
            } else {
                const option = document.createElement("option");
                option.value = "";
                option.textContent = "소속을 먼저 선택하세요.";
                equipmentSelect.appendChild(option);
            }
        });
    }

     if (form) {
            form.addEventListener("submit", function () {
                const p1 = document.getElementById("phone1").value;
                const p2 = document.getElementById("phone2").value;
                const p3 = document.getElementById("phone3").value;

                document.getElementById("phone").value =
                    (p1 && p2 && p3) ? `${p1}-${p2}-${p3}` : "";
            });
        }

         const deleteBtn = document.getElementById("deleteBtn");
            const deleteModal = document.getElementById("deleteModal");
            const deleteCancel = document.getElementById("deleteCancel");
            const selectedIdsInput = document.getElementById("selectedIdsInput");

            if (deleteBtn) {
                deleteBtn.addEventListener("click", () => {
                    const checkedBoxes = document.querySelectorAll("input[name='selectedIds']:checked");
                    if (checkedBoxes.length === 0) {
                        alert("삭제할 작업자를 선택하세요.");
                        return;
                    }

                    // 선택된 ID들을 hidden input에 담기
                    const ids = Array.from(checkedBoxes).map(cb => cb.value);
                    selectedIdsInput.value = ids.join(",");

                    deleteModal.style.display = "flex";
                });
            }

            if (deleteCancel) {
                deleteCancel.addEventListener("click", () => {
                    deleteModal.style.display = "none";
                });
            }

            if (deleteModal) {
                deleteModal.addEventListener("click", (e) => {
                    if (e.target === deleteModal) {
                        deleteModal.style.display = "none";
                    }
                });
            }
});
