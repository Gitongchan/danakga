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
    companyName: document.getElementById('reg-companyName'),
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
        userData.sample4_detailAddress.classList.contains("_success"))
        document.querySelector('#register-pass.btn.uregister-pass').disabled = !result;
        document.querySelector('#register-pass.btn.cregister-pass').disabled = !result;
}

<!-- 회원가입 아이디 및 패스워드 확인 함수-->
const regeisterCheck = {
    ID: () => {
        const checkbool = useridCheck.test(userData.userid.value);
        console.log(useridCheck.test(userData.userid.value));
        if (checkbool === false) {
            <!-- 정규식 실패 시-->
            console.log("정규식 실패에요!!!")
            document.getElementById('id_check').innerHTML = '공백 및 특수문자를 제외한 영문 및 숫자 4~20자를 입력해주세요!';
            document.getElementById('id_check').style.display = 'block';
            document.getElementById('id_check').style.color = '#B02A37';
            userData.userid.classList.remove("_success");
            userData.userid.classList.add("_error");
            buttoncheck();
        } else {
            console.log("정규식 성공이에요!!!");
            <!-- 정규식 성공 시-->
            fetch(`/api/userid_check?userid=${userData.userid.value}`)
                .then(response => response.json())
                .then(data => {
                    if (data.result === 1) {
                        document.getElementById('id_check').innerHTML = '사용 가능한 아이디입니다!';
                        document.getElementById('id_check').style.display = 'block';
                        document.getElementById('id_check').style.color = '#0167f3';
                        userData.userid.classList.remove("_error");
                        userData.userid.classList.add("_success");
                        buttoncheck();
                    } else {
                        document.getElementById('id_check').innerHTML = '사용 중인 아이디입니다!!';
                        document.getElementById('id_check').style.display = 'block';
                        document.getElementById('id_check').style.color = '#B02A37';
                        userData.userid.classList.remove("_success");
                        userData.userid.classList.add("_error");
                        buttoncheck();
                    }
                })
                .catch(error => console.log(error))
        }
    },
    PW: () => {
        <!-- 두번째 비밀번호 input에  입력시 비밀번호 두 개가 맞는지 확인하는 함수-->
        if (userData.password1.value !== userData.password2.value) {
            <!-- 두 비밀번호가 일치하지 않을 때-->
            document.getElementById('pw_confirmcheck').style.display = 'block';
            document.getElementById('pw_confirmcheck').innerHTML = '비밀번호가 일치하지 않습니다!';
            userData.password2.classList.remove("_success");
            userData.password2.classList.add("_error");
            buttoncheck();
        } else {
            <!-- 두 비밀번호가 일치할때-->
            document.getElementById('pw_confirmcheck').style.display = "none";
            userData.password2.classList.remove("_error");
            userData.password2.classList.add("_success");
            buttoncheck();
        }
    },
    EMAIL: () => {
        const checkbool = emailCheck.test(userData.email.value);
        if (checkbool === false) {
            <!-- 정규식 실패 시-->
            document.getElementById('em_check').innerHTML = '이메일 형식을 올바르게 작성해주세요!';
            document.getElementById('em_check').style.display = 'block';
            document.getElementById('em_check').style.color = '#B02A37';
            userData.email.classList.remove("_success");
            userData.email.classList.add("_error");
            buttoncheck();
        } else {
            <!-- 정규식 성공 시-->
            fetch(`/api/email_check?email=${userData.email.value}`)
                .then(response => response.json())
                .then(data => {
                    if (data.result === 1) {
                        document.getElementById('em_check').innerHTML = '사용 가능한 이메일입니다!';
                        document.getElementById('em_check').style.display = 'block';
                        document.getElementById('em_check').style.color = '#0167f3';
                        userData.email.classList.remove("_error");
                        userData.email.classList.add("_success");
                        buttoncheck();
                    } else {
                        document.getElementById('em_check').innerHTML = '사용 중인 이메일입니다!!';
                        document.getElementById('em_check').style.display = 'block';
                        document.getElementById('em_check').style.color = '#B02A37';
                        userData.email.classList.remove("_success");
                        userData.email.classList.add("_error");
                        buttoncheck();
                    }
                })
                .catch(error => console.log(error))
        }
    },
    companyName: ()=> {
        fetch(`/api/company/name_check?companyName=${userData.companyName.value}`)
            .then(response => response.json())
            .then(data => {
                if (data.result === 1) {
                    document.getElementById('cn_check').innerHTML = '사용 가능한 회사명입니다!!';
                    document.getElementById('cn_check').style.display = 'block';
                    document.getElementById('cn_check').style.color = '#0167f3';
                    userData.companyName.classList.remove("_error");
                    userData.companyName.classList.add("_success");
                    buttoncheck();
                } else {
                    document.getElementById('cn_check').innerHTML = '등록된 회사명입니다!!';
                    document.getElementById('cn_check').style.display = 'block';
                    document.getElementById('cn_check').style.color = '#B02A37';
                    userData.companyName.classList.remove("_success");
                    userData.companyName.classList.add("_error");
                    buttoncheck();
                }
            })
            .catch(error => console.log(error))
    }
}


