// 카테고리 더보기 버튼 부분
const moreBtn = document.querySelectorAll('.btn_item_more');

for(const btn of moreBtn){
    btn.addEventListener('click', (event) => {
        const checkClass = event.target.parentNode.parentElement.classList;
        if(checkClass.contains('open')){
            checkClass.remove('open');
        }else{
            checkClass.add('open');
        }
    })
}

//내 장비(장바구니)
const rod = document.querySelector('.rod');
const reel = document.querySelector('.reel');
const chum = document.querySelector('.chum');
const lure = document.querySelector('.lure');
const cloth = document.querySelector('.cloth');
const all = document.querySelector('.all');


//내장비 담기 하면 들어가 공간
const rod_id = document.getElementById('rod_panel');
const reel_id = document.getElementById('reel_panel');
const chum_id = document.getElementById('chum_panel');
const lure_id = document.getElementById('lure_panel');
const cloth_id = document.getElementById('cloth_panel');
const all_id = document.getElementById('all_panel');

//체크박스 타이틀 제목
const title_name = document.querySelector('.title_name');
// 타이틀 히든 벨류
const title_value = document.getElementById('title_value');

rod.addEventListener('click', () => {
    title_name.textContent = '낚시대';
    title_value.value = "낚시대";
    if(rod_id.classList.contains('hide')){
        rod_id.classList.remove('hide');
    }else{
        rod_id.classList.add('hide');
    }
})

reel.addEventListener('click', () => {
    title_name.textContent = '릴'
    title_value.value = "릴";
    if(reel_id.classList.contains('hide')){
        reel_id.classList.remove('hide');
    }else{
        reel_id.classList.add('hide');
    }
})

chum.addEventListener('click', () => {
    title_name.textContent = '밑밥'
    title_value.value = "밑밥";
    if(chum_id.classList.contains('hide')){
        chum_id.classList.remove('hide');
    }else{
        chum_id.classList.add('hide');
    }
})

lure.addEventListener('click', () => {
    title_name.textContent = '루어'
    title_value.value = "루어";

    if(lure_id.classList.contains('hide')){
        lure_id.classList.remove('hide');
    }else{
        lure_id.classList.add('hide');
    }
})

cloth.addEventListener('click', () => {
    title_name.textContent = '의류'
    title_value.value = "의류";

    if(cloth_id.classList.contains('hide')){
        cloth_id.classList.remove('hide');
    }else{
        cloth_id.classList.add('hide');
    }
})

all.addEventListener('click', () => {
    title_name.textContent = '공통장비'
    title_value.value = "공통장비";

    if(all_id.classList.contains('hide')){
        all_id.classList.remove('hide');
    }else{
        all_id.classList.add('hide');
    }
})

async function searchProduct (type, subtype, brand, name, stock) {
    try{
        const res = await fetch(`/api/product/list?productType=${type}&productSubType=${subtype}&productBrand=${brand}&productName=${name}&productStock=${stock}?page=0`)
        const data = await res.json();

        console.log(data);
    }catch (e) {

    }
}

(async function(){
    await searchProduct(title_value.value,"","","","");
})();