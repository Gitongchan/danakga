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
const sea_rod = document.querySelector('.sea_rod');
const fresh_rod = document.querySelector('.fresh_rod');
const one_throw_rod = document.querySelector('.one_throw_rod');
const reel = document.querySelector('.reel');
const line = document.querySelector('.line');
const hook = document.querySelector('.hook');
const all = document.querySelector('.all');


//내장비 담기 하면 들어가 공간
const sea_rod_id = document.getElementById('sea_rod_panel');
const fresh_rod_id = document.getElementById('fresh_rod_panel');
const one_throw_rod_id = document.getElementById('one_throw_rod_panel');
const reel_id = document.getElementById('reel_panel');
const line_id = document.getElementById('line_panel');
const hook_id = document.getElementById('hook_panel');
const all_id = document.getElementById('all_panel');

//체크박스 타이틀 제목
const title_name = document.querySelector('.title_name');
// 타이틀 히든 벨류
const title_value = document.getElementById('title_value');


sea_rod.addEventListener('click', () => {
    title_name.textContent = '바다로드';
    title_value.value = "바다로드";
    if(sea_rod_id.classList.contains('hide')){
        sea_rod_id.classList.remove('hide');
    }else{
        sea_rod_id.classList.add('hide');
    }
})

fresh_rod.addEventListener('click', () => {
    title_name.textContent = '민물로드';
    title_value.value = "민물로드";
    if(fresh_rod_id.classList.contains('hide')){
        fresh_rod_id.classList.remove('hide');
    }else{
        fresh_rod_id.classList.add('hide');
    }
})


one_throw_rod.addEventListener('click', () => {
    title_name.textContent = '원투낚시';
    title_value.value = "원투낚시";
    if(one_throw_rod_id.classList.contains('hide')){
        one_throw_rod_id.classList.remove('hide');
    }else{
        one_throw_rod_id.classList.add('hide');
    }
})

reel.addEventListener('click', () => {
    title_name.textContent = '릴/용품'
    title_value.value = "릴/용품";
    if(reel_id.classList.contains('hide')){
        reel_id.classList.remove('hide');
    }else{
        reel_id.classList.add('hide');
    }
})

line.addEventListener('click', () => {
    title_name.textContent = '라인/용품'
    title_value.value = "라인/용품";
    if(line_id.classList.contains('hide')){
        line_id.classList.remove('hide');
    }else{
        line_id.classList.add('hide');
    }
})

hook.addEventListener('click', () => {
    title_name.textContent = '루어'
    title_value.value = "루어";

    if(hook_id.classList.contains('hide')){
        hook_id.classList.remove('hide');
    }else{
        hook_id.classList.add('hide');
    }
})

all.addEventListener('click', () => {
    title_name.textContent = '기타'
    title_value.value = "기타";

    if(all_id.classList.contains('hide')){
        all_id.classList.remove('hide');
    }else{
        all_id.classList.add('hide');
    }
})


async function searchProduct (type, subtype, brand, name, stock, sort, desc) {
    // productType : 메인타입
    // productSubType : 서브타입
    // productBrand : 브랜드
    // productName : 이름
    // productStock : 재고수
    try{
        const res = await fetch(`/api/product/list?productType=${type}&productSubType=${subtype}&productBrand=${brand}&productName=${name}&productStock=${stock}&page=0&sort=${sort}&order=${desc}`)
        const data = await res.json();

        console.log(data);
    }catch (e) {

    }
}


//첫 로딩시 동작하는 곳
(async function(){
    await searchProduct(title_value.value,'%25','%25','%25',"1", "productUploadDate", "desc");
})();