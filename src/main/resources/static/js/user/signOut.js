const btn = document.getElementById("signout_btn");
const pw = document.getElementById("signout_pw");

btn.addEventListener('click',function() {
    const header = document.querySelector('meta[name="_csrf_header"]').content;
    const token = document.querySelector('meta[name="_csrf"]').content;
    if(pw.value!==null && pw.value.length >= 8){
        if(confirm('정말로 탈퇴하시겠습니까?')){
            fetch(`/api/user/user_deleted?password=${pw.value}`, {
                method: "PUT",
                headers: {
                    'header': header,
                    'X-Requested-With': 'XMLHttpRequest',
                    "Content-Type": "application/json",
                    'X-CSRF-Token': token
                }
            })
                .then((response) => response.json())
                .then((data) => {
                    if(data.id === 1){
                        alert("회원 탈퇴 성공")
                        window.location.replace('/index');
                    }else if(data.id === -3){
                        alert("비밀번호 오류!")
                    }else{
                        alert("일반회원만 탈퇴 할 수 있습니다!");
                    }
                })
        }
    }else{
        alert('비밀번호를 다시 입력하세요!')
    }
})