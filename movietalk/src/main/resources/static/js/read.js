// 전체 리뷰 가져오기
const baseUrl = "/reviews";
const reviewArea = document.querySelector(".reviewList");
const reviewCnt = document.querySelector(".review-cnt");
const reviewForm = document.querySelector("#reviewForm");

//-------- 날짜/시간  2025-12-16T15:44:23.136186
const formatDate = (data) => {
  const date = new Date(data);
  // 2025/12/16 12:20
  return (
    date.getFullYear() +
    "/" +
    (date.getMonth() + 1) +
    "/" +
    date.getDate() +
    " " +
    date.getHours() +
    ":" +
    date.getMinutes()
  );
};

//-------- 전체 리뷰 가져오기
const reviewList = () => {
  fetch(`${baseUrl}/${mno}/all`)
    .then((res) => {
      if (!res.ok) {
        throw new Error("에러 발생");
      }

      return res.json();
    })
    .then((data) => {
      // 화면 작업
      console.log(data);

      let result = "";

      data.forEach((review) => {
        result += `<div class="d-flex justify-content-between py-2 border-bottom review-row" data-rno="${review.rno}" data-email="${review.email}">`;
        result += `<div class="flex-grow-1 align-self-center">`;
        result += `<div><span class="font-semibold">${review.text}</span></div>`;
        result += `<div class="small text-muted"><span class="d-inline-block mr-3">${review.nickname}</span>`;
        result += `평점 : <span class="grade">${review.grade}</span><div class="starrr"></div></div>`;
        result += `<div class="text-muted"><span class="small">${formatDate(review.createDate)}</span></div></div>`;
        // 로그인user == 작성자
        if (loginUser === `${review.email}`) {
          result += `<div class="d-flex flex-column align-self-center">`;
          result += `<div class="mb-2"><button class="btn btn-outline-danger btn-sm">삭제</button></div>`;
          result += `<div><button class="btn btn-outline-success btn-sm">수정</button></div>`;
          result += `</div>`;
        }
        result += `</div>`;
      });

      reviewArea.innerHTML = result;
      reviewCnt.innerHTML = data.length;
    })
    .catch((e) => console.error(e));
};

reviewList();

//-------- 특정 리뷰 삭제
const reviewDelete = (rno, email) => {
  const form = new FormData();
  form.append("email", email);

  fetch(`${baseUrl}/${mno}/${rno}`, {
    method: "DELETE",
    headers: {
      "X-CSRF-TOKEN": csrfVal,
    },
    body: form,
  })
    .then((res) => {
      if (!res.ok) {
        throw new Error("에러 발생");
      }
      return res.text();
    })
    .then((data) => {
      // 화면 작업
      console.log("delete");
      console.log(data);
      // 화면 갱신
      reviewList();
    })
    .catch((e) => console.error(e));
};

//-------- 특정 리뷰 가져오기
const reviewGet = (rno) => {
  fetch(`${baseUrl}/${mno}/${rno}`)
    .then((res) => {
      if (!res.ok) {
        throw new Error("에러 발생");
      }

      return res.json();
    })
    .then((data) => {
      // 화면 작업
      console.log("get");
      console.log(data);

      reviewForm.nickname.value = data.nickname;
      reviewForm.text.value = data.text;
      reviewForm.rno.value = data.rno;
      reviewForm.mid.value = data.mid;
      reviewForm.mno.value = data.mno;
      reviewForm.email.value = data.email;
      reviewForm.querySelector(".starrr a:nth-child(" + data.grade + ")").click();
      reviewForm.rbtn.innerHTML = "수정";

      // 작성 영역으로 포커스
      reviewForm.scrollIntoView({ behavior: "smooth", block: "start" });
      reviewForm.text.focus();
    })
    .catch((e) => console.error(e));
};

// 수정/삭제 클릭시
reviewArea.addEventListener("click", (e) => {
  console.log(e.target);

  // 어느 버튼의 이벤트인가? 수정 or 삭제
  const btn = e.target;
  const rno = btn.closest(".review-row").dataset.rno;
  const email = btn.closest(".review-row").dataset.email;

  if (btn.classList.contains("btn-outline-danger")) {
    // 삭제
    reviewDelete(rno, email);
  } else if (btn.classList.contains("btn-outline-success")) {
    // 수정
    reviewGet(rno);
  }
});

//---------------- 리뷰 수정 + PUT  or  리뷰 신규 + POST
const reviewPut = (form, rno) => {
  const review = {
    rno: rno,
    grade: grade,
    text: form.text.value,
    email: form.email.value,
  };

  fetch(`${baseUrl}/${mno}/${rno}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json", // text/html;charset=UTF-8
      "X-CSRF-TOKEN": csrfVal,
    },
    body: JSON.stringify(review),
  })
    .then((res) => {
      if (!res.ok) {
        throw new Error(`에러 발생 ${res.status}`);
      }

      // json body 추출
      return res.text();
    })
    .then((data) => {
      console.log("modify");
      console.log(data);

      // form.nickname.value = "";
      form.text.value = "";
      form.rno.value = "";
      form.mid.value = "";
      reviewForm.querySelector(".starrr a:nth-child(" + grade + ")").click();

      reviewForm.rbtn.innerHTML = "등록";

      // 리뷰 가져오기
      reviewList();
    })
    .catch((e) => console.error(e));
};

const reviewPost = (form) => {
  const review = {
    mid: form.mid.value,
    mno: mno,
    grade: grade,
    text: form.text.value,
  };

  fetch(`${baseUrl}/${mno}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json", // text/html;charset=UTF-8
      "X-CSRF-TOKEN": csrfVal,
    },
    body: JSON.stringify(review),
  })
    .then((res) => {
      if (!res.ok) {
        throw new Error(`에러 발생 ${res.status}`);
      }
      // json body 추출
      return res.text();
    })
    .then((data) => {
      console.log("NEW");
      console.log(data);

      form.text.value = "";
      form.querySelector(".starrr a:nth-child(" + grade + ")").click();

      // 리뷰 가져오기
      reviewList();
    })
    .catch((e) => console.error(e));
};

// 리뷰 폼 등록 클릭 시 새로운 리뷰 등록 or 기존 리뷰 수정
if (reviewForm) {
  reviewForm.addEventListener("submit", (e) => {
    e.preventDefault();
    // rno 값 존재 여부에 따라
    const form = e.target;
    const rno = form.rno.value;
    console.log("rno : " + rno);

    if (rno) {
      // 수정
      reviewPut(form, rno);
    } else {
      // 등록
      reviewPost(form);
    }
  });
}

// 큰 이미지 보기
const imgModal = document.getElementById("imgModal");
if (imgModal) {
  imgModal.addEventListener("show.bs.modal", (e) => {
    // 모달을 작동시킨 li 요소 찾기
    const posterLi = e.relatedTarget;
    // li의 data-*요소 값 가져오기
    const filePath = posterLi.getAttribute("data-file");

    // Update the modal's content.
    const modalTitle = imgModal.querySelector(".modal-title");
    const modalBody = imgModal.querySelector(".modal-body");

    modalTitle.textContent = `${title}`;
    modalBody.innerHTML = `<img src="/upload/display?fileName=${filePath}" style="width:100%">`;
  });
}
