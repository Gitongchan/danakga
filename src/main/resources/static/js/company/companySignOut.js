const btn = document.getElementById("signout_btn");
const pw = document.getElementById("signout_pw");

btn.addEventListener('click',function() {
    if(pw.value!==null && pw.value.length >= 8){
        if(confirm('정말로 사업자를 탈퇴하시겠습니까?')){
            fetch(`/api/manager/deleted`, {
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
                        alert("사업자 탈퇴 실패");
                    }else if(data.id === -2){
                        alert("비밀번호 오류!")
                    }else{
                        alert("회원 탈퇴 성공")
                        location.replace('/index');
                    }
                })
        }
    }else{
        alert('8자 이상의 비밀번호를 입력하세요!')
    }
})