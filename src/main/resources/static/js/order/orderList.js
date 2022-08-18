const orderList = document.getElementById('orderList');

function changeStatusType(){
    const orderStatus = document.getElementById('order-status');
    const orderStatusValue = orderStatus.options[orderStatus.selectedIndex].value;
    console.log(orderStatusValue);
}

async function myOrdersList(startDate, endDate){
    const res = await fetch('/api/user/orders/list?startDate=2022-05-01T00:00&endDate=2300-06-14T23:59&productName=%25&productStock=1&page=0');

    if(res.ok){
        // companyName: "좋은회사"
        // orderStatus: "상품준비중"
        // ordersDate: "2022-08-10T21:40:22.258953"
        // ordersId: 4
        // ordersPrice: 301
        // ordersQuantity: 1
        // productBrand: "시마노"
        // productId: 14
        // productName: "카본라인1"
        // totalElement: 4
        // totalPage: 1
        const data = await res.json();
        for(let i in data){
            console.log(data[i]);
            // 후기 있는지 없는지 여부 체크
            const checkRes = await fetch(`/api/user/review/check/${data[i].ordersId}`);
            const checkData = await checkRes.json();
            if(checkData.id === 0){
                console.log(checkData);
                orderList.innerHTML += `
                                <div class="row align-items-center mb-10">
                                    <div class="col-lg-1 col-md-1 col-12">
                                        <p class="order_num">
                                            ${data[i].ordersId}
                                        </p>
                                    </div>
                                    <div class="col-lg-1 col-md-1 col-12">
                                        <h6 class="product-name">
                                            ${data[i].companyName}
                                        </h6>
                                    </div>
                                    <div class="col-lg-1 col-md-1 col-12">
                                        <p>${data[i].productBrand}</p>
                                    </div>
                                    <div class="col-lg-3 col-md-3 col-12">
                                        <a href="/product/info?productId=${data[i].productId}">
                                            <p>${data[i].productName}</p>
                                        </a>
                                    </div>
                                    <div class="col-lg-1 col-md-1 col-12">
                                        <p>${data[i].ordersQuantity}</p>
                                    </div>
                                    <div class="col-lg-2 col-md-2 col-12">
                                        <p>${data[i].ordersPrice}원</p>
                                    </div>
                                    <div class="col-lg-2 col-md-3 col-12">
                                        ${data[i].orderStatus}
                                    </div>
                                    <div class="col-lg-1 col-md-2 col-12">
                                        <button class="order_review" data-id=${data[i].productId} data-value="${data[i].ordersId}">리뷰작성</button>
                                        <button class="order_change">교환신청</button>
                                        <button class="order_return">반품신청</button>
                                    </div>
                                </div>

            `

            }else{
                //리뷰가 있으면
                orderList.innerHTML += `
                                <div class="row align-items-center mb-10">
                                    <div class="col-lg-1 col-md-1 col-12">
                                        <p class="order_num">
                                            ${data[i].ordersId}
                                        </p>
                                    </div>
                                    <div class="col-lg-1 col-md-1 col-12">
                                        <h6 class="product-name">
                                            ${data[i].companyName}
                                        </h6>
                                    </div>
                                    <div class="col-lg-1 col-md-1 col-12">
                                        <p>${data[i].productBrand}</p>
                                    </div>
                                    <div class="col-lg-3 col-md-3 col-12">
                                        <a href="/product/info?productId=${data[i].productId}">
                                            <p>${data[i].productName}</p>
                                        </a>
                                    </div>
                                    <div class="col-lg-1 col-md-1 col-12">
                                        <p>${data[i].ordersQuantity}</p>
                                    </div>
                                    <div class="col-lg-2 col-md-2 col-12">
                                        <p>${data[i].ordersPrice}원</p>
                                    </div>
                                    <div class="col-lg-2 col-md-3 col-12">
                                        ${data[i].orderStatus}
                                    </div>
                                    <div class="col-lg-1 col-md-2 col-12">
                                        <button class="order_reviewChange" data-id=${data[i].productId} data-value="${data[i].ordersId}">리뷰수정</button>
                                        <button class="order_change">교환신청</button>
                                        <button class="order_return">반품신청</button>
                                    </div>
                                </div>

            `
            }
        }

        const order_review = document.querySelectorAll('.order_review');
        for(const button of order_review){
            button.addEventListener('click', (event) => {
                console.log(event.target.dataset.value);
                console.log(event.target.dataset.id);
                location.replace(`/product/info?productId=${event.target.dataset.id}&orderId=${event.target.dataset.value}`)
            })
        }

        const order_reviewChange = document.querySelectorAll('.order_reviewChange');
        for(const button of order_reviewChange){
            button.addEventListener('click', (event) => {
                console.log(event.target.dataset.value);
                console.log(event.target.dataset.id);
                location.replace(`/product/info?productId=${event.target.dataset.id}&orderId=${event.target.dataset.value}`)
            })
        }
    }
}


(async () =>{
    await myOrdersList();
})();