const id = document.getElementById('userId');
const email = document.getElementById('userEmail');
const companyAdrNum = document.getElementById('sample4_postcode');
const companyName = document.getElementById("companyName");
const companyNum = document.getElementById("companyNum");
const companyStreetAdr = document.getElementById("sample4_roadAddress");
const companyLotAdr = document.getElementById("sample4_jibunAddress");
const companyDetailAdr = document.getElementById("sample4_detailAddress");
const companyBankName =document.getElementById("companyBankName");
const companyBankNum = document.getElementById("companyNum");

const post_btn = document.getElementById("postCode");
post_btn.addEventListener('click',sample4_execDaumPostcode);

(async function() {
    try {
        const res = await fetch('/api/user')
        const data = await res.json();

        console.log(data);
        if(res.status === 200){
            id.innerText = data.userid;
            email.innerText = data.email;
        }
    }
    catch (e) {
    }
})();



// 변경버튼 누르면 실행
const changeOK = document.getElementById('changeOK');
changeOK.addEventListener('click',function(event) {
    event.preventDefault();

    const userData = {
        companyName:companyName.value,
        companyNum:companyNum.value,
        companyBankName:companyBankName.value,
        companyBankNum:+companyBankNum.value,
        companyDetailAdr:companyDetailAdr.value,
        companyStreetAdr:companyStreetAdr.value,
        companyAdrNum:companyAdrNum.value,
        companyLotAdr:companyLotAdr.value,
    }

    fetch("/api/user/company_register", {
        method: "POST",
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

// fetch('/api/user/company_register')