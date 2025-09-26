async function apiFetch(url, options = {}) {
  const { header, token } = csrf();  // // CSRF 토큰과 헤더 키를 가져옴 (Spring Security용)

  const res = await fetch(url, {  // fetch API 호출
    ...options,  // method, body 등 옵션
    headers: {
      "Content-Type": "application/json",  // JSON 요청
      ...(token ? { [header]: token } : {}),  // CSRF 토큰 있으면 헤더에 포함
      ...(options.headers || {})  // 추가적인 커스텀 헤더가 있으면 병합
    },
    credentials: "same-origin"  // 쿠키(JSESSIONID 등) 포함
  });

  let result;
  try {
    result = await res.json(); // 서버에서 내려준 JSON (ApiResponse) 파싱
  } catch (e) {
    result = { success: false, message: "서버 응답을 파싱할 수 없습니다." };
  }

  if (!res.ok || !result.success) {
    throw new Error(result.message || `요청 실패: ${res.status}`);  // 실패 시 에러 발생시켜 catch 블록에서 처리
  }

  return result;  // { success, data, message } 구조의 ApiResponse 반환
}

// 메시지 표시 함수
let hideTimeout;  // 전역 변수로 타이머 ID 저장

function showMessage(msg, isError = false, duration = 3000) {
  const box = document.getElementById("msg-box");
  if (!box) return;

  // 기존 타이머 제거 (중복 실행 방지)
  if (hideTimeout) {
    clearTimeout(hideTimeout);
  }

  box.textContent = msg;
  box.className = isError ? "error" : "success";
  box.style.display = "block";

  hideTimeout = setTimeout(() => {
    box.style.display = "none";
  }, duration);
}

// 이동 하기 전 딜레이
function delay(path) {
    setTimeout(() => {
        location.replace(path);
    }, 2000)  // 2초
}

// 삭제 버튼
 const deleteButton = document.getElementById('delete-btn');  // 삭제 버튼 가져오기
 if (deleteButton) {
     deleteButton.addEventListener('click', async () => {
         const id = document.getElementById('article-id')?.value;  // 게시글 id 가져오기
         if (!id) return alert('id가 없습니다.');

         if (!confirm("정말 삭제하시겠습니까?")) return; // ✅ UX 개선(삭제 확인 다이얼로그)

        try {
              const result = await apiFetch(`/api/articles/${id}`, { method: 'DELETE' });
              showMessage(result.message || "삭제가 완료되었습니다.");  // 성공 시 메시지 출력
              delay("/articles");  // 목록 페이지로 이동
            } catch (err) {
              showMessage("삭제 실패: " + err.message, true);  // 실패 시 에러 메시지 표시
            }
      });
}

// modifyButton / createButton 쪽에서 body 구성하는 코드도 반복, 아직 사용 x
function getArticleFormData() {
  return {
    title: document.getElementById('title').value,
    content: document.getElementById('content').value
  };
}

 // 수정 버튼
const modifyButton = document.getElementById('modify-btn');
if (modifyButton) {
  modifyButton.addEventListener('click', async () => {
    const id = document.getElementById('article-id')?.value;
    if (!id) return alert('id가 없습니다.');

    const body = {  // 입력값 읽어서 JSON 객체로 만듦
      title: document.getElementById('title').value,
      content: document.getElementById('content').value,
    };

    try {
      const result = await apiFetch(`/api/articles/${id}`, {
        method: 'PUT',
        body: JSON.stringify(body)  // JSON 문자열로 변환
      });
      showMessage(result.message || "수정이 완료되었습니다.");
      delay(`/articles/${id}`);  // 수정된 글 상세 페이지로 이동
    } catch (err) {
      showMessage("수정 실패: " + err.message, true);
    }
  });
}

 // 생성 버튼
const createButton = document.getElementById('create-btn');
if (createButton) {
  createButton.addEventListener('click', async () => {
    const body = {  // 폼 입력값 읽어서 JSON 객체
      title: document.getElementById('title').value,
      content: document.getElementById('content').value,
    };

    try {
      const result = await apiFetch(`/api/articles`, {
        method: 'POST',
        body: JSON.stringify(body)
      });
//      alert(result.message || "등록이 완료되었습니다.");
        showMessage(result.message || "등록이 완료되었습니다.");
          // 2초 후 새 글 상세 페이지로 이동
          delay(`/articles/${result.data.id}`);
    } catch (err) {
      showMessage("등록 실패: " + err.message, true);
    }
  });
}



//// 삭제
// const deleteButton = document.getElementById('delete-btn');
// if (deleteButton) {
//     deleteButton.addEventListener('click', async () => {
//         const id = document.getElementById('article-id')?.value;  // 잡아온 id의 값
//         if (!id) return alert('id가 없습니다.');
//
////         const res = await fetch(`/api/articles/${id}`, { method: 'DELETE'});
//         const { header, token } = csrf();
//           const res = await fetch(`/api/articles/${id}`, {
//              method: 'DELETE',
//              headers: token ? { [header]: token } : undefined,
//              credentials: 'same-origin'
//            });
//
//         if (!res.ok) return alert('삭제 실패 : ' + res.status);
//
//         alert('삭제가 완료되었습니다.');
//         location.replace(`/articles`)
//     })
// }
//
// // 수정
// const modifyButton = document.getElementById('modify-btn');
// if (modifyButton) {
//    modifyButton.addEventListener('click', async () => {
//        const id = document.getElementById('article-id')?.value;
//        if (!id) return alert('id가 없습니다.');
//
//        const body = {
//            title: document.getElementById('title').value,
//            content: document.getElementById('content').value,
//        };
//
////        const res = await fetch(`/api/articles/${id}`, {  // 여기로 보냄
////            method: 'PUT',
////            headers: { "Content-Type": "application/json"},
////            body: JSON.stringify(body),  // 내용 담아서
////            });
//
//        const { header, token } = csrf();
//            const res = await fetch(`/api/articles/${id}`, {
//              method: 'PUT',
//              headers: {
//                "Content-Type": "application/json",
//                ...(token ? { [header]: token } : {})
//              },
//              body: JSON.stringify(body),
//              credentials: 'same-origin'
//            });
//
//        if (!res.ok) return alert('수정 실패 : ' + res.status);
//        alert('수정이 완료되었습니다.');
//        location.replace(`/articles/${id}`);
//    });
// }
//
// // 생성
// const createButton = document.getElementById('create-btn');
// if (createButton) {
//    createButton.addEventListener('click', async () => {
//        const body = {
//            title: document.getElementById('title').value,
//            content: document.getElementById('content').value,
//        };
//
////    const res = await fetch(`/api/articles`, {
////        method: 'POST',
////        headers: {"Content-Type" : "application/json"},
////        body: JSON.stringify(body),
////    });
//
//    const { header, token } = csrf();
//    console.log("CSRF header:", header, "token:", token);  // 디버그 로그
//
//        const res = await fetch('/api/articles', {
//          method: 'POST',
//          headers: {
//            "Content-Type": "application/json",
//            ...(token ? { [header]: token } : {})
//          },
//          body: JSON.stringify(body),
//          credentials: 'same-origin'
//        });
//
//    if(!res.ok) return alert('등록 실패 : ' + res.status);
//        alert('등록 완료되었습니다.');
//        location.replace(`/articles`);
//    });
// }