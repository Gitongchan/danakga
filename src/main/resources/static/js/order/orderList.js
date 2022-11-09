const orderList = document.getElementById('orderList');

function changeStatusType(){
    const orderStatus = document.getElementById('order-status');
    const orderStatusValue = orderStatus.options[orderStatus.selectedIndex].value;
    console.log(orderStatusValue);
}
const $resetBtn = document.getElementById('reset-date');
const $searchBtn = document.getElementById('search-date');
const $orderList = document.getElementById('orderList');
const sDate = document.getElementById('startDate');
const eDate = document.getElementById('endDate');

//초기화 버튼
$resetBtn.addEventListener('click', async () => {
    $orderList.innerHTML = '';
    sDate.value = '';
    eDate.value = '';

    await myOrdersList('2022-05-01T00:00', '2322-06-12T22:00', 0);
    const res = await fetch(`/api/user/orders/list?startDate=2022-05-01T00:00&endDate=2322-06-12T00:00&page=0`);
    if(res.ok) {
        const data = await res.json();
        renderOrderPagination(data[0].totalPage, data[0].totalElement);
    }
})


//검색버튼
$searchBtn.addEventListener('click', async () => {
    const res = await fetch(`/api/user/orders/list?startDate=${sDate.value}&endDate=${eDate.value}&page=0`);
    if(res.ok){
        const data = await res.json();
        await myOrdersList(sDate.value, eDate.value, 0);
        renderOrderPagination(data[0].totalPage, data[0].totalElement);
    }
})

