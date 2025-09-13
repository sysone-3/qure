document.addEventListener("DOMContentLoaded",function () {
    const prevBtn = document.querySelector(".circle-btn.prev");
    const nextBtn = document.querySelector(".circle-btn.next");
    const pageNumber = document.getElementById("pageNumber");

    let currentPage = 1;

    prevBtn.addEventListener("click", () => {
        if (currentPage > 1) {
            currentPage--;
            pageNumber.textContent = currentPage;
        }
    });

    nextBtn.addEventListener("click", () => {
        currentPage++;
        pageNumber.textContent = currentPage;
    })

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

        // 취소 버튼 눌렀을 때 닫기
    if (cancelBtn) {
       cancelBtn.addEventListener("click", (e) => {
       e.preventDefault();
       if (form) form.reset();
       modal.style.display = "none";
      });
    }

        // 바깥 클릭 시 닫기
    if (modal) {
       modal.addEventListener("click", (e) => {
       if (e.target === modal) {
           if (form) form.reset();
           modal.style.display = "none";
       }
    });
   }
})

