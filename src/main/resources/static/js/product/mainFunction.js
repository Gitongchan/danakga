// 상품이 담길 곳
const sea_rod_arr = [];
const fresh_rod_arr = [];
const one_throw_rod_arr = [];
const reel_arr = [];
const line_arr = [];
const hook_arr = [];
const all_arr = []


const searchBtn = document.getElementById('searchBtn');
const searchText = document.getElementById('searchText');
searchBtn.addEventListener('click',async (event)=>{

    const brand_checked = document.querySelector('input[name="brand"]:checked'); // 체크된 값(checked value)
    const type_checked = document.querySelector('input[name="area"]:checked'); // 체크된 값(checked value)

    await searchProduct(title_value.value, type_checked.value, brand_checked.value, searchText.value,"1", "productUploadDate", "desc");
})


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
// 카테고리 부분
const cate_list = document.querySelector('.cate_list.two');
//상품 정보 담을 곳
const product_list = document.getElementById('product_list');


sea_rod.addEventListener('click', async () => {

    title_name.textContent = '바다로드';
    title_value.value = "바다로드";
    cate_list.innerHTML =`
        <li class="cate_item">
            <label class="item_checkbox">
                <input type="radio" name="area" value="%" checked>
                    <span class="item_text">상관없음</span>
            </label>
        </li>
        <li class="cate_item">
            <label class="item_checkbox">
                <input type="radio" name="area" value="인쇼어">
                    <span class="item_text">인쇼어</span>
            </label>
        </li>
    <li class="cate_item">
        <label class="item_checkbox">
            <input type="radio" name="area" value="라이트지깅">
                <span class="item_text">라이트지깅</span>
        </label>
    </li>
    <li class="cate_item">
        <label class="item_checkbox">
            <input type="radio" name="area" value="볼락/락피쉬">
                <span class="item_text">볼락/락피쉬</span>
        </label>
    </li>
    <li class="cate_item">
        <label class="item_checkbox">
            <input type="radio" name="area" value="지깅로드">
                <span class="item_text">지깅로드</span>
        </label>
    </li>
    <li class="cate_item">
        <label class="item_checkbox">
            <input type="radio" name="area" value="선상캐스트로드">
                <span class="item_text">선상캐스트로드</span>
        </label>
    </li>
    <li class="cate_item">
        <label class="item_checkbox">
            <input type="radio" name="area" value="외수질/침선">
                <span class="item_text">외수질/침선</span>
        </label>
    </li>
    <li class="cate_item">
        <label class="item_checkbox">
            <input type="radio" name="area" value="타이라바">
                <span class="item_text">타이라바</span>
        </label>
    </li>
    <li class="cate_item">
        <label class="item_checkbox">
            <input type="radio" name="area" value="좌대용">
                <span class="item_text">좌대용</span>
        </label>
    </li>
    <li class="cate_item">
        <label class="item_checkbox">
            <input type="radio" name="area" value="두족류">
                <span class="item_text">두족류</span>
        </label>
    </li>`;

    const brand_checked = document.querySelector('input[name="brand"]:checked'); // 체크된 값(checked value)
    const type_checked = document.querySelector('input[name="area"]:checked'); // 체크된 값(checked value)
    await searchProduct(title_value.value, type_checked.value, brand_checked.value,'%',"1", "productUploadDate", "desc");
    if(sea_rod_id.classList.contains('hide')){
        sea_rod_id.classList.remove('hide');
    }else{
        sea_rod_id.classList.add('hide');
    }
})

