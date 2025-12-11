// 검색 폼 submit 시
const form = document.querySelector("#actionForm");

form.addEventListener("submit", (e) => {
  // submit 중지
  e.preventDefault();

  // keyword, select 값이 있는지 확인
  // 없다면 메세지 띄우기
  if (form.type.value === "") {
    alert("검색 조건을 선택해주세요");
    form.type.focus();
    return;
  } else if (form.keyword.value === "") {
    alert("검색어를 입력하세요");
    form.type.focus();
    return;
  }
  // page 값을 1로 변경
  form.page.value = 1;
  form.submit();
});
