const $gridProductList = document.querySelector('#gridProductList');

// URL 설정
let productUrl = '';
let paging = ''
let productPaging = ''

const shopName = getParameterByName('shopName').split('?')[0];

// productUrl = `/api/company/inquire/${shopName}?sortBy=productPrice&sortMethod=desc&productName=%25&productStock=0&`

function setURL(shopName, sortBy, sortMethod, productName, page) {
    productUrl = `/api/company/inquire/${shopName}?sortBy=${sortBy}&sortMethod=${sortMethod}&productName=${productName}&productStock=0&`
    paging = `page=${page}`
    productPaging = productUrl + paging
}


const gridProductList = async (url) => {
    const res = await fetch(url);
    const data = await res.json();

    for(const item of data.productListDto){
        console.log(item)
        $gridProductList.innerHTML += `
                    <div class="col-lg-4 col-md-6 col-12">
                        <!-- Start Single Product -->
                        <div class="single-product">
                            <div class="product-image">
                                <img src=${item.productPhoto} alt="#">
                            </div>
                            <div class="product-info">
                                <span class="category">${item.productType}</span>
                                <h4 class="title">
                                    <a href="/product/info?productId=${item.productId}">${item.productName}</a>
                                </h4>
                                <ul class="review">
                                    <li><i class="lni lni-star star-1_${item.productId}"></i></li>
                                    <li><i class="lni lni-star star-2_${item.productId}"></i></li>
                                    <li><i class="lni lni-star star-3_${item.productId}"></i></li>
                                    <li><i class="lni lni-star star-4_${item.productId}"></i></li>
                                    <li><i class="lni lni-star star-5_${item.productId}"></i></li>
                                    <li><span>${item.productRating}점</span></li>
                                </ul>
                                <div class="price">
                                    <span>${item.productPrice}원</span>
                                </div>
                            </div>
                        </div>
                        <!-- End Single Product -->
                    </div>`


        for(let i=1; i <= item.productRating; i++){
            document.querySelector(`.star-${i}_${item.productId}`).classList.remove('lni-star')
            document.querySelector(`.star-${i}_${item.productId}`).classList.add('lni-star-filled')
        }
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

gridProductList();