fresh_rod.addEventListener('click', async () => {

    title_name.textContent = '민물로드';
    title_value.value = "민물로드";
    cate_list.innerHTML =`
        <li class="cate_item">
            <label class="item_checkbox">
                <input type="radio" name="area" value="%" checked>
                    <span class="item_text">상관없음</span>
            </label>
        </li>
        <li class="cate_item">
            <label class="item_checkbox">
                <input type="radio" name="area" value="스피닝로드">
                    <span class="item_text">스피닝로드</span>
            </label>
        </li>
    <li class="cate_item">
        <label class="item_checkbox">
            <input type="radio" name="area" value="베이트로드">
                <span class="item_text">베이트로드</span>
        </label>
    </li>
    <li class="cate_item">
        <label class="item_checkbox">
            <input type="radio" name="area" value="계류로드">
                <span class="item_text">계류로드</span>
        </label>
    </li>
    <li class="cate_item">
        <label class="item_checkbox">
            <input type="radio" name="area" value="가물치로드">
                <span class="item_text">가물치로드</span>
        </label>
    </li>
    <li class="cate_item">
        <label class="item_checkbox">
            <input type="radio" name="area" value="빙어로드">
                <span class="item_text">빙어로드</span>
        </label>
    </li>
    <li class="cate_item">
        <label class="item_checkbox">
            <input type="radio" name="area" value="플라이로드">
                <span class="item_text">플라이로드</span>
        </label>
    </li>`;

    const brand_checked = document.querySelector('input[name="brand"]:checked'); // 체크된 값(checked value)
    const type_checked = document.querySelector('input[name="area"]:checked'); // 체크된 값(checked value)
    await searchProduct(title_value.value, type_checked.value, brand_checked.value,'%',"1", "productUploadDate", "desc");
    if(fresh_rod_id.classList.contains('hide')){
        fresh_rod_id.classList.remove('hide');
    }else{
        fresh_rod_id.classList.add('hide');
    }
})


one_throw_rod.addEventListener('click', async () => {

    title_name.textContent = '원투낚시';
    title_value.value = "원투낚시";
    cate_list.innerHTML =`
        <li class="cate_item">
            <label class="item_checkbox">
                <input type="radio" name="area" value="%" checked>
                    <span class="item_text">상관없음</span>
            </label>
        </li>
        <li class="cate_item">
            <label class="item_checkbox">
                <input type="radio" name="area" value="원투로드">
                    <span class="item_text">원투로드</span>
            </label>
        </li>
    <li class="cate_item">
        <label class="item_checkbox">
            <input type="radio" name="area" value="원투릴">
                <span class="item_text">원투릴</span>
        </label>
    </li>
    <li class="cate_item">
        <label class="item_checkbox">
            <input type="radio" name="area" value="원투소픔">
                <span class="item_text">원투소품</span>
        </label>
    </li>`;

    const brand_checked = document.querySelector('input[name="brand"]:checked'); // 체크된 값(checked value)
    const type_checked = document.querySelector('input[name="area"]:checked'); // 체크된 값(checked value)
    await searchProduct(title_value.value, type_checked.value, brand_checked.value,'%',"1", "productUploadDate", "desc");
    if(one_throw_rod_id.classList.contains('hide')){
        one_throw_rod_id.classList.remove('hide');
    }else{
        one_throw_rod_id.classList.add('hide');
    }
})

reel.addEventListener('click', async () => {

    title_name.textContent = '릴/용품'
    title_value.value = "릴/용품";
    cate_list.innerHTML =`
        <li class="cate_item">
            <label class="item_checkbox">
                <input type="radio" name="area" value="%" checked>
                    <span class="item_text">상관없음</span>
            </label>
        </li>
        <li class="cate_item">
            <label class="item_checkbox">
                <input type="radio" name="area" value="스피닝릴">
                    <span class="item_text">스피닝릴</span>
            </label>
        </li>
    <li class="cate_item">
        <label class="item_checkbox">
            <input type="radio" name="area" value="베이트릴">
                <span class="item_text">베이트릴</span>
        </label>
    </li>
    <li class="cate_item">
        <label class="item_checkbox">
            <input type="radio" name="area" value="전동릴">
                <span class="item_text">전동릴</span>
        </label>
    </li>
    <li class="cate_item">
        <label class="item_checkbox">
            <input type="radio" name="area" value="빙어릴">
                <span class="item_text">빙어릴</span>
        </label>
    </li>
    <li class="cate_item">
        <label class="item_checkbox">
            <input type="radio" name="area" value="전동릴부품">
                <span class="item_text">전동릴부품</span>
        </label>
    </li>
    <li class="cate_item">
        <label class="item_checkbox">
            <input type="radio" name="area" value="튜님용품">
                <span class="item_text">플라이</span>
        </label>
    </li>`;

    const brand_checked = document.querySelector('input[name="brand"]:checked'); // 체크된 값(checked value)
    const type_checked = document.querySelector('input[name="area"]:checked'); // 체크된 값(checked value)
    await searchProduct(title_value.value, type_checked.value, brand_checked.value,'%',"1", "productUploadDate", "desc");
    if(reel_id.classList.contains('hide')){
        reel_id.classList.remove('hide');
    }else{
        reel_id.classList.add('hide');
    }
})

