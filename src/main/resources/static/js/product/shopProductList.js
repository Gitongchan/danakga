// (상품번호, 상품아이디), 사업자 등록 번호, 상품종류, 상품 서브 종류, 상품 브랜드, 상품명
// 상품대표사진, 상품내용, 가격, 재고, 상품등록일, 조회수, 누적 구매수

//data[0].totalElement //총 개수
//data[0].totalPage // 페이지 그룹 개수
// 이전 버튼 : 화면에 그려진 첫번째 페이지 - 1
// 다음 버튼 : 화면에 그려진 마지막 페이지 + 1
const pagenation = document.querySelector('.product-list .text-center .pagination');

const list = document.querySelector('#product-list');

document.getElementById('product-listBtn').addEventListener('click',async function () {
    console.log('조회 버튼 클릭됨')
});

let productUrl = '';
let paging = ''
let productPaging = ''

function setURL(startDate, endDate, productName, productStock, productType, productSubType, productBrand, startPrice, endPrice, sortBy, sortMethod, page) {
    productUrl = `/api/manager/product/list?startDate=${startDate}&endDate=${endDate}&productName=${productName}&productStock=${productStock}&productType=${productType}&productSubType=${productSubType}&productBrand=${productBrand}&startPrice=${startPrice}&endPrice=${endPrice}&sortBy=${sortBy}&sortMethod=${sortMethod}&`
    paging = `page=${page}`
    productPaging = productUrl + paging
}

// startDate=2022-05-01T01:30&endDate=2100-06-14T17:30
async function myProductList(url){
    const res = await fetch(url)
    const data = await res.json();
    console.log(data);

    pagenation.innerHTML = "";
    renderPagination(url, data[0].totalPage)

    try{
        if(res.status===200){
            list.innerHTML= ''
            for(let datalist in data) {
                const item = data[datalist];
                const tr = document.createElement('tr');
                tr.classList.add('product-item');
                tr.innerHTML = `
                        <td class="p-id">${item.productId}</td>
                        <td class="pname"><a href="/product/info?productId=${item.productId}">${item.productName}</a></td>
                        <td class="main-type">${item.productType}</td>
                        <td class="sub-type">${item.productSubType}</td>
                        <td class="price">${item.productPrice}</td>
                        <td class="stock">${item.productStock}</td>
                        <td class="imageYN">${item.productPhoto===""?"N":"Y"}</td>
                        <td><button class="info-delete" id="${item.productId}">삭제</button></td>
                        <td class="info" id="${item.productId}"><a href="/product/edit?productId=${item.productId}">수정하기</a></td>
                `

                list.appendChild(tr);
            }

            for(const btn of document.querySelectorAll('.info-delete')){
                btn.addEventListener('click',async (event)=>{
                    if(confirm('정말 삭제하시겠습니까?')){
                        console.log(event.target.id);
                        const response = await fetch(`/api/manager/product/delete/${event.target.id}`,{
                            method: 'DELETE',
                            headers: {
                                'header': header,
                                'X-Requested-With': 'XMLHttpRequest',
                                "Content-Type": "application/json",
                                'X-CSRF-Token': token
                            }
                        })
                        if(!response.ok){
                            alert('삭제 실패!');
                            return;
                        }
                        alert('삭제 성공!');
                        myProductList(productPaging);
                    }
                })
            }
        }
    }catch (e) {
        alert("불러오기 실패!");
    }
}

// 페이징 번호 생성 하는 곳
function renderPagination(url, currentPage) {
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
                    else onList(id);

                    renderPagination(url, selectedPage);//페이지네이션 그리는 함수
                })
            }
        })
}

//페이지 번호 클릭 시 이동하는 곳
function onList(e){
    myProductList(productUrl + `page=${e}`)
}

// 초기 실행
(function(){
    setURL('2022-05-01T01:30','2100-06-14T17:30','%25',1,'%25','%25','%25','0','99999999','productUploadDate','des',0);
    myProductList(productPaging);
})();



