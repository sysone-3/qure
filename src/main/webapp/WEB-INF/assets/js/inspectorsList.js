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
})