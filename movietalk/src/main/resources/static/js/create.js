// x 를 클릭 시 파일 삭제(이벤트 버블링)
document.querySelector(".uploadResult").addEventListener("click", (e) => {
  e.preventDefault();

  const aTag = e.target.closest("a");
  const li = e.target.closest("li");

  // href 값 가져오기
  const href = aTag.getAttribute("href");

  // 컨트롤러로 요청 보내기
  const formData = new FormData();
  formData.append("fileName", href);

  fetch("/upload/remove", {
    method: "post",
    body: formData,
  })
    .then((res) => res.text())
    .then((data) => {
      console.log(data);
      // 화면에서 이미지 제거
      li.remove();
    });
});
