//data[0].totalElement //총 개수
//data[0].totalPage // 페이지 그룹 개수
// 이전 버튼 : 화면에 그려진 첫번째 페이지 - 1
// 다음 버튼 : 화면에 그려진 마지막 페이지 + 1
const pagenation = document.querySelector('.text-center .pagination');


function productRenderPagination(currentPage, url, pURL1, pURL2) {
    pagenation.innerHTML = '';
    fetch(url)
        .then((res)=>res.json())
        .then((data)=>{
            //총 페이지 수
            const total = Math.ceil(data[0].totalElement/10);
            //화면에 보여질 페이지 그룹
            const group = Math.ceil(currentPage/10);

            //마지막 번호
            let last = group * 10;
            //만약 마지막 번호가 총 갯수보다 많다면 마지막 번호는 총 개수로
            if (last > total) last = total;
            let first = last - (10 - 1) <= 0 ? 1 : last - (10 - 1);
            let next = last + 1;
            let prev = first - 1;

            // console.log(`총 페이지 수 : ${total}`);
            // console.log(`보여질 페이지 그룹 : ${group}`);
            // console.log(`첫번째 페이지 : ${first}`);
            // console.log(`마지막 페이지 : ${last}`);
            // console.log(`다음 페이지 : ${next}`);
            // console.log(`이전 페이지 : ${prev}`);

            const fragmentPage = document.createDocumentFragment();
            if (prev > 0) {
                const allpreli = document.createElement('li');
                allpreli.classList.add('page-item');
                allpreli.insertAdjacentHTML("beforeend", `<a class="page-link" id='allprev'>&lt;&lt;</a>`);

                const preli = document.createElement('li');
                preli.insertAdjacentHTML("beforeend", `<a class="page-link" id='prev'>&lt;</a>`);
                preli.classList.add("page-item")

                fragmentPage.appendChild(allpreli);
                fragmentPage.appendChild(preli);
            }

            for (let i = first; i <= last; i++) {
                const li = document.createElement("li");
                li.insertAdjacentHTML("beforeend", `<a class="page-link" id='${i-1}'>${i}</a>`);
                li.classList.add("page-item")

                fragmentPage.appendChild(li);
            }

            if (last < total) {
                const allendli = document.createElement('li');
                allendli.classList.add("page-item")
                allendli.insertAdjacentHTML("beforeend", `<a class="page-link"  id='allnext'>&gt;&gt;</a>`);

                const endli = document.createElement('li');
                endli.classList.add("page-item");
                endli.insertAdjacentHTML("beforeend", `<a  class="page-link" id='next'>&gt;</a>`);

                fragmentPage.appendChild(endli);
                fragmentPage.appendChild(allendli);
            }

            pagenation.appendChild(fragmentPage);
            // 페이지 목록 생성

            // document.querySelector(".text-center .pagination li a").classList.remove("active");
            // document.querySelector(`.text-center .pagination li a#page-${currentPage}`).classList.add("active");

            const setnum = document.querySelectorAll(".text-center .pagination li a")
            for (const id of setnum) {
                id.addEventListener('click', function(e) {
                    e.preventDefault();
                    let item = e.target;
                    let id = item.id;
                    let selectedPage = item.innerText;

                    if (id === "next") selectedPage = next;
                    else if (id === "prev") selectedPage = prev;
                    else if (id === "allprev") selectedPage = 1;
                    else if (id === "allnext") selectedPage = total;
                    //페이지 그리는 함수
                    else productOnList(id ,pURL1, pURL2);



                    pagenation.innerHTML = "";
                    productRenderPagination(selectedPage, url, pURL1, pURL2);//페이지네이션 그리는 함수

                })
            }
        })
}

