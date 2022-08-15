// 상품이 담길 곳
let sea_rod_arr = [];
let fresh_rod_arr = [];
let one_throw_rod_arr = [];
let reel_arr = [];
let line_arr = [];
let hook_arr = [];
let all_arr = []

//총 가격 보여주는 곳
let all_price = 0;
//내장비 담기 하면 들어가 공간
const sea_rod_id = document.getElementById('sea_rod_panel');
const fresh_rod_id = document.getElementById('fresh_rod_panel');
const one_throw_rod_id = document.getElementById('one_throw_rod_panel');
const reel_id = document.getElementById('reel_panel');
const line_id = document.getElementById('line_panel');
const hook_id = document.getElementById('hook_panel');
const all_id = document.getElementById('all_panel');

//총 가격 태그
const $all_price = document.getElementById('all_price');

async function toolsList() {
    // 내 장비 목록 뿌려주는 곳
    const res = await fetch('/api/user/myTool/folder');
    if (res.ok) {
        const data = await res.json();
        const tools_list = document.getElementById('tools_list');
//             // 내 장비 추가부분
//             // myToolFolderId: 1
//             // myToolFolderName: "내장비 1"
        tools_list.innerHTML = '';
        for (let i = 0; i < data.length; i++) {
            tools_list.innerHTML += `
            <button class="card bg-primary mx-1 d-flex justify-content-center align-items-center rounded-pill" value="${data[i].myToolFolderId}">${data[i].myToolFolderName}</button>
                `
        }
        const select_tool = document.querySelectorAll('.card');
        for (const button of select_tool) {
            button.addEventListener('click', async (event) => {
                all_price = 0;
                // 폴더 아이디 값
                //console.log(event.target.value);
                const res = await fetch(`/api/user/myTool/detail/${event.target.value}`);
                if (res.ok) {
                    const data = await res.json();
                    // myToolFolderId: 3
                    // myToolId: 4
                    // myToolProductBrand: "시마노"
                    // myToolProductId: 3
                    // myToolProductName: "시마노3번"
                    // myToolProductPrice: "1332"
                    // myToolProductSubType: "인쇼어"
                    // myToolProductType: "바다로드"
                    // myToolQuantity: 1

                    sea_rod_arr = [];
                    fresh_rod_arr = [];
                    one_throw_rod_arr = [];
                    reel_arr = [];
                    line_arr = [];
                    hook_arr = [];
                    all_arr = [];


                    for (let i = 0; i < data.length; i++) {
                        all_price += (+data[i].myToolProductPrice) * (+data[i].myToolQuantity);

                        if (data[i].myToolProductType === "바다로드") {
                            sea_rod_arr.push({
                                id: data[i].myToolProductId,
                                name: data[i].myToolProductName,
                                price: (+data[i].myToolProductPrice) * (+data[i].myToolQuantity),
                                quantity: data[i].myToolQuantity
                            });
                        } else if (data[i].myToolProductType === "민물로드") {
                            fresh_rod_arr.push({
                                id: data[i].myToolProductId,
                                name: data[i].myToolProductName,
                                price: (+data[i].myToolProductPrice) * (+data[i].myToolQuantity),
                                quantity: data[i].myToolQuantity
                            });
                        } else if (data[i].myToolProductType === "원투낚시") {
                            one_throw_rod_arr.push({
                                id: data[i].myToolProductId,
                                name: data[i].myToolProductName,
                                price: (+data[i].myToolProductPrice) * (+data[i].myToolQuantity),
                                quantity: data[i].myToolQuantity
                            });
                        } else if (data[i].myToolProductType === "릴/용품") {
                            reel_arr.push({
                                id: data[i].myToolProductId,
                                name: data[i].myToolProductName,
                                price: (+data[i].myToolProductPrice) * (+data[i].myToolQuantity),
                                quantity: data[i].myToolQuantity
                            });
                        } else if (data[i].myToolProductType === "라인/용품") {
                            line_arr.push({
                                id: data[i].myToolProductId,
                                name: data[i].myToolProductName,
                                price: (+data[i].myToolProductPrice) * (+data[i].myToolQuantity),
                                quantity: data[i].myToolQuantity
                            });
                        } else if (data[i].myToolProductType === "바늘/훅") {
                            hook_arr.push({
                                id: data[i].myToolProductId,
                                name: data[i].myToolProductName,
                                price: (+data[i].myToolProductPrice) * (+data[i].myToolQuantity),
                                quantity: data[i].myToolQuantity
                            });
                        } else if (data[i].myToolProductType === "기타") {
                            all_arr.push({
                                id: data[i].myToolProductId,
                                name: data[i].myToolProductName,
                                price: (+data[i].myToolProductPrice) * (+data[i].myToolQuantity),
                                quantity: data[i].myToolQuantity
                            });
                        }
                    }

                    itemSetting(sea_rod_id, sea_rod_arr, "바다로드");
                    itemSetting(fresh_rod_id, fresh_rod_arr, "민물로드");
                    itemSetting(one_throw_rod_id, one_throw_rod_arr, "원투낚시");
                    itemSetting(reel_id, reel_arr, "릴/용품");
                    itemSetting(line_id, line_arr, "라인/용품");
                    itemSetting(hook_id, hook_arr, "바늘/훅");
                    itemSetting(all_id, all_arr, "기타");

                    $all_price.innerHTML = `${all_price} <span>원</span>`;
                }
            })
        }
    }
}

