const replyList = document.querySelector(".replyList");

// -------- 댓글 목록 가져오기
const url = `http://localhost:8080/replies`;

const loadReply = () => {
  fetch(`${url}/board/${bno}`)
    .then((res) => {
      if (!res.ok) {
        throw new Error(`에러 발생 ${res.status}`);
      }

      return res.json();
    })
    .then((data) => {
      console.log(data);

      // 댓글개수 보여주기
      // document.querySelector(".row .card:nth-child(2) .card-title span").innerHTML = data.length;
      replyList.previousElementSibling.firstElementChild.innerHTML = data.length;

      // data 댓글 영역에 보여주기
      let result = "";
      data.forEach((reply) => {
        result += `<div class="d-flex justify-content-between my-2 border-bottom reply-row" data-rno="${reply.rno}">
         <div class="p-3">
           <img src="/img/himkuma.jpg" alt="" class="rounded-circle.mx-auto.d-block" style="width: 60px; height: 60px" />
         </div>
         <div class="flex-grow-1 align-self-center">
           <div>${reply.replyer}</div>
           <div>
               <span class="fs-5">${reply.text}</span>
            </div>
           <div class="text-muted">
              <span class="small">${formatDate(reply.createDate)}</span>
           </div>
         </div>
         <div class="d-flex flex-column align-self-center">
           <div class="mb-2">
             <botton class="btn btn-outline-success btn-sm">수정</botton>
           </div>
           <div class="mb-2">
             <botton class="btn btn-outline-danger btn-sm">삭제</botton>
           </div>
         </div>
      </div>`;
      });
      replyList.innerHTML = result;
    });
};
// 댓글 목록 호출
loadReply();

// -------- 댓글 추가
// 댓글작성 클릭 시 == replyForm submit 이 발생 시
document.querySelector("#replyForm").addEventListener("submit", (e) => {
  // submit 기능지중지
  e.preventDefault();
  const form = e.target;
  const rno = form.rno.value;
  // {
  //     "text": "reply...추가",
  //     "replyer": "guest4",
  //     "bno": 50
  // }
  const reply = {
    rno: rno, // 있거나 없거나
    text: form.text.value,
    replyer: form.replyer.value,
    bno: bno,
  };

  // new or modify => rno value 존재 여부
  if (!rno) {
    // post 요청
    fetch(`${url}/new`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(reply),
    })
      .then((res) => {
        if (!res.ok) {
          throw new Error(`error! ${res.status}`);
        }

        // json body 추출
        return res.json();
      })
      .then((data) => {
        console.log(data);
        if (data) {
          Swal.fire({
            title: "댓글 추가 완료",
            icon: "success",
            draggable: true,
          });
        }
        // 작성란 비우기
        form.replyer.value = "";
        form.text.value = "";

        // 댓글가져오기
        loadReply();
      })
      .catch((err) => console.log(err));
  } else {
    // modify
    fetch(`${url}/${rno}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(reply),
    })
      .then((res) => {
        if (!res.ok) {
          throw new Error(`error! ${res.status}`);
        }

        // json body 추출
        return res.json();
      })
      .then((data) => {
        console.log(data);
        if (data) {
          Swal.fire({
            title: "댓글 수정 완료",
            icon: "success",
            draggable: true,
          });
        }
        // 작성란 비우기
        form.replyer.value = "";
        form.text.value = "";
        form.rno.value = "";
        // form.reset();

        form.rbtn.innerHTML = "댓글 작성";

        // 댓글가져오기
        loadReply();
      })
      .catch((err) => console.log(err));
  }
});

// -------- 날짜/시간 2025-12-16T15:46:41.030901
const formatDate = (data) => {
  const date = new Date(data);
  return (
    // 2025/12/16 12:20
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

// ---- 댓글 삭제 버튼 클릭 시
// document.querySelectorAll(".btn-outline-danger").forEach((btn) => {
//   btn.addEventListener("click", (e) => {
//     const targetBtn = e.target;

//     const rno = targetBtn.closest(".reply-row").dataset.rno;
//   });
// });
// 이벤트버블링
replyList.addEventListener("click", (e) => {
  console.log(e.target); // 어느 버튼의 이벤트인가?
  const btn = e.target;

  // 부모쪽으로만 검색
  // data- 접근 : dataset
  const rno = btn.closest(".reply-row").dataset.rno;
  console.log(rno);

  // 삭제 or 수정
  if (btn.classList.contains("btn-outline-danger")) {
    if (!confirm("정말로 삭제하시겠습니까?")) return;

    // true 인 경우 삭제요청(fetch)
    fetch(`${url}/${rno}`, {
      method: "DELETE",
    })
      .then((res) => {
        if (!res.ok) {
          throw new Error(`error! ${res.status}`);
        }
        return res.text();
      })
      .then((data) => {
        console.log(data);
        if (data) {
          Swal.fire({
            title: "데이터 삭제 완료",
            icon: "success",
            draggable: true,
          });
        }
        // 댓글 다시 가져오기
        loadReply();
      })
      .catch((err) => console.log(err));
  } else if (btn.classList.contains("btn-outline-success")) {
    // rno 를 이용해 reply 가져오기
    // 가져온 reply 를 replyForm 에 보여주기
    const form = document.querySelector("#replyForm");
    // 댓글 작성 버튼 => 댓글 수정
    fetch(`${url}/${rno}`)
      .then((res) => {
        if (!res.ok) {
          throw new Error(`error! ${res.status}`);
        }

        // json body 추출
        return res.json();
      })
      .then((data) => {
        console.log(data);

        form.rno.value = data.rno;
        form.replyer.value = data.replyer;
        form.text.value = data.text;

        // 버튼 텍스트 변경
        form.rbtn.innerHTML = "댓글 수정";
      })
      .catch((err) => console.log(err));
  }
});
