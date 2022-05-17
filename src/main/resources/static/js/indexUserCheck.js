//토근값 가져오기
const token = document.querySelector('meta[name="_csrf"]').content;

//ID넣는 곳
const span_id = document.querySelector('#text_id');

const user_login = document.querySelector('.middle-right-area._user')
const not_login = document.querySelector('.middle-right-area._noInfo')
//로그인 안되어있다면 로그인, 회원가입 > 로그인 되어있다면 내정보|로그아웃

const userDataCheck = async ()=> {
    alert("첫번째 실행");
    const res = await fetch('/api/user')
    alert("두번째 실행");

    const data = await res.json();
    alert("세번째 실행");

    console.log(res);
    console.log(data);
    not_login.style.display = "flex";
    span_id.innerText = `${data.userid}님! 안녕하세요!`;
    

}

console.log(userDataCheck);
window.addEventListener('DOMContentloaded',userDataCheck);