let settingPrice = 0;

function itemSetting(wrapId, eleArr, arrName) {
    wrapId.innerHTML = '';
    eleArr.forEach((el) => {
        settingPrice += el.price;
        wrapId.innerHTML += `
                                        <div class="item item-${el.id} d-flex justify-content-between ">
                                             <div>
                                                <p class="w-100">${el.name}</p>
                                                <div>
                                                    <button class="minus ${el.id}" value="${el.id}">-</button>
                                                    <span class="quantity-${el.id}">${el.quantity}</span>
                                                    <button class="plus ${el.id}" value="${el.id}">+</button>
                                                </div>
                                            </div>
                                           <div class="d-flex align-items-center">
                                           <p class="cart_price-${el.id}">${el.price}<span>원</span></p>
                                            <button class="delete_item" value="${el.id}" data-value="${arrName}">X</button>
                                            </div>
                                        </div>
                                        `
        const minus_btn = document.querySelectorAll('.minus');
        for (const button of minus_btn) {
            button.addEventListener('click', async (event) => {
                const idValue = event.target.attributes.value.value;
                const quaintValue = document.querySelector(`.quantity-${idValue}`);
                const priceValue = document.querySelector(`.cart_price-${idValue}`);
                const priceRes = await fetch(`/api/product/item/${idValue}`);
                const priceData = await priceRes.json();
                if (priceRes.ok) {
                    eleArr.map(value => {
                        if (value.id === +idValue) {
                            if (value.quantity === 1) {
                                return;
                            }
                            all_price -= (+priceData.productPrice);
                            $all_price.innerHTML = `${all_price} <span>원</span>`;
                            value.quantity -= 1;
                            value.price = (+value.price) - (+priceData.productPrice);
                            quaintValue.textContent = value.quantity;
                            priceValue.innerHTML = `${value.price}<span>원</span>`;
                        }
                    })
                }
            })
        }

        const plus_btn = document.querySelectorAll('.plus');
        for (const button of plus_btn) {
            button.addEventListener('click', async (event) => {
                const idValue = event.target.attributes.value.value;
                const quaintValue = document.querySelector(`.quantity-${idValue}`);
                const priceValue = document.querySelector(`.cart_price-${idValue}`);
                const priceRes = await fetch(`/api/product/item/${idValue}`);
                const priceData = await priceRes.json();
                if (priceRes.ok) {
                    eleArr.map(value => {
                        if (value.id === +idValue) {
                            all_price += (+priceData.productPrice);
                            $all_price.innerHTML = `${all_price} <span>원</span>`;
                            value.quantity += 1;
                            value.price = (+value.price) + (+priceData.productPrice);
                            quaintValue.textContent = value.quantity;
                            priceValue.innerHTML = `${value.price}<span>원</span>`;
                        }
                    })
                }
            })
        }
        let delete_sum = 0;

        const delete_btn = document.querySelectorAll('.delete_item');
        for (const button of delete_btn) {
            button.addEventListener('click', async (event) => {

                const idValue = event.target.attributes.value.value;
                const $parent = document.querySelector(`.item-${idValue}`);
                if (event.target.dataset.value === "바다로드") {
                    sea_rod_arr = sea_rod_arr.filter(el => {
                        if(el.id === +idValue){
                            all_price -= (+el.price);
                        }
                        return el.id !== +idValue
                    });
                    $all_price.innerHTML = `${all_price}<span>원</span>`;
                } else if (event.target.dataset.value === "민물로드") {
                    fresh_rod_arr = fresh_rod_arr.filter(el => {
                        if(el.id === +idValue){
                            all_price -= (+el.price);
                        }
                        return el.id !== +idValue
                    });
                    $all_price.innerHTML = `${all_price}<span>원</span>`;
                } else if (event.target.dataset.value === "원투낚시") {
                    one_throw_rod_arr = one_throw_rod_arr.filter(el => {
                        if(el.id === +idValue){
                            all_price -= (+el.price);
                        }
                        return el.id !== +idValue
                    });
                    $all_price.innerHTML = `${all_price}<span>원</span>`;
                } else if (event.target.dataset.value === "릴/용품") {
                    reel_arr = reel_arr.filter(el => {
                        if(el.id === +idValue){
                            all_price -= (+el.price);
                        }
                        return el.id !== +idValue
                    });
                    $all_price.innerHTML = `${all_price}<span>원</span>`;
                } else if (event.target.dataset.value === "라인/용품") {
                    line_arr = line_arr.filter(el => {
                        if(el.id === +idValue){
                            all_price -= (+el.price);
                        }
                        return el.id !== +idValue
                    });
                    $all_price.innerHTML = `${all_price}<span>원</span>`;
                } else if (event.target.dataset.value === "바늘/훅") {
                    hook_arr = hook_arr.filter(el => {
                        if(el.id === +idValue){
                            all_price -= (+el.price);
                        }
                        return el.id !== +idValue
                    });
                    $all_price.innerHTML = `${all_price}<span>원</span>`;
                } else if (event.target.dataset.value === "기타") {
                    all_arr = all_arr.filter(el => {
                        if(el.id === +idValue){
                            all_price -= (+el.price);
                        }
                        return el.id !== +idValue
                    });
                    $all_price.innerHTML = `${all_price}<span>원</span>`;
                }
                $parent.remove();
            })
        }
    })
}