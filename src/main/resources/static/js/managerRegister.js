//jquery 가져와서 사용하는 곳
let script = document.createElement('script');
script.src = 'https://code.jquery.com/jquery-3.4.1.min.js';
script.type = 'text/javascript';
document.getElementsByTagName('head')[0].appendChild(script);

// 공백 정규식
// var regExp = /^[0-9]+$/;

//  영문 대문자, 소문자, 숫자, 문자 사이 공백 및 특수문자.
// /^[a-zA-Z0-9-_/,.][a-zA-Z0-9-_/,. ]*$/;

//  아이디 체크(영문자 또는 숫자 6~20자)
//  /^[a-z]+[a-z0-9]{5,19}$/g;

//blur란? input태그에서 focus가 벗어나면? 이벤트가 발생하도록 하는 것

const useridCheck = /^[a-z]+[a-z0-9]{5,19}$/g; //[A-Za-z\d]{4,15}$/; // 최소4자, 최대 15자의 문자

const passwordCheck = /^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{8,16}$/; //최소 8 자, 최소 하나의 문자 및 하나의 숫자

const numberCheck = /^[0-9]{2,3}[0-9]{3,4}[0-9]{4}/;	// 숫자인경우

const nameCheck = /[가-힣]/; // 한글, 영어만

const emailCheck = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/;

<!-- 회원가입 폼 값들 -->
<!-- 유저아이디, 비밀번호, 이름, 이메일, 전화번호 저장하는 객체 -->
const userData = {
    userid: document.getElementById('reg-ID'),
    password1: document.getElementById('reg-pass'),
    password2: document.getElementById('reg-pass-confirm'),
    username: document.getElementById('reg-name'),
    email: document.getElementById('reg-email'),
    phone: document.getElementById('reg-phone'),
    postCode: document.getElementById('sample4_postcode'),
    sample4_roadAddress: document.getElementById('sample4_roadAddress'),
    sample4_jibunAddress: document.getElementById('sample4_jibunAddress'),
    sample4_detailAddress: document.getElementById('sample4_detailAddress')
}

<!-- 유효성검사가 정상실행 되어서 값이 입력되어있다면 실행하는 함수-->
function buttoncheck() {
    const result = (userData.userid.classList.contains("_success") &&
        userData.password1.classList.contains("_success") &&
        userData.password2.classList.contains("_success") &&
        userData.username.classList.contains("_success") &&
        userData.email.classList.contains("_success") &&
        userData.phone.classList.contains("_success") &&
        userData.sample4_detailAddress.classList.contains("_success"));
        document.getElementById('register-pass').disabled = !result;
}

//회원가입 버튼 부분
const mregisterOK = document.getElementById("manager_register-pass");

mregisterOK.addEventListener('click', async function () {
    event.preventDefault();
    // api에 요청을 보낼 때 header에 _csrf토큰값을 가져와서 넘김
    const header = document.querySelector('meta[name="_csrf_header"]').content;
    const token = document.querySelector('meta[name="_csrf"]').content;

    const postData = {
        userid: userData.userid.value,
        password: userData.password1.value,
        name: userData.username.value,
        phone: userData.phone.value,
        email: userData.email.value,
        userAdrNum: userData.postCode.value,
        userLotAdr: userData.sample4_jibunAddress.value,
        userStreetAdr: userData.sample4_roadAddress.value,
        userDetailAdr: userData.sample4_detailAddress.value
    }
    console.log(postData);
    await fetch("/signup", {
        method: "POST",
        headers: {
            'header': header,
            'X-Requested-With': 'XMLHttpRequest',
            "Content-Type": "application/json",
            'X-CSRF-Token': token
        },
        body: JSON.stringify(postData)
    })
        .then(res => {
            if (res.status === 200 || res.status === 201) { // 성공을 알리는 HTTP 상태 코드면
                location.href = '/index';
            } else { // 실패를 알리는 HTTP 상태 코드면
                console.error(res.statusText);
                console.error(res);
            }
        })
        .then(data => console.log(data))
        .catch(error => console.log(error))
})
