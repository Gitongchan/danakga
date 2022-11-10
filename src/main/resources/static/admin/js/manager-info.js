/*
* email X
* name X
* pwd
* phone X
* role
* companyAdrNum = 우편번호
* userDeletedDate
* companyDetailAdr = 상세주소
* companyLotAdr = 지번주소
* companyStreetAdr = 도로명
* userid X
* companyBankNum = 은행계좌
* companyNum = 회사번호
* */



const username = document.querySelector('#username');
const userid = document.querySelector('#userid');
const email = document.querySelector('#email');
const phone = document.querySelector('#phone');
const companyAdrNum = document.querySelector('#companyAdrNum');
const companyStreetAdr = document.querySelector('#companyStreetAdr');
const companyLotAdr = document.querySelector('#companyLotAdr');
const companyDetailAdr = document.querySelector('#companyDetailAdr');
const companyBankNum = document.querySelector('#companyBankNum');
const companyNum = document.querySelector('#companyNum');

const url = new URL(window.location.href);
const urlParams = url.searchParams;
const id =  urlParams.get('id');

async function getManagerInfo(){
    const res = await fetch(`/admin/members/manager/${id}`);
    const data = await res.json();

    console.log(data);
    if(!res.ok){
        alert("조회 실패!");
        return null;
    }
    username.value = data.name;
    userid.value = data.userid;
    email.value = data.email;
    phone.value = data.phone;
    companyAdrNum.value = data.companyAdrNum;
    companyStreetAdr.value = data.companyStreetAdr;
    companyLotAdr.value = data.companyLotAdr;
    companyDetailAdr.value = data.companyDetailAdr;
    companyBankNum.value = data.companyBankNum ?? '등록안됨';
    companyNum.value = data.companyNum ?? '등록안됨';
}

getManagerInfo();