line.addEventListener('click', async () => {
    title_name.textContent = '라인/용품'
    title_value.value = "라인/용품";
    cate_list.innerHTML =`
        <li class="cate_item">
            <label class="item_checkbox">
                <input type="radio" name="area" value="%" checked>
                    <span class="item_text">상관없음</span>
            </label>
        </li>
        <li class="cate_item">
            <label class="item_checkbox">
                <input type="radio" name="area" value="카본라인">
                    <span class="item_text">카본라인</span>
            </label>
        </li>
    <li class="cate_item">
        <label class="item_checkbox">
            <input type="radio" name="area" value="합사라인">
                <span class="item_text">합사라인</span>
        </label>
    </li>
    <li class="cate_item">
        <label class="item_checkbox">
            <input type="radio" name="area" value="모노라인">
                <span class="item_text">모노라인</span>
        </label>
    </li>
    <li class="cate_item">
        <label class="item_checkbox">
            <input type="radio" name="area" value="하이브리드라인">
                <span class="item_text">하이브리드라인</span>
        </label>
    </li>
    <li class="cate_item">
        <label class="item_checkbox">
            <input type="radio" name="area" value="플라이라인">
                <span class="item_text">플라이라인</span>
        </label>
    </li>
    <li class="cate_item">
        <label class="item_checkbox">
            <input type="radio" name="area" value="라인결속기">
                <span class="item_text">라인결속기</span>
        </label>
    </li>
    <li class="cate_item">
        <label class="item_checkbox">
            <input type="radio" name="area" value="플라이">
                <span class="item_text">플라이</span>
        </label>
    </li>`;

    const brand_checked = document.querySelector('input[name="brand"]:checked'); // 체크된 값(checked value)
    const type_checked = document.querySelector('input[name="area"]:checked'); // 체크된 값(checked value)
    await searchProduct(title_value.value, type_checked.value, brand_checked.value,'%',"1", "productUploadDate", "desc");
    if(line_id.classList.contains('hide')){
        line_id.classList.remove('hide');
    }else{
        line_id.classList.add('hide');
    }
})

hook.addEventListener('click', async () => {
    title_name.textContent = '바늘/훅'
    title_value.value = "바늘/훅";
    cate_list.innerHTML =`
        <li class="cate_item">
            <label class="item_checkbox">
                <input type="radio" name="area" value="%" checked>
                    <span class="item_text">상관없음</span>
            </label>
        </li>
        <li class="cate_item">
            <label class="item_checkbox">
                <input type="radio" name="area" value="지그헤드">
                    <span class="item_text">지그헤드</span>
            </label>
        </li>
    <li class="cate_item">
        <label class="item_checkbox">
            <input type="radio" name="area" value="다운샷">
                <span class="item_text">다운샷</span>
        </label>
    </li>
    <li class="cate_item">
        <label class="item_checkbox">
            <input type="radio" name="area" value="와이드갭">
                <span class="item_text">와이드갭</span>
        </label>
    </li>
    <li class="cate_item">
        <label class="item_checkbox">
            <input type="radio" name="area" value="스트레이트">
                <span class="item_text">스트레이트</span>
        </label>
    </li>
    <li class="cate_item">
        <label class="item_checkbox">
            <input type="radio" name="area" value="플래시스위머">
                <span class="item_text">플래시스위머</span>
        </label>
    </li>
    <li class="cate_item">
        <label class="item_checkbox">
            <input type="radio" name="area" value="웜스프링">
                <span class="item_text">웜스프링</span>
        </label>
    </li>
    <li class="cate_item">
        <label class="item_checkbox">
            <input type="radio" name="area" value="플라이">
                <span class="item_text">플라이</span>
        </label>
    </li>
    <li class="cate_item">
        <label class="item_checkbox">
            <input type="radio" name="area" value="플라이">
                <span class="item_text">플라이</span>
        </label>
    </li>`;

    const brand_checked = document.querySelector('input[name="brand"]:checked'); // 체크된 값(checked value)
    const type_checked = document.querySelector('input[name="area"]:checked'); // 체크된 값(checked value)
    await searchProduct(title_value.value, type_checked.value, brand_checked.value,'%',"1", "productUploadDate", "desc");
    if(hook_id.classList.contains('hide')){
        hook_id.classList.remove('hide');
    }else{
        hook_id.classList.add('hide');
    }
})

