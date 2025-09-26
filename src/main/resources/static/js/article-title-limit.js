document.addEventListener("DOMContentLoaded", () => {
  const titleInput = document.getElementById("title");
  const titleCount = document.getElementById("title-count");
  const titleWarning = document.getElementById("title-warning");
  const maxLength = 50;

  if (!titleInput) return;

  titleInput.addEventListener("input", () => {
    const length = titleInput.value.length;
    titleCount.textContent = `${length}/${maxLength}`;

    if (length >= maxLength) {
      titleWarning.style.display = "block"; // 경고 표시
    } else {
      titleWarning.style.display = "none";  // 숨김
    }
  });
});