<!-- 첫 번째 비밀번호 정규식 체크하는 부분(password input에 focus가 떠나게 되면)-->
userData.password1.onblur = function () {
    console.log(passwordCheck.test(userData.password1.value));
    if (!passwordCheck.test(userData.password1.value)) {
        <!--비밀번호 정규화 실패 시-->
        document.getElementById('pw_check').style.display = "block";
        document.getElementById('pw_check').innerHTML = "숫자, 영문, 특수문자 포함 최소8자 이상입력해주세요!";
        userData.password1.classList.remove('_success');
        userData.password1.classList.add('_error');
        buttoncheck();
    } else {
        <!-- 비밀번호 정규화 성공시-->
        document.getElementById("pw_check").style.display = "none";
        userData.password1.classList.remove('_error');
        userData.password1.classList.add('_success');
        regeisterCheck.PW();
        buttoncheck();
    }
};

<!-- 이름 입력 후 focus 벗어나면 유효성 검사-->
userData.username.onblur = function () {
    if (!nameCheck.test(userData.username.value)) {
        <!-- 실패 시 -->
        document.getElementById("name_check").style.display = 'block';
        userData.username.classList.remove("_success");
        userData.username.classList.add("_error");
        buttoncheck();
    } else {
        <!-- 성공 시 -->
        document.getElementById("name_check").style.display = 'none';
        userData.username.classList.remove("_error");
        userData.username.classList.add("_success");
        buttoncheck();
    }
};


<!-- 번호 입력 후 focus 벗어나면 유효성 검사 -->
userData.phone.onblur = function () {
    if (!numberCheck.test(userData.phone.value)) {
        document.getElementById("phone_check").style.display = 'block';
        userData.phone.classList.remove("_success");
        userData.phone.classList.add("_error");
        buttoncheck();
    } else {
        document.getElementById("phone_check").style.display = 'none';
        userData.phone.classList.remove("_error");
        userData.phone.classList.add("_success");
        buttoncheck();
    }
};

<!-- 지번 입력 후 focus 벗어나면 유효성 검사 -->
userData.sample4_detailAddress.onblur = function () {
    if (!(userData.sample4_detailAddress.value.length === 0)) {
        //성공
        userData.sample4_detailAddress.classList.remove("_error");
        userData.sample4_detailAddress.classList.add("_success");
        buttoncheck();
    } else {
        //실패
        userData.sample4_detailAddress.classList.remove("_success");
        userData.sample4_detailAddress.classList.add("_error");
        buttoncheck();
    }
};

const post_btn = document.getElementById("PostCode");
post_btn.addEventListener('click',sample4_execDaumPostcode);

//버튼 값 가져오기
const utap_css = document.getElementById('tap-member');
const ctap_css = document.getElementById('tap-company');
const hide_name = document.querySelector('.register-form #form-data .cn_name._hide');
const desc = document.getElementById('companydesc');

const uRegisterOK = document.querySelector('#register-pass.btn.uregister-pass');
const cRegisterOK = document.querySelector('#register-pass.btn.cregister-pass');


//일반회원 탭을 눌렀을 때
const userTap = document.getElementById('tap-member');
userTap.addEventListener('click',()=>{
    ctap_css.classList.remove('_select');
    utap_css.classList.add('_select');
    hide_name.classList.remove('_select');
    desc.classList.remove('_select');
    cRegisterOK.style.display = 'none';
    uRegisterOK.style.display = 'block';

});


//사업자 회원 탭을 눌렀을 때
const companyTap = document.getElementById('tap-company');
companyTap.addEventListener('click',()=>{
    utap_css.classList.remove('_select');
    ctap_css.classList.add('_select');
    hide_name.classList.add('_select');
    desc.classList.add('_select');

    uRegisterOK.style.display = 'none';
    cRegisterOK.style.display = 'block';
    //사업자 회원일 때 회원가입 기능 ON
    cRegisterOK.addEventListener('click',companyBtn);

});

// 사업자 버튼 눌렸을 때 버튼 동작하는 함수
const companyBtn = function(event) {
    event.preventDefault();

    const postData = {
        userid: userData.userid.value,
        password: userData.password1.value,
        name: userData.username.value,
        phone: userData.phone.value,
        email: userData.email.value,
        companyName: userData.companyName.value,
        companyAdrNum: userData.postCode.value,
        companyLotAdr: userData.sample4_jibunAddress.value,
        companyStreetAdr: userData.sample4_roadAddress.value,
        companyDetailAdr: userData.sample4_detailAddress.value
    }
    fetch("/api/company/signup", {
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

            } else { // 실패를 알리는 HTTP 상태 코드면
                console.error(res.statusText);
                console.error(res);
            }
        })
        .then(data => {
            alert('사업자 회원가입 성공!');
            location.replace('/login');
        })
        .catch(error => console.log(error))
}

//일반 회원일 때
uRegisterOK.addEventListener('click',  function (event) {
    event.preventDefault();

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
    fetch("/api/signup", {
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

            } else { // 실패를 알리는 HTTP 상태 코드면
                console.error(res.statusText);
                console.error(res);
            }
        })
        .then(data => {
            alert('회원가입 성공!');
            location.replace('/login');
        })
        .catch(error => console.log(error))
});

