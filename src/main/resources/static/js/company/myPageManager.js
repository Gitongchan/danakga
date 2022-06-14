const id = document.getElementById('userId');
const name = document.getElementById('userName');
const phone = document.getElementById('userPhone');
const email = document.getElementById('userEmail');
const comName = document.getElementById('reg-companyName');
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
            try{
                const res = await fetch('/api/manager')
                const data = await res.json();

                comName.value = data.companyName;
                postCode.value = data.companyAdrNum
                roadNum.value = data.companyStreetAdr;
                jibun.value = data.companyLotAdr;
                detailadress.value = data.companyDetailAdr;

            }catch (e) {

            }

        }
    }
    catch (e) {
    }
})();

btn.addEventListener('click',function (){
    location.replace('/user/edit');
})