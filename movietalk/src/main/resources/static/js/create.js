// 등록 클릭 시(form submit)
document.querySelector("#createForm").addEventListener("submit", (e) => {
  // submit 기능 중지
  e.preventDefault();

  // uploadResult 안 li 정보 수집 후 form hidden 태그로 append
  const attachInfos = document.querySelectorAll(".uploadResult li");

  let result = "";

  attachInfos.forEach((obj, idx) => {
    result += `<input type="hidden" name="movieImages[${idx}].imgName" value="${obj.dataset.name}">`;
    result += `<input type="hidden" name="movieImages[${idx}].uuid" value="${obj.dataset.uuid}">`;
    result += `<input type="hidden" name="movieImages[${idx}].path" value="${obj.dataset.path}">`;
  });

  e.target.insertAdjacentHTML("beforeend", result);

  console.log(e.target.innerHTML);

  e.target.submit();
});
