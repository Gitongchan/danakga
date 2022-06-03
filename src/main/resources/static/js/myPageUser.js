const id = document.getElementById('userId');
const name = document.getElementById('userName');
const phone = document.getElementById('userPhone');
const email = document.getElementById('userEmail');
const postCode = document.getElementById('sample4_postcode');
const roadNum = document.getElementById('sample4_roadAddress');
const jibun = document.getElementById('sample4_jibunAddress');
const detailadress = document.getElementById('sample4_detailAddress');



const post_btn = document.getElementById("PostCode");
post_btn.addEventListener('click',sample4_execDaumPostcode);

(async function() {
    try {
        const res = await fetch('/api/user')
        const data = await res.json();

        console.log(res);
        console.log(data);
        if(res.status === 200){
            console.log(data);
            id.innerText = data.userid;
            name.innerText = data.name;
            phone.innerText = data.phone;
            email.value = data.email;
            postCode.value = data.userAdrNum;
            roadNum.value = data.userStreetAdr;
            jibun.value = data.userLotAdr;
            detailadress.value = data.userDetailAdr;
        }
    }
    catch (e) {
    }
})();

const changeOK = document.getElementById('changeOK');

changeOK.addEventListener('click',function(e) {

    event.preventDefault();
    // api에 요청을 보낼 때 header에 _csrf토큰값을 가져와서 넘김
    const header = document.querySelector('meta[name="_csrf_header"]').content;
    const token = document.querySelector('meta[name="_csrf"]').content;

    const userData = {
        userid: id.innerText,
        name: name.innerText,
        password: "ASDasd12!",
        phone: phone.innerText,
        email: email.value,
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