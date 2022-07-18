const id = document.getElementById('userId');
const name = document.getElementById('userName');
const phone = document.getElementById('userPhone');
const email = document.getElementById('userEmail');
const postCode = document.getElementById('sample4_postcode');
const roadNum = document.getElementById('sample4_roadAddress');
const jibun = document.getElementById('sample4_jibunAddress');
const detailadress = document.getElementById('sample4_detailAddress');
const changePw = document.getElementById('change_pw');
const changePwConfirm = document.getElementById('change_pw_confirm');

const realPassword = document.getElementById("my_pw");
let isBool = false;



const post_btn = document.getElementById("PostCode");
post_btn.addEventListener('click',sample4_execDaumPostcode);

(async function() {
    try {
        const res = await fetch('/api/user')
        const data = await res.json();

        console.log(data);
        if(res.status === 200){
            id.innerText = data.userid;
            name.value = data.name;
            phone.value = data.phone;
            email.innerText = data.email;
            postCode.value = data.userAdrNum;
            roadNum.value = data.userStreetAdr;
            jibun.value = data.userLotAdr;
            detailadress.value = data.userDetailAdr;
        }
    }
    catch (e) {
    }
})();



// 변경버튼 누르면 실행
const changeOK = document.getElementById('changeOK');
changeOK.addEventListener('click',function(e) {
    event.preventDefault();
    // api에 요청을 보낼 때 header에 _csrf토큰값을 가져와서 넘김
    const header = document.querySelector('meta[name="_csrf_header"]').content;
    const token = document.querySelector('meta[name="_csrf"]').content;

    const userData = {
        userid: id.innerText,
        name: name.value,
        password: isBool ? changePwConfirm.value : realPassword.value,
        checkPassword : realPassword.value,
        phone: phone.value,
        email: email.innerText,
        userAdrNum: postCode.value,
        userLotAdr: jibun.value,
        userStreetAdr: roadNum.value,
        userDetailAdr: detailadress.value
    }
    fetch("/api/user", {
        method: "PUT",
        headers: {
            'header': header,
            'X-Requested-With': 'XMLHttpRequest',
            "Content-Type": "application/json",
            'X-CSRF-Token': token
        },
        body: JSON.stringify(userData),
    })
        .then((response) => response.json())
        .then((data) => console.log(data))

})




function chagnePwCheck() {
    <!-- 두번째 비밀번호 input에  입력시 비밀번호 두 개가 맞는지 확인하는 함수-->
    if (changePw.value !== changePwConfirm.value) {
        <!-- 두 비밀번호가 일치하지 않을 때-->
        document.getElementById('pw_confirmcheck').style.display = 'block';
        document.getElementById('pw_confirmcheck').innerHTML = '비밀번호가 일치하지 않습니다!';
        changePwConfirm.classList.remove("_success");
        changePwConfirm.classList.add("_error");
        isBool = false;
    } else {
        <!-- 두 비밀번호가 일치할때-->
        document.getElementById('pw_confirmcheck').style.display = "none";
        changePwConfirm.classList.remove("_error");
        changePwConfirm.classList.add("_success");
        isBool = true;
    }
}