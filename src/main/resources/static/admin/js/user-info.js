/*
* email X
* name X
* pwd
* phone X
* role
* userAdrNum = 우편번호
* userDeletedDate
* userDetailAdr = 상세주소
* userLotAdr = 지번주소
* userStreetAdr = 도로명
* userid X
* */

const username = document.querySelector('#username');
const userid = document.querySelector('#userid');
const email = document.querySelector('#email');
const phone = document.querySelector('#phone');
const userAdrNum = document.querySelector('#userAdrNum');
const userStreetAdr = document.querySelector('#userStreetAdr');
const userLotAdr = document.querySelector('#userLotAdr');
const userDetailAdr = document.querySelector('#userDetailAdr');

const url = new URL(window.location.href);
const urlParams = url.searchParams;
const id =  urlParams.get('id');

async function getUserInfo(){
    const res = await fetch(`/admin/members/user/${id}`);
    const data = await res.json();

    if(!res.ok){
        alert("조회 실패!");
        return null;
    }
    username.value = data.name;
    userid.value = data.userid;
    email.value = data.email;
    phone.value = data.phone;
    userAdrNum.value = data.userAdrNum;
    userStreetAdr.value = data.userStreetAdr;
    userLotAdr.value = data.userLotAdr;
    userDetailAdr.value = data.userDetailAdr;
}

getUserInfo();