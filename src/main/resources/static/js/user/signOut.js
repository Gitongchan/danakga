const btn = document.getElementById("signout_btn");
const pw = document.getElementById("signout_pw");

// <form method="POST" action="/api/user/logout">
//     <input type="hidden" name="_csrf" value="${token}">
//         <button>로그아웃</button>
// </form>
btn.addEventListener('click',function() {
    const header = document.querySelector('meta[name="_csrf_header"]').content;
    const token = document.querySelector('meta[name="_csrf"]').content;
    if(pw.value!==null && pw.value.length >= 8){
        if(confirm('정말로 탈퇴하시겠습니까?')){
            fetch(`/api/user/user_deleted`, {
                method: "PUT",
                headers: {
                    'header': header,
                    'X-Requested-With': 'XMLHttpRequest',
                    "Content-Type": "application/json",
                    'X-CSRF-Token': token
                },
                body: JSON.stringify({password: pw.value})
            })
                .then((response) => response.json())
                .then((data) => {
                    console.log(data);
                    if(data.id === -1){
                        alert("회원탈퇴 실패!")
                    }else if(data.id === -2){
                        alert("일반 회원만 탈퇴할 수 있습니다!")
                    }else{
                        const formData = new FormData();
                        formData.append('_csrf',token);

                        const res = fetch('/api/user/logout',{
                            method: "POST",
                            headers: {
                                'header': header,
                                'X-Requested-With': 'XMLHttpRequest',
                                "Content-Type": "application/json",
                                'X-CSRF-Token': token
                            },
                            body: formData
                        })
                            .then(response => {
                                if (!response.ok) {
                                    throw new Error('에러발생');
                                }
                                return response.json();
                            })
                            .then(data => {
                            })
                        alert("탈퇴 완료!");
                        window.location.replace('/index');
                    }
                })
        }
    }else{
        alert('8자 이상의 비밀번호를 입력하세요!')
    }
})