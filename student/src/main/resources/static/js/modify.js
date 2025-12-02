// 삭제버튼 클릭 시
// remove-form 가져온 후 submit시키기

document.querySelector(".btn-outline-danger").addEventListener("click", (e) => {
  document.querySelector("[name='remove-form']").submit();
});