async function myOrdersList(startDate = '2022-05-01T00:00', endDate = '2322-06-12T00:00', page=0){
    const res = await fetch(`/api/user/orders/list?startDate=${startDate}&endDate=${endDate}&page=${page}`);
    orderList.innerHTML = '';
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
        // totalElement: 4정
        // totalPage: 1

        //상품 준비중일때는 사업자는 배송시작,주문거절 / 일반사용자는 주문취소만 가능
        //주문취소,반품신청일때 사업자는 "환불완료" 처리만 가능
        //배송완료면 교환신청,반품신청,구매확정
        //교환신청 일때는 사업자는 "교환상품배송"으로 처리, 운송장번호 같이입력  , 이후 사업자는 배송완료 처리 해주어야한다
        //기본 적인 경우 상품준비중-> 배송시작(운송장번호 입력) -> 배송완료(완료일자 자동입력) -> 구매확정
        //구매확정 일때만 리뷰가능

        //     READY("상품준비중"), CANCEL("주문취소"), EXCHANGE("교환신청") , RETURN("반품신청") ,CONFIRM("구매확정")
        //     ,START("배송시작"), FINISH("배송완료"), REFUND("환불완료") , REDELIVERY("교환상품배송");

        const data = await res.json();
        for(let i in data){
            // 후기 있는지 없는지 여부 체크
            const checkRes = await fetch(`/api/user/review/check/${data[i].ordersId}`);
            const checkData = await checkRes.json();
            console.log(checkData);
            //후기를 작성할 수 있다면 0, 후기를 수정할 수 있다면 1, 후기가 없다면 -1
            if(checkData.id !== 1 ){
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
                                    <div class="col-lg-1 col-md-2 col-12 info_wrap status_wrap${data[i].ordersId}">
                                        
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
                                    <div class="col-lg-1 col-md-2 col-12 d-flex info_wrap status_wrap${data[i].ordersId}">
                                        <button class="order_reviewChange" data-id=${data[i].productId} data-value="${data[i].ordersId}">리뷰수정</button>
                                    </div>
                                </div>

            `
            }

            if (data[i].orderStatus === "상품준비중") {
                document.querySelector(`.status_wrap${data[i].ordersId}`).innerHTML+= `
                <button class="order_cancel" data-id="${data[i].ordersId}">주문취소</button>
                `;
            }else if (data[i].orderStatus === "배송시작") {
                document.querySelector(`.status_wrap${data[i].ordersId}`).innerHTML+= `
                <button class="order_trackingNum" data-id="${data[i].ordersId}">운송장조회</button>
                `;
            } else if (data[i].orderStatus === "배송완료") {
                document.querySelector(`.status_wrap${data[i].ordersId}`).innerHTML+= `
                <button class="order_confirm" data-id="${data[i].ordersId}" data-value="${data[i].orderStatus}">구매확정</button>
                <button class="order_change" data-id="${data[i].ordersId}" data-value="${data[i].orderStatus}">교환신청</button>
                <button class="order_return" data-id="${data[i].ordersId}" data-value="${data[i].orderStatus}">반품신청</button>
                `;
            }else if (data[i].orderStatus === "구매확정" && checkData.id === 0) {
                document.querySelector(`.status_wrap${data[i].ordersId}`).innerHTML+= `
                <button class="order_review" data-id=${data[i].productId} data-value=${data[i].ordersId}>리뷰작성</button>
                `;
            }else if(data[i].orderStatus === "교환상품배송") {
                document.querySelector(`.status_wrap${data[i].ordersId}`).innerHTML+= `
                <button class="order_trackingNum" data-id="${data[i].ordersId}">운송장조회</button>
                `;
            }
        }

        //주문취소
        const order_cancel = document.querySelectorAll('.order_cancel');
        for(const button of order_cancel){
            button.addEventListener('click', async (event) => {
                //주문번호
                console.log(event.target.dataset.id);
                const res = await fetch(`/api/user/orders/list/detail/${event.target.dataset.id}`);
                if(res.ok){
                    const data = await res.json();
                    if(confirm("주문을 취소하시겠습니까?")){
                        const res = await fetch(`/api/user/orders/updateStatus/${event.target.dataset.id}`,{
                            method: 'PUT',
                            headers: {
                                'header': header,
                                'X-Requested-With': 'XMLHttpRequest',
                                "Content-Type": "application/json",
                                'X-CSRF-Token': token
                            },
                            body: JSON.stringify({ordersTrackingNum:data.ordersTrackingNum, changeOrdersStatus:"주문취소"})
                        })
                        if(res.ok){
                            alert("취소가 완료되었습니다!");
                            location.reload();
                        }
                    }
                }
            })
        }

        //운송장 조회하기
        const order_trackingNum = document.querySelectorAll('.order_trackingNum');
        for(const button of order_trackingNum){
            button.addEventListener('click', async (event) => {
                //주문번호
                console.log(event.target.dataset.id);
                const res = await fetch(`/api/user/orders/list/detail/${event.target.dataset.id}`);
                if(res.ok){
                    const data = await res.json();
                    alert("운송장 번호:" + data.ordersTrackingNum);
                }
            })
        }

        //리뷰 작성하러 가기
        const order_review = document.querySelectorAll('.order_review');
        for(const button of order_review){
            button.addEventListener('click', (event) => {
                console.log(event.target.dataset.value);
                console.log(event.target.dataset.id);
                if(confirm("리뷰작성를 하시겠습니까?")){
                    location.replace(`/product/info?productId=${event.target.dataset.id}&orderId=${event.target.dataset.value}`)
                }
            })
        }

        //구매확정
        const order_confirm = document.querySelectorAll('.order_confirm');
        for(const button of order_confirm){
            button.addEventListener('click', async (event) => {
                //주문번호
                console.log(event.target.dataset.id);
                const res = await fetch(`/api/user/orders/list/detail/${event.target.dataset.id}`);
                if(res.ok){
                    const data = await res.json();
                    if(confirm("구매를 확정하시겠습니까?")){
                        const res = await fetch(`/api/user/orders/updateStatus/${event.target.dataset.id}`,{
                            method: 'PUT',
                            headers: {
                                'header': header,
                                'X-Requested-With': 'XMLHttpRequest',
                                "Content-Type": "application/json",
                                'X-CSRF-Token': token
                            },
                            body: JSON.stringify({ordersTrackingNum:data.ordersTrackingNum, changeOrdersStatus:"구매확정"})
                        })
                        if(res.ok){
                            alert("구매확정이 완료되었습니다!");
                            location.reload();
                        }
                    }
                }
            })
        }

        //리뷰 수정하러 가기
        const order_reviewChange = document.querySelectorAll('.order_reviewChange');
        for(const button of order_reviewChange){
            button.addEventListener('click', (event) => {
                console.log(event.target.dataset.value);
                console.log(event.target.dataset.id);
                location.replace(`/product/info?productId=${event.target.dataset.id}&orderId=${event.target.dataset.value}`)
            })
        }

        //교환신청
        const order_change = document.querySelectorAll('.order_change');
        for(const button of order_change){
            button.addEventListener('click', async (event) => {
                //주문번호
                console.log(event.target.dataset.id);
                //상품상태
                console.log(event.target.dataset.value);

                // 운송장 번호 조회
                const res = await fetch(`/api/user/orders/list/detail/${event.target.dataset.id}`);
                if(res.ok) {
                    const data = await res.json();
                    if (confirm("교환신청을 진행하시겠습니까?")) {
                        // 상태 변경
                        const response = await fetch(`/api/user/orders/updateStatus/${event.target.dataset.id}`, {
                            method: 'PUT',
                            headers: {
                                'header': header,
                                'X-Requested-With': 'XMLHttpRequest',
                                "Content-Type": "application/json",
                                'X-CSRF-Token': token
                            },
                            body: JSON.stringify({ordersTrackingNum:data.ordersTrackingNum, changeOrdersStatus: "교환신청"})
                        })

                        if (response.ok) {
                            alert("교환신청이 완료되었습니다!");
                            location.reload();
                        }
                    }
                }
            })
        }

        //반품신청
        const order_return = document.querySelectorAll('.order_return');
        for(const button of order_return){
            button.addEventListener('click', async (event) => {
                //주문번호
                console.log(event.target.dataset.id);
                //상품상태
                console.log(event.target.dataset.value);
                    if (confirm("반품신청을 하시겠습니까?")) {
                        const res = await fetch(`/api/user/orders/updateStatus/${event.target.dataset.id}`, {
                            method: 'PUT',
                            headers: {
                                'header': header,
                                'X-Requested-With': 'XMLHttpRequest',
                                "Content-Type": "application/json",
                                'X-CSRF-Token': token
                            },
                            body: JSON.stringify({
                                ordersTrackingNum: data.ordersTrackingNum,
                                changeOrdersStatus: "반품신청"
                            })
                        })

                        if (res.ok) {
                            alert("반품신청이 완료되었습니다!");
                            location.reload();
                        }
                    }
            })
        }
    }
}


(async () =>{
    await myOrdersList('2022-05-01T00:00', '2322-06-12T22:00', 0);
})();