all.addEventListener('click', async () => {

    title_name.textContent = '기타'
    title_value.value = "기타";
    cate_list.innerHTML =`
        <li class="cate_item">
            <label class="item_checkbox">
                <input type="radio" name="area" value="기타" checked>
                    <span class="item_text">기타</span>
            </label>
        </li>`;

    const brand_checked = document.querySelector('input[name="brand"]:checked'); // 체크된 값(checked value)
    const type_checked = document.querySelector('input[name="area"]:checked'); // 체크된 값(checked value)
    await searchProduct(title_value.value, type_checked.value, brand_checked.value,'%',"1", "productUploadDate", "desc");
    if(all_id.classList.contains('hide')){
        all_id.classList.remove('hide');
    }else{
        all_id.classList.add('hide');
    }
})


const typeRadios = document.querySelectorAll('input[type=radio][name="area"]');
typeRadios.forEach((el)=> el.addEventListener('change', async ()=>{
    //브랜드 값 가지고있는곳
    const brand_checked = document.querySelector('input[name="brand"]:checked'); // 체크된 값(checked value)
    //type값 가지고 있는곳
    const type_checked = document.querySelector('input[name="area"]:checked'); // 체크된 값(checked value)
    //신상품, 최고주문순, 조회최다순 정보 가지고 있는 곳
    const value_checked = document.querySelector('.category_list_wrap ul li.active');
    await searchProduct(title_value.value,type_checked.value,brand_checked.value,'%',"1", value_checked.id, "desc");
}))

const brandRadios = document.querySelectorAll('input[type=radio][name="brand"]');
brandRadios.forEach((el)=> el.addEventListener('change', async ()=>{
    //브랜드 값 가지고있는곳
    const brand_checked = document.querySelector('input[name="brand"]:checked'); // 체크된 값(checked value)
    //type값 가지고 있는곳
    const type_checked = document.querySelector('input[name="area"]:checked'); // 체크된 값(checked value)
    //신상품, 최고주문순, 조회최다순 정보 가지고 있는 곳
    const value_checked = document.querySelector('.category_list_wrap ul li.active');

    await searchProduct(title_value.value,type_checked.value,brand_checked.value,'%',"1", value_checked.id, "desc");
}))


