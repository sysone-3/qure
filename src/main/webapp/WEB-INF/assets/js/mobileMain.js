// 작성자 : 최온유
(function () {
  function pickFlash() {
    // __flash 우선(민원용). 없으면 FLASH(success/error) 사용(점검용 유지)
    if (window.__flash && window.__flash.text) return window.__flash;

    const g = window.FLASH || {};
    const s = (g.success || "").trim();
    const e = (g.error || "").trim();
    if (s) return { type: "success", text: s };
    if (e) return { type: "error", text: e };
    return null;
  }

  function showModal(msg) {
    const overlay = document.createElement("div");
    overlay.style.cssText =
      "position:fixed;inset:0;background:rgba(0,0,0,.35);display:flex;align-items:center;justify-content:center;z-index:9999;";

    const box = document.createElement("div");
    box.style.cssText =
      "max-width:320px;width:88%;background:#fff;border-radius:14px;box-shadow:0 10px 30px rgba(0,0,0,.15);padding:18px;text-align:center;";

    const title = document.createElement("div");
    title.textContent = msg.type === "success" ? "완료" : "실패";
    title.style.cssText =
      "font-weight:800;margin-bottom:8px;color:" +
      (msg.type === "success" ? "#0a8f6a" : "#c62828");

    const body = document.createElement("div");
    body.textContent = msg.text;
    body.style.cssText = "font-size:14px;color:#333;margin-bottom:12px;text-align:center;";

    const btn = document.createElement("button");
    btn.textContent = "확인";
    btn.style.cssText =
      "width:100%;height:40px;border:0;border-radius:10px;background:#A9DECE;color:#083B33;font-weight:800;";
    btn.addEventListener("click", () => document.body.removeChild(overlay));

    box.appendChild(title);
    box.appendChild(body);
    box.appendChild(btn);
    overlay.appendChild(box);
    document.body.appendChild(overlay);

    setTimeout(() => {
      if (document.body.contains(overlay)) document.body.removeChild(overlay);
    }, 3000);
  }

  document.addEventListener("DOMContentLoaded", function () {
    const msg = pickFlash();
    if (msg) showModal(msg);
  });
})();