//페이지 번호 클릭 시 이동하는 곳
function productOnList(e, url1, url2){
    console.log(`${url1+e+url2}`)
    fetch(`${url1 + e + url2}`)
        .then((res)=>res.json())
        .then((data)=>{
            console.log(data);
            product_list.innerHTML = '';
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
                                            <button class="delete_item" value="${el.id}">X</button>
                                            </div>
                                        </div>
                                        `
                                    const minus_btn = document.querySelectorAll('.minus');
                                    for(const button of minus_btn){
                                        button.addEventListener('click', async (event)=>{
                                            const idValue = event.target.attributes.value.value;
                                            const quaintValue = document.querySelector(`.quantity-${idValue}`);
                                            const priceValue = document.querySelector(`.cart_price-${idValue}`);
                                            const priceRes = await fetch(`/api/product/item/${idValue}`);
                                            const priceData = await priceRes.json();
                                            sea_rod_arr.map(value => {
                                                if(value.id === idValue){
                                                    if(value.quantity === 1){
                                                        return;
                                                    }
                                                    value.quantity -= 1;
                                                    value.price = (+value.price) - (+priceData.productPrice);
                                                    quaintValue.textContent = value.quantity;
                                                    priceValue.innerHTML = `${value.price}<span>원</span>`;
                                                }
                                            })
                                        })
                                    }

                                    const plus_btn = document.querySelectorAll('.plus');
                                    for(const button of plus_btn){
                                        button.addEventListener('click', async (event)=>{
                                            const idValue = event.target.attributes.value.value;
                                            const quaintValue = document.querySelector(`.quantity-${idValue}`);
                                            const priceValue = document.querySelector(`.cart_price-${idValue}`);
                                            const priceRes = await fetch(`/api/product/item/${idValue}`);
                                            const priceData = await priceRes.json();
                                            sea_rod_arr.map(value => {
                                                if(value.id === idValue){
                                                    value.quantity += 1;
                                                    value.price = (+value.price) + (+priceData.productPrice);
                                                    quaintValue.textContent = value.quantity;
                                                    priceValue.innerHTML = `${value.price}<span>원</span>`;
                                                }
                                            })

                                        })
                                    }
                                })

                                const delete_btn = document.querySelectorAll('.delete_item');
                                for(const button of delete_btn){
                                    button.addEventListener('click', (event) => {
                                        const idValue = event.target.attributes.value.value;
                                        const $parent = document.querySelector(`.item-${idValue}`);
                                        sea_rod_arr = sea_rod_arr.filter(el => el.id !== idValue);
                                        $parent.remove();
                                    })
                                }


                            }
                            else if(title_value.value === '민물로드'){
                                if(fresh_rod_arr.some(item => item.id === event.target.id)){
                                    fresh_rod_arr.map(value => {
                                        if(value.id === event.target.id){
                                            value.quantity += 1;
                                            value.price = (+value.price) + (+data.productPrice);
                                        }
                                    })
                                }else{
                                    fresh_rod_arr.push({id:event.target.id, name: data.productName, price: data.productPrice, quantity:1})
                                }
                                fresh_rod_id.innerHTML = '';
                                fresh_rod_arr.forEach(el=>{
                                    fresh_rod_id.innerHTML += `
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
                                            <button class="delete_item" value="${el.id}">X</button>
                                            </div>
                                        </div>
                                        `
                                    const minus_btn = document.querySelectorAll('.minus');
                                    for(const button of minus_btn){
                                        button.addEventListener('click', async (event)=>{
                                            const idValue = event.target.attributes.value.value;
                                            const quaintValue = document.querySelector(`.quantity-${idValue}`);
                                            const priceValue = document.querySelector(`.cart_price-${idValue}`);
                                            const priceRes = await fetch(`/api/product/item/${idValue}`);
                                            const priceData = await priceRes.json();
                                            fresh_rod_arr.map(value => {
                                                if(value.id === idValue){
                                                    if(value.quantity === 1){
                                                        return;
                                                    }
                                                    value.quantity -= 1;
                                                    value.price = (+value.price) - (+priceData.productPrice);
                                                    quaintValue.textContent = value.quantity;
                                                    priceValue.innerHTML = `${value.price}<span>원</span>`;
                                                }
                                            })
                                        })
                                    }

                                    const plus_btn = document.querySelectorAll('.plus');
                                    for(const button of plus_btn){
                                        button.addEventListener('click', async (event)=>{
                                            const idValue = event.target.attributes.value.value;
                                            const quaintValue = document.querySelector(`.quantity-${idValue}`);
                                            const priceValue = document.querySelector(`.cart_price-${idValue}`);
                                            const priceRes = await fetch(`/api/product/item/${idValue}`);
                                            const priceData = await priceRes.json();
                                            fresh_rod_arr.map(value => {
                                                if(value.id === idValue){
                                                    value.quantity += 1;
                                                    value.price = (+value.price) + (+priceData.productPrice);
                                                    quaintValue.textContent = value.quantity;
                                                    priceValue.innerHTML = `${value.price}<span>원</span>`;
                                                }
                                            })

                                        })
                                    }
                                })

                                const delete_btn = document.querySelectorAll('.delete_item');
                                for(const button of delete_btn){
                                    button.addEventListener('click', (event) => {
                                        const idValue = event.target.attributes.value.value;
                                        const $parent = document.querySelector(`.item-${idValue}`);
                                        fresh_rod_arr = fresh_rod_arr.filter(el => el.id !== idValue);
                                        $parent.remove();
                                    })
                                }

                            } else if(title_value.value === '원투낚시'){
                                if(one_throw_rod_arr.some(item => item.id === event.target.id)){
                                    one_throw_rod_arr.map(value => {
                                        if(value.id === event.target.id){
                                            value.quantity += 1;
                                            value.price = (+value.price) + (+data.productPrice);
                                        }
                                    })
                                }else{
                                    one_throw_rod_arr.push({id:event.target.id, name: data.productName, price: data.productPrice, quantity:1})
                                }
                                one_throw_rod_id.innerHTML = '';
                                one_throw_rod_arr.forEach(el=>{
                                    one_throw_rod_id.innerHTML += `
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
                                            <button class="delete_item" value="${el.id}">X</button>
                                            </div>
                                        </div>
                                        `
                                    const minus_btn = document.querySelectorAll('.minus');
                                    for(const button of minus_btn){
                                        button.addEventListener('click', async (event)=>{
                                            const idValue = event.target.attributes.value.value;
                                            const quaintValue = document.querySelector(`.quantity-${idValue}`);
                                            const priceValue = document.querySelector(`.cart_price-${idValue}`);
                                            const priceRes = await fetch(`/api/product/item/${idValue}`);
                                            const priceData = await priceRes.json();
                                            one_throw_rod_arr.map(value => {
                                                if(value.id === idValue){
                                                    if(value.quantity === 1){
                                                        return;
                                                    }
                                                    value.quantity -= 1;
                                                    value.price = (+value.price) - (+priceData.productPrice);
                                                    quaintValue.textContent = value.quantity;
                                                    priceValue.innerHTML = `${value.price}<span>원</span>`;
                                                }
                                            })
                                        })
                                    }

                                    const plus_btn = document.querySelectorAll('.plus');
                                    for(const button of plus_btn){
                                        button.addEventListener('click', async (event)=>{
                                            const idValue = event.target.attributes.value.value;
                                            const quaintValue = document.querySelector(`.quantity-${idValue}`);
                                            const priceValue = document.querySelector(`.cart_price-${idValue}`);
                                            const priceRes = await fetch(`/api/product/item/${idValue}`);
                                            const priceData = await priceRes.json();
                                            one_throw_rod_arr.map(value => {
                                                if(value.id === idValue){
                                                    value.quantity += 1;
                                                    value.price = (+value.price) + (+priceData.productPrice);
                                                    quaintValue.textContent = value.quantity;
                                                    priceValue.innerHTML = `${value.price}<span>원</span>`;
                                                }
                                            })

                                        })
                                    }
                                })

                                const delete_btn = document.querySelectorAll('.delete_item');
                                for(const button of delete_btn){
                                    button.addEventListener('click', (event) => {
                                        const idValue = event.target.attributes.value.value;
                                        const $parent = document.querySelector(`.item-${idValue}`);
                                        one_throw_rod_arr = one_throw_rod_arr.filter(el => el.id !== idValue);
                                        $parent.remove();
                                    })
                                }
                            }else if(title_value.value === '릴/용품'){
                                if(reel_arr.some(item => item.id === event.target.id)){
                                    reel_arr.map(value => {
                                        if(value.id === event.target.id){
                                            value.quantity += 1;
                                            value.price = (+value.price) + (+data.productPrice);
                                        }
                                    })
                                }else{
                                    reel_arr.push({id:event.target.id, name: data.productName, price: data.productPrice, quantity:1})
                                }
                                reel_id.innerHTML = '';
                                reel_arr.forEach(el=>{
                                    reel_id.innerHTML += `
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
                                            <button class="delete_item" value="${el.id}">X</button>
                                            </div>
                                        </div>
                                        `
                                    const minus_btn = document.querySelectorAll('.minus');
                                    for(const button of minus_btn){
                                        button.addEventListener('click', async (event)=>{
                                            const idValue = event.target.attributes.value.value;
                                            const quaintValue = document.querySelector(`.quantity-${idValue}`);
                                            const priceValue = document.querySelector(`.cart_price-${idValue}`);
                                            const priceRes = await fetch(`/api/product/item/${idValue}`);
                                            const priceData = await priceRes.json();
                                            reel_arr.map(value => {
                                                if(value.id === idValue){
                                                    if(value.quantity === 1){
                                                        return;
                                                    }
                                                    value.quantity -= 1;
                                                    value.price = (+value.price) - (+priceData.productPrice);
                                                    quaintValue.textContent = value.quantity;
                                                    priceValue.innerHTML = `${value.price}<span>원</span>`;
                                                }
                                            })
                                        })
                                    }

                                    const plus_btn = document.querySelectorAll('.plus');
                                    for(const button of plus_btn){
                                        button.addEventListener('click', async (event)=>{
                                            const idValue = event.target.attributes.value.value;
                                            const quaintValue = document.querySelector(`.quantity-${idValue}`);
                                            const priceValue = document.querySelector(`.cart_price-${idValue}`);
                                            const priceRes = await fetch(`/api/product/item/${idValue}`);
                                            const priceData = await priceRes.json();
                                            reel_arr.map(value => {
                                                if(value.id === idValue){
                                                    value.quantity += 1;
                                                    value.price = (+value.price) + (+priceData.productPrice);
                                                    quaintValue.textContent = value.quantity;
                                                    priceValue.innerHTML = `${value.price}<span>원</span>`;
                                                }
                                            })

                                        })
                                    }
                                })

                                const delete_btn = document.querySelectorAll('.delete_item');
                                for(const button of delete_btn){
                                    button.addEventListener('click', (event) => {
                                        const idValue = event.target.attributes.value.value;
                                        const $parent = document.querySelector(`.item-${idValue}`);
                                        reel_arr = reel_arr.filter(el => el.id !== idValue);
                                        $parent.remove();
                                    })
                                }
                            }else if(title_value.value === '라인/용품'){
                                if(line_arr.some(item => item.id === event.target.id)){
                                    line_arr.map(value => {
                                        if(value.id === event.target.id){
                                            value.quantity += 1;
                                            value.price = (+value.price) + (+data.productPrice);
                                        }
                                    })
                                }else{
                                    line_arr.push({id:event.target.id, name: data.productName, price: data.productPrice, quantity:1})
                                }
                                line_id.innerHTML = '';
                                line_arr.forEach(el=>{
                                    line_id.innerHTML += `
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
                                            <button class="delete_item" value="${el.id}">X</button>
                                            </div>
                                        </div>
                                        `
                                    const minus_btn = document.querySelectorAll('.minus');
                                    for(const button of minus_btn){
                                        button.addEventListener('click', async (event)=>{
                                            const idValue = event.target.attributes.value.value;
                                            const quaintValue = document.querySelector(`.quantity-${idValue}`);
                                            const priceValue = document.querySelector(`.cart_price-${idValue}`);
                                            const priceRes = await fetch(`/api/product/item/${idValue}`);
                                            const priceData = await priceRes.json();
                                            line_arr.map(value => {
                                                if(value.id === idValue){
                                                    if(value.quantity === 1){
                                                        return;
                                                    }
                                                    value.quantity -= 1;
                                                    value.price = (+value.price) - (+priceData.productPrice);
                                                    quaintValue.textContent = value.quantity;
                                                    priceValue.innerHTML = `${value.price}<span>원</span>`;
                                                }
                                            })
                                        })
                                    }

                                    const plus_btn = document.querySelectorAll('.plus');
                                    for(const button of plus_btn){
                                        button.addEventListener('click', async (event)=>{
                                            const idValue = event.target.attributes.value.value;
                                            const quaintValue = document.querySelector(`.quantity-${idValue}`);
                                            const priceValue = document.querySelector(`.cart_price-${idValue}`);
                                            const priceRes = await fetch(`/api/product/item/${idValue}`);
                                            const priceData = await priceRes.json();
                                            line_arr.map(value => {
                                                if(value.id === idValue){
                                                    value.quantity += 1;
                                                    value.price = (+value.price) + (+priceData.productPrice);
                                                    quaintValue.textContent = value.quantity;
                                                    priceValue.innerHTML = `${value.price}<span>원</span>`;
                                                }
                                            })

                                        })
                                    }
                                })

                                const delete_btn = document.querySelectorAll('.delete_item');
                                for(const button of delete_btn){
                                    button.addEventListener('click', (event) => {
                                        const idValue = event.target.attributes.value.value;
                                        const $parent = document.querySelector(`.item-${idValue}`);
                                        line_arr = line_arr.filter(el => el.id !== idValue);
                                        $parent.remove();
                                    })
                                }
                            }else if(title_value.value === '바늘/훅'){
                                if(hook_arr.some(item => item.id === event.target.id)){
                                    hook_arr.map(value => {
                                        if(value.id === event.target.id){
                                            value.quantity += 1;
                                            value.price = (+value.price) + (+data.productPrice);
                                        }
                                    })
                                }else{
                                    hook_arr.push({id:event.target.id, name: data.productName, price: data.productPrice, quantity:1})
                                }
                                hook_id.innerHTML = '';
                                hook_arr.forEach(el=>{
                                    hook_id.innerHTML += `
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
                                            <button class="delete_item" value="${el.id}">X</button>
                                            </div>
                                        </div>
                                        `
                                    const minus_btn = document.querySelectorAll('.minus');
                                    for(const button of minus_btn){
                                        button.addEventListener('click', async (event)=>{
                                            const idValue = event.target.attributes.value.value;
                                            const quaintValue = document.querySelector(`.quantity-${idValue}`);
                                            const priceValue = document.querySelector(`.cart_price-${idValue}`);
                                            const priceRes = await fetch(`/api/product/item/${idValue}`);
                                            const priceData = await priceRes.json();
                                            hook_arr.map(value => {
                                                if(value.id === idValue){
                                                    if(value.quantity === 1){
                                                        return;
                                                    }
                                                    value.quantity -= 1;
                                                    value.price = (+value.price) - (+priceData.productPrice);
                                                    quaintValue.textContent = value.quantity;
                                                    priceValue.innerHTML = `${value.price}<span>원</span>`;
                                                }
                                            })
                                        })
                                    }

                                    const plus_btn = document.querySelectorAll('.plus');
                                    for(const button of plus_btn){
                                        button.addEventListener('click', async (event)=>{
                                            const idValue = event.target.attributes.value.value;
                                            const quaintValue = document.querySelector(`.quantity-${idValue}`);
                                            const priceValue = document.querySelector(`.cart_price-${idValue}`);
                                            const priceRes = await fetch(`/api/product/item/${idValue}`);
                                            const priceData = await priceRes.json();
                                            hook_arr.map(value => {
                                                if(value.id === idValue){
                                                    value.quantity += 1;
                                                    value.price = (+value.price) + (+priceData.productPrice);
                                                    quaintValue.textContent = value.quantity;
                                                    priceValue.innerHTML = `${value.price}<span>원</span>`;
                                                }
                                            })

                                        })
                                    }
                                })

                                const delete_btn = document.querySelectorAll('.delete_item');
                                for(const button of delete_btn){
                                    button.addEventListener('click', (event) => {
                                        const idValue = event.target.attributes.value.value;
                                        const $parent = document.querySelector(`.item-${idValue}`);
                                        hook_arr = hook_arr.filter(el => el.id !== idValue);
                                        $parent.remove();
                                    })
                                }
                            }else if(title_value.value === '기타'){
                                if(all_arr.some(item => item.id === event.target.id)){
                                    all_arr.map(value => {
                                        if(value.id === event.target.id){
                                            value.quantity += 1;
                                            value.price = (+value.price) + (+data.productPrice);
                                        }
                                    })
                                }else{
                                    all_arr.push({id:event.target.id, name: data.productName, price: data.productPrice, quantity:1})
                                }
                                all_id.innerHTML = '';
                                all_arr.forEach(el=>{
                                    all_id.innerHTML += `
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
                                            <button class="delete_item" value="${el.id}">X</button>
                                            </div>
                                        </div>
                                        `
                                    const minus_btn = document.querySelectorAll('.minus');
                                    for(const button of minus_btn){
                                        button.addEventListener('click', async (event)=>{
                                            const idValue = event.target.attributes.value.value;
                                            const quaintValue = document.querySelector(`.quantity-${idValue}`);
                                            const priceValue = document.querySelector(`.cart_price-${idValue}`);
                                            const priceRes = await fetch(`/api/product/item/${idValue}`);
                                            const priceData = await priceRes.json();
                                            all_arr.map(value => {
                                                if(value.id === idValue){
                                                    if(value.quantity === 1){
                                                        return;
                                                    }
                                                    value.quantity -= 1;
                                                    value.price = (+value.price) - (+priceData.productPrice);
                                                    quaintValue.textContent = value.quantity;
                                                    priceValue.innerHTML = `${value.price}<span>원</span>`;
                                                }
                                            })
                                        })
                                    }

                                    const plus_btn = document.querySelectorAll('.plus');
                                    for(const button of plus_btn){
                                        button.addEventListener('click', async (event)=>{
                                            const idValue = event.target.attributes.value.value;
                                            const quaintValue = document.querySelector(`.quantity-${idValue}`);
                                            const priceValue = document.querySelector(`.cart_price-${idValue}`);
                                            const priceRes = await fetch(`/api/product/item/${idValue}`);
                                            const priceData = await priceRes.json();
                                            all_arr.map(value => {
                                                if(value.id === idValue){
                                                    value.quantity += 1;
                                                    value.price = (+value.price) + (+priceData.productPrice);
                                                    quaintValue.textContent = value.quantity;
                                                    priceValue.innerHTML = `${value.price}<span>원</span>`;
                                                }
                                            })

                                        })
                                    }
                                })

                                const delete_btn = document.querySelectorAll('.delete_item');
                                for(const button of delete_btn){
                                    button.addEventListener('click', (event) => {
                                        const idValue = event.target.attributes.value.value;
                                        const $parent = document.querySelector(`.item-${idValue}`);
                                        all_arr = all_arr.filter(el => el.id !== idValue);
                                        $parent.remove();
                                    })
                                }
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
        })
}