async function searchProduct (type, subtype, brand, name, stock, sort, desc) {
    // productType : 메인타입
    // productSubType : 서브타입
    // productBrand : 브랜드
    // productName : 이름
    // productStock : 재고수
    try{
        const url = `/api/product/list?productType=${type}&productSubType=${subtype}&productBrand=${brand}&productName=${name}&productStock=${stock}&page=0&sort=${sort}&order=${desc}`;
        const paginationUrl1 = `/api/product/list?productType=${type}&productSubType=${subtype}&productBrand=${brand}&productName=${name}&productStock=${stock}&page=`;
        const paginationUrl2 = `&sort=${sort}&order=des`;

        const res = await fetch(encodeURI(url));
        product_list.innerHTML = '';

        if(res.ok){
            const data = await res.json();
            console.log(url);
            console.log(data);
            if(data.length > 0){
                for (let i = 0; i < data.length; i++) {
                    product_list.innerHTML +=
                        `
                <tr>
                    <td class="item_images">
                    <img src="${data[i].productPhoto}" alt="">
                    </td>
                    <td class="title_price">
                    <p class=""><a href="/product/info?productId=${data[i].productId}">${data[i].productName}</a></p>
                    </td>
                    <td class="right_price">
                        <p>${data[i].productPrice}원</p>
                        <button class="add_cart" id="${data[i].productId}">담기</button>
                    </td>
                </tr>`
                }
                const buttons = document.querySelectorAll('.add_cart');
                for(const button of buttons){
                    button.addEventListener('click', async (event) => {
                        //담기 이벤트
                        const res = await fetch(`/api/product/item/${event.target.id}`);

                        if(res.ok){
                            const data = await res.json();
                            if(title_value.value === '바다로드'){
                                if(sea_rod_arr.some(item => item.id === event.target.id)){
                                    sea_rod_arr.map(value => {
                                        if(value.id === event.target.id){
                                            value.quantity += 1;
                                            value.price = (+value.price) + (+data.productPrice);
                                        }
                                    })
                                }else{
                                    sea_rod_arr.push({id:event.target.id, name: data.productName, price: data.productPrice, quantity:1})
                                }
                                sea_rod_id.innerHTML = '';
                                sea_rod_arr.forEach(el=>{
                                    sea_rod_id.innerHTML += `
                                        <div class="item d-flex justify-content-between ${el.id}">
                                             <div>
                                                <p class="w-100">${el.name}</p>
                                                <div>
                                                    <button class="minus-${el.id}">-</button>
                                                    <span class="quantity-${el.id}">${el.quantity}</span>
                                                    <button class="plus-${el.id}">+</button>
                                                </div>
                                            </div>
                                           <div class="d-flex align-items-center">
                                           <p class="cart_price">${el.price}<span>원</span></p>
                                            <button class="delete_item">X</button>
                                            </div>
                                        </div>
                                        `
                                })


                            }else if(title_value.value === '민물로드'){
                                fresh_rod_arr.push({id:event.target.id, name: data.productName, price: data.productPrice, quantity:1})
                            }else if(title_value.value === '원투낚시'){
                                one_throw_rod_arr.push({id:event.target.id, name: data.productName, price: data.productPrice, quantity:1})
                            }else if(title_value.value === '릴/용품'){
                                reel_arr.push({id:event.target.id, name: data.productName, price: data.productPrice, quantity:1})
                            }else if(title_value.value === '라인/용품'){
                                line_arr.push({id:event.target.id, name: data.productName, price: data.productPrice, quantity:1})
                            }else if(title_value.value === '바늘/훅'){
                                hook_arr.push({id:event.target.id, name: data.productName, price: data.productPrice, quantity:1})
                            }else if(title_value.value === '기타'){
                                all_arr.push({id:event.target.id, name: data.productName, price: data.productPrice, quantity:1})
                            }
                        }

                    })
                }
            }else{
                product_list.innerHTML =
                    `
                <tr>
                    <td class="item_images">
                    <img src="" alt="이미지없음">
                    </td>
                    <td class="title_price">
                    <p class=""><a href="">검색정보 없음</a></p>
                    </td>
                    <td class="right_price">
                        <p>정보없음</p>
                        <button class="" id="">담기</button>
                    </td>
                </tr>`
            }
            productRenderPagination(data[0].totalPage, encodeURI(url), encodeURI(paginationUrl1), encodeURI(paginationUrl2));
        }
    }catch (e) {
        product_list.innerHTML = '';
            product_list.innerHTML +=
                `
                <tr>
                    <td class="item_images">
                    <img src="" alt="이미지없음">
                    </td>
                    <td class="title_price">
                    <p class=""><a href="">정보없음</a></p>
                    </td>
                    <td class="right_price">
                        <p>정보없음</p>
                        <button class="" id="">담기</button>
                    </td>
                </tr>`
    }
}

// 신상품, 주문순, 조회순 버튼 클릭시 동작!
const view_wrap = document.querySelectorAll('.item_list_view .category_list_wrap ul li');
view_wrap.forEach((el)=> el.addEventListener('click', async () =>{
    view_wrap.forEach((ele)=> ele.classList.remove('active'));
    document.getElementById(el.id).classList.add('active');
    const brand_checked = document.querySelector('input[name="brand"]:checked'); // 체크된 값(checked value)
    const type_checked = document.querySelector('input[name="area"]:checked'); // 체크된 값(checked value)
    const text = searchText.value.trim().length === 0 ? '%' : searchText.value;

    await searchProduct(title_value.value, type_checked.value, brand_checked.value, text,"1", el.id, "des");
}));

//첫 로딩시 동작하는 곳
(async function(){
    function getParameterByName(name) {
        name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
        var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
            results = regex.exec(location.search);
        return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
    }
    const type = getParameterByName('type');

    const view_wrap = document.querySelectorAll('.item_list_view .category_list_wrap ul li');
    view_wrap.forEach((ele)=> ele.classList.remove('active'));
    document.getElementById(type).classList.add('active');

    await searchProduct(title_value.value,'%','%','%',"1", type, "des");
})();