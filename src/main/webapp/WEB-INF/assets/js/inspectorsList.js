function onlyNumber(el) {
    el.value = el.value.replace(/[^0-9]/g, '');

    const p1 = document.getElementById("phone1").value;
    const p2 = document.getElementById("phone2").value;
    const p3 = document.getElementById("phone3").value;

    document.getElementById("phone").value =
        (p1 && p2 && p3) ? `${p1}-${p2}-${p3}` : "";
}

document.addEventListener("DOMContentLoaded", function () {
    const modal = document.getElementById("inspectorModal");
    const openModalBtn = document.querySelector(".register");
    const cancelBtn = document.querySelector(".btn.cancel");
    const form = modal ? modal.querySelector("form") : null;

    if (openModalBtn) {
        openModalBtn.addEventListener("click", (e) => {
            e.preventDefault();
            modal.style.display = "flex";
        });
    }

    if (cancelBtn) {
        cancelBtn.addEventListener("click", (e) => {
            e.preventDefault();
            if (form) form.reset();
            document.getElementById("phone").value = "";
            modal.style.display = "none";
        });
    }

    if (modal) {
        modal.addEventListener("click", (e) => {
            if (e.target === modal) {
                if (form) form.reset();
                document.getElementById("phone").value = "";
                modal.style.display = "none";
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

    // 삭제 모달
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
