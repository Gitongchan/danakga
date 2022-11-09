const id = document.getElementById('userId');
const name = document.getElementById('userName');
const phone = document.getElementById('userPhone');
const email = document.getElementById('userEmail');
const postCode = document.getElementById('sample4_postcode');
const roadNum = document.getElementById('sample4_roadAddress');
const jibun = document.getElementById('sample4_jibunAddress');
const detailadress = document.getElementById('sample4_detailAddress');
const btn = document.getElementById('changeBtn');

(async function() {
    try {
        const res = await fetch('/api/user')
        const data = await res.json();

        console.log(data);
        if(res.status === 200){
            id.innerText = data.userid;
            name.innerText = data.name;
            phone.innerText = data.phone;
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

btn.addEventListener('click',function (){
    location.replace('/user/edit');
})