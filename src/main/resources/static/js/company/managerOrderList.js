(async () => {
    // orderStatus: "주문취소"
    // ordersDate: "2022-08-19T16:01:15.566133"
    // ordersId: 11
    // ordersPrice: 16
    // ordersQuantity: 3
    // productBrand: "시마노"
    // productId: 4
    // productName: "시마노4번"
    // totalElement: 11
    // totalPage: 2

    //상품 준비중일때는 사업자는 배송시작,주문거절 / 일반사용자는 주문취소만 가능
    //주문취소,반품신청일때 사업자는 "환불완료" 처리만 가능
    //배송완료면 교환신청,반품신청,구매확정
    //교환신청 일때는 사업자는 "교환상품배송"으로 처리, 운송장번호 같이입력  , 이후 사업자는 배송완료 처리 해주어야한다
    //기본 적인 경우 상품준비중-> 배송시작(운송장번호 입력) -> 배송완료(완료일자 자동입력) -> 구매확정
    //구매확정 일때만 리뷰가능

    //     READY("상품준비중"), CANCEL("주문취소"), EXCHANGE("교환신청") , RETURN("반품신청") ,CONFIRM("구매확정")
    //     ,START("배송시작"), FINISH("배송완료"), REFUND("환불완료") , REDELIVERY("교환상품배송");

    await salesList('2022-05-01T00:00', '2322-06-12T22:00', 0);

})();

const $resetBtn = document.getElementById('reset-date');
const $searchBtn = document.getElementById('search-date')
const $salesList = document.getElementById('salesList');
const sDate = document.getElementById('startDate');
const eDate = document.getElementById('endDate');

$resetBtn.addEventListener('click', async () => {
    $salesList.innerHTML = '';
    sDate.value = '';
    eDate.value = '';

    await salesList('2022-05-01T00:00', '2322-06-12T22:00', 0);
    const res = await fetch(`/api/manager/sales/list?startTime=2022-05-01T00:00&endTime=2322-06-12T22:00&page=0`);
    if(res.ok) {
        const data = await res.json();
        renderSalesPagination(data[0].totalPage, data[0].totalElement);
    }
})

$searchBtn.addEventListener('click', async () => {
    const res = await fetch(`/api/manager/sales/list?startTime=${sDate.value}&endTime=${eDate.value}&page=0`);
    if(res.ok){
        const data = await res.json();
        await salesList(sDate.value, eDate.value, 0);
        renderSalesPagination(data[0].totalPage, data[0].totalElement);
    }
})

async function salesList(startDate = '2022-05-01T00:00', endDate = '2322-06-12T00:00', page) {
    const res = await fetch(`/api/manager/sales/list?startTime=${startDate}&endTime=${endDate}&page=${page}`);

    if (res.ok) {
        $salesList.innerHTML = '';
        const data = await res.json();
        console.log(data);
        for (let i in data) {
            $salesList.innerHTML += `
                                <div class="row align-items-center mb-10">
                                    <div class="col-lg-1 col-md-1 col-12">
                                        <p class="order_num">
                                            ${data[i].ordersId}
                                        </p>
                                    </div>
                                    <div class="col-lg-1 col-md-1 col-12">
                                        <p>${data[i].productBrand}</p>
                                    </div>
                                    <div class="col-lg-3 col-md-3 col-12">
                                        <a href="/product/info?productId=${data[i].productId}">
                                            <p>${data[i].productName}</p>
                                        </a>
                                    </div>
                                    <div class="col-lg-2 col-md-1 col-12">
                                        <p>${data[i].ordersQuantity}</p>
                                    </div>
                                    <div class="col-lg-2 col-md-2 col-12">
                                        <p>${data[i].ordersPrice}원</p>
                                    </div>
                                    <div class="col-lg-2 col-md-3 col-12">
                                        ${data[i].orderStatus}
                                    </div>
                                    <div class="col-lg-1 col-md-2 col-12 info_wrap btnWrap${data[i].ordersId}">
                                    </div>
                                </div>
            `;
            if (data[i].orderStatus === "상품준비중") {
                document.querySelector(`.btnWrap${data[i].ordersId}`).innerHTML += `
                <button class="sales_start" data-id="${data[i].ordersId}">배송시작</button>
                <button class="sales_cancel" data-id="${data[i].ordersId}">주문거절</button>
                `
            } else if (data[i].orderStatus === "배송시작") {
                document.querySelector(`.btnWrap${data[i].ordersId}`).innerHTML += `
                <button class="sales_success" data-id="${data[i].ordersId}">배송완료</button>
                `
            } else if (data[i].orderStatus === "교환신청") {
                document.querySelector(`.btnWrap${data[i].ordersId}`).innerHTML += `
                <button class="sales_change" data-id="${data[i].ordersId}">배송시작</button>
                `
            } else if (data[i].orderStatus === "교환상품배송") {
                document.querySelector(`.btnWrap${data[i].ordersId}`).innerHTML += `
                <button class="sales_success" data-id="${data[i].ordersId}">배송완료</button>
                `
            } else if (data[i].orderStatus === "환불신청") {
                document.querySelector(`.btnWrap${data[i].ordersId}`).innerHTML += `
                <button class="sales_refund" data-id="${data[i].ordersId}">환불완료</button>
                `
            } else if (data[i].orderStatus === "반품신청") {
                document.querySelector(`.btnWrap${data[i].ordersId}`).innerHTML += `
                <button class="sales_refund" data-id="${data[i].ordersId}">환불완료</button>
                `
            } else if (data[i].orderStatus === "주문취소") {
                document.querySelector(`.btnWrap${data[i].ordersId}`).innerHTML += `
                <button class="sales_refund" data-id="${data[i].ordersId}">환불완료</button>
                `
            }
        }

        //배송 시작 버튼
        const salesStart = document.querySelectorAll('.sales_start');
        for (const button of salesStart) {
            button.addEventListener('click', async (event) => {
                console.log(event.target.dataset.id);
                if (confirm("배송을 시작하시겠습니까?")) {
                    const trackNum = prompt("운송장 번호를 입력해주세요.");
                    if (trackNum.trim().length === 0) {
                        alert("운송장 번호를 입력해주세요!");
                        return 0;
                    } else {
                        const res = await fetch(`/api/manager/sales/updateStatus/${event.target.dataset.id}`, {
                            method: 'PUT',
                            headers: {
                                'header': header,
                                'X-Requested-With': 'XMLHttpRequest',
                                "Content-Type": "application/json",
                                'X-CSRF-Token': token
                            },
                            body: JSON.stringify({ordersTrackingNum: trackNum, changeOrdersStatus: "배송시작"})
                        })
                        if (res.ok) {
                            alert("배송이 시작되었습니다!");
                            location.reload();
                        }
                    }
                }
            })
        }

        //주문 거절 버튼
        const salesCancel = document.querySelectorAll('.sales_cancel');
        for (const button of salesCancel) {
            button.addEventListener('click', async (event) => {
                if (confirm("주문을 거절하시겠습니까?")) {
                    const res = await fetch(`/api/manager/sales/updateStatus/${event.target.dataset.id}`, {
                        method: 'PUT',
                        headers: {
                            'header': header,
                            'X-Requested-With': 'XMLHttpRequest',
                            "Content-Type": "application/json",
                            'X-CSRF-Token': token
                        },
                        body: JSON.stringify({ordersTrackingNum: "", changeOrdersStatus: "주문취소"})
                    })
                    if (res.ok) {
                        alert("주문을 취소하였습니다!");
                        location.reload();
                    }
                }
            })
        }

        //배송완료 버튼
        const salesSuccess = document.querySelectorAll('.sales_success');
        for (const button of salesSuccess) {
            button.addEventListener('click', async (event) => {
                if (confirm("배송이 완료된 제품입니까?")) {
                    const res = await fetch(`/api/manager/sales/updateStatus/${event.target.dataset.id}`, {
                        method: 'PUT',
                        headers: {
                            'header': header,
                            'X-Requested-With': 'XMLHttpRequest',
                            "Content-Type": "application/json",
                            'X-CSRF-Token': token
                        },
                        body: JSON.stringify({ordersTrackingNum: "", changeOrdersStatus: "배송완료"})
                    })
                    if (res.ok) {
                        location.reload();
                    }
                }
            })
        }

        //교환신청일때의 배송시작 버튼
        const salesChange = document.querySelectorAll('.sales_change');
        for (const button of salesChange) {
            button.addEventListener('click', async (event) => {
                if (confirm("교환상품의 배송을 시작하시겠습니까?")) {
                    const trackNum = prompt("새로운 운송장 번호를 입력해주세요.");
                    if (trackNum.trim().length === 0) {
                        alert("운송장 번호를 입력해주세요!");
                        return 0;
                    } else {
                        const res = await fetch(`/api/manager/sales/updateStatus/${event.target.dataset.id}`, {
                            method: 'PUT',
                            headers: {
                                'header': header,
                                'X-Requested-With': 'XMLHttpRequest',
                                "Content-Type": "application/json",
                                'X-CSRF-Token': token
                            },
                            body: JSON.stringify({ordersTrackingNum: trackNum, changeOrdersStatus: "교환상품배송"})
                        })
                        if (res.ok) {
                            alert("교환상품의 배송이 시작되었습니다!");
                            location.reload();
                        }
                    }
                }
            })
        }

        //환불 완료(주문취소, 환불신청, 반품신청)
        const salesReFund = document.querySelectorAll('.sales_refund');
        for (const button of salesReFund) {
            button.addEventListener('click', async (event) => {
                if (confirm("환불완료를 진행하시겠습니까?")) {
                    const res = await fetch(`/api/manager/sales/updateStatus/${event.target.dataset.id}`, {
                        method: 'PUT',
                        headers: {
                            'header': header,
                            'X-Requested-With': 'XMLHttpRequest',
                            "Content-Type": "application/json",
                            'X-CSRF-Token': token
                        },
                        body: JSON.stringify({ordersTrackingNum: "", changeOrdersStatus: "환불완료"})
                    })
                    if (res.ok) {
                        alert("환불이 완료되었습니다!");
                        location.reload();
                    }
                }
            })
        }
    }
}