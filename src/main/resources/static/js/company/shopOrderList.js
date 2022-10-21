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
    const pagenation = document.querySelector('.text-center .pagination');

    const $searchBtn = document.getElementById('search-orderList');
    const $salesList = document.getElementById('salesList');

    const searchType = document.querySelector('#search-type');
    const searchWord = document.querySelector('#search-word');
    const searchStatus = document.querySelector('#status-type');


    // URL 설정
    let orderUrl = '';
    let paging = ''
    let orderPaging = ''

    // 날짜
    let today = new Date();

    let startDate = today.toISOString().substring(0,11)+'00:00';
    let endDate = today.toISOString().substring(0,11)+'23:59';

    $searchBtn.addEventListener('click',() => {
        console.log(startDate, endDate, searchType.value, searchWord.value ? searchWord.value : '%25' , searchStatus.value, 0);

        setURL(startDate, endDate, searchStatus.value, searchType.value, searchWord.value ? searchWord.value : '%25' ,0);
        shopOrderList(orderPaging);
    });

    
    function setURL(startDate = '2022-05-01T00:00', endDate = '2322-06-12T00:00', orderStatus, searchRequirements, searchWord= '%25', page) {
        orderUrl = `/api/manager/sales/list?startTime=${startDate}&endTime=${endDate}&ordersStatus=${orderStatus}&searchRequirements=${searchRequirements}&searchWord=${searchWord}&`
        paging = `page=${page}`
        orderPaging = orderUrl + paging
    }

    async function shopOrderList(url) {
        const res = await fetch(url);

        if (res.ok) {
            $salesList.innerHTML = '';
            pagenation.innerHTML = "";
            const data = await res.json();
            console.log(data);

            orderPagination(url, data[0].totalPage)

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

    // 페이징 번호 생성 하는 곳
    function orderPagination(url, currentPage) {
        fetch(url)
            .then((res)=>res.json())
            .then((data)=>{
                pagenation.innerHTML = "";

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
                        else shopOrderList(orderUrl + `page=${id}`);

                        orderPagination(url, selectedPage);//페이지네이션 그리는 함수
                    })
                }
            })
    }

     (function () {
         setURL('2000-01-01T00:00', today.toISOString().substring(0,11)+'23:59', '상품준비중','productType','%25',0);
         shopOrderList(orderPaging);
     })();