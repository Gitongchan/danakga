const span_id = document.createElement('span');
const login_info = document.querySelector('.userinfo');
const div_id = document.querySelector('.view_id');

//로그인 안되어있다면 로그인, 회원가입 > 로그인 되어있다면 내정보|로그아웃

const left_a = document.createElement('a');
const left_span = document.createElement('span');
const right_a = document.createElement('a');
const right_span = document.createElement('span');


window.addEventListener('DOMContentLoaded', async function () {
    await fetch('/api/user')
        .then(response => response.json())
        .then(data =>{
            span_id.innerText = `${data.userid}님! 안녕하세요!`;

            left_a.href = '/mypage';
            left_span.innerText = "내 정보"
            left_a.appendChild(left_span);

            right_a.href = '/logout';
            right_span.innerText = "로그아웃"
            right_a.appendChild(right_span);

            div_id.appendChild(span_id);
            login_info.appendChild(left_a);
            login_info.appendChild(right_a);
        })
        .catch(error=>{
            span_id.innerText = `로그인이 필요합니다!`;
            left_a.href = '/login';
            left_span.innerText = "로그인"
            left_a.appendChild(left_span);

            right_a.href = '/register';
            right_span.innerText = "회원가입"
            right_a.appendChild(right_span);

            div_id.appendChild(span_id);
            login_info.appendChild(left_a);
            login_info.appendChild(right_a);
        })
})