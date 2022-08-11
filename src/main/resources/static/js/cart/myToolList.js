(async function () {
    await cartList();
})();

let go_pay = [];
// 내 장비 보여주는곳
const cart_list = document.querySelector('.cart-single-list');

//선택삭제 버튼
const select_delete = document.getElementById('tool_select_delete');
//가격 보여주는 곳
const cart_price = document.getElementById('cart_price');
//폴더 관리 누르면 보여주는 곳
const folder_manager = document.getElementById('folder_manager');
//주문하기 클릭 시
const order_btn = document.getElementById('order_btn');


async function cartList() {
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
                let all_price = 0;
                go_pay = [];
                // 폴더 아이디 값
                //console.log(event.target.value);
                const res = await fetch(`/api/user/myTool/detail/${event.target.value}`);
                if (res.ok) {
                    const data = await res.json();
                    console.log(data);
                    // myToolFolderId: 3
                    // myToolId: 4
                    // myToolProductBrand: "시마노"
                    // myToolProductId: 3
                    // myToolProductName: "시마노3번"
                    // myToolProductPrice: "1332"
                    // myToolProductSubType: "인쇼어"
                    // myToolProductType: "바다로드"
                    // myToolQuantity: 1
                    cart_list.innerHTML = '';
                    for (let i = 0; i < data.length; i++) {
                        go_pay.push({
                            productId: data[i].myToolProductId,
                            ordersQuantity: data[i].myToolQuantity,
                            ordersPrice: (+data[i].myToolProductPrice) + data[i].myToolQuantity
                        })
                        all_price += (+data[i].myToolProductPrice) * data[i].myToolQuantity;
                        cart_list.innerHTML += `
                                            <div class="cart_item row align-items-center mb-10" data-id="${data[i].myToolId}">
                        <div class="col-lg-1 col-md-1 col-12">
                            <input type="checkbox" name="tools" value="${data[i].myToolId}"/>
                        </div>
                        <div class="col-lg-4 col-md-3 col-12">
                            <h5 class="product-name"><a href="/product/info?productId=${data[i].myToolProductId}">
                                    ${data[i].myToolProductName}</a></h5>
                            <p class="product-des">
                                <span><em>분류:</em> ${data[i].myToolProductType}</span>
                                <span><em>브랜드:</em>${data[i].myToolProductBrand}</span>
                            </p>
                        </div>
                        <div class="col-lg-2 col-md-2 col-12">
                            <div class="count-input">
                                <span>${data[i].myToolQuantity}</span>
                            </div>
                        </div>
                        <div class="col-lg-2 col-md-2 col-12">
                            <p>${(+data[i].myToolProductPrice) + data[i].myToolQuantity}<span>원</span></p>
                        </div>
                        <div class="col-lg-2 col-md-2 col-12">
                            <p>${data[i].myToolProductSubType}</p>
                        </div>
                        <div class="col-lg-1 col-md-2 col-12">
                            <a class="remove-item" href="javascript:void(0)" data-id="${data[i].myToolId}"><i class="lni lni-close" data-id="${data[i].myToolId}"></i></a>
                        </div>
                    </div>
                        `
                    }
                    cart_price.innerHTML = `${all_price} 원`;

                    const button = document.querySelectorAll('.remove-item');
                    for (const remove of button) {
                        remove.addEventListener('click', async (clicked) => {
                            if (confirm("내 장비에서 삭제하시겠습니까?")) {
                                const res = await fetch(`/api/user/myTool/detail`, {
                                    method: 'DELETE',
                                    headers: {
                                        'header': header,
                                        'X-Requested-With': 'XMLHttpRequest',
                                        "Content-Type": "application/json",
                                        'X-CSRF-Token': token
                                    },
                                    body: JSON.stringify([{
                                        myToolId: clicked.target.dataset.id,
                                        myToolFolderId: event.target.value
                                    }])
                                })
                                if (res.ok) {
                                    alert('삭제되었습니다!');
                                    location.reload();
                                }
                            }
                        })
                    }

                    select_delete.addEventListener('click', async () => {
                        if (confirm("내 장비에서 삭제하시겠습니까?")) {
                            const cartArray = [];
                            const selectItem = document.querySelectorAll('input[name="tools"]:checked');
                            selectItem.forEach((el) => {
                                cartArray.push({myToolId: el.value, myToolFolderId: event.target.value})
                            })
                            const res = await fetch(`/api/user/myTool/detail`, {
                                method: 'DELETE',
                                headers: {
                                    'header': header,
                                    'X-Requested-With': 'XMLHttpRequest',
                                    "Content-Type": "application/json",
                                    'X-CSRF-Token': token
                                },
                                body: JSON.stringify(cartArray)
                            })
                            if (res.ok) {
                                alert('삭제되었습니다!');
                                location.reload();
                            }
                        }
                    })
                }
            })
        }
    }
}
order_btn.addEventListener('click', async () => {
    if(go_pay.length===0){
        alert('주문할 수 있는 상품이 없습니다!');
    }else{
        if(confirm("주문하시겠습니까?")){
            const res = await fetch(`/api/user/orders`,{
                method: 'POST',
                headers: {
                    'header': header,
                    'X-Requested-With': 'XMLHttpRequest',
                    "Content-Type": "application/json",
                    'X-CSRF-Token': token
                },
                body: JSON.stringify(go_pay)
            })

            if (res.ok){
                alert('주문이 완료되었습니다!');
                //주문내역으로 이동시키기
                location.replace('/index');
            }
        }
    }
})

// 자식창 열기( 폴더 생성 및 저장하는 곳)
folder_manager.addEventListener('click', () => {
    window.name = "폴더관리";
    openWin = window.open("myFolder", "childForm", "width=600px height=500, resizable = no, scrollbars = no");

});