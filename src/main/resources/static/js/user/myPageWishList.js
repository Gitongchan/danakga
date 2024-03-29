const wish_list = document.querySelector('.cart-single-list');
const wishSelectDelete = document.getElementById('wishSelectDelete');

//data[0].totalElement //총 개수
//data[0].totalPage // 페이지 그룹 개수
// 이전 버튼 : 화면에 그려진 첫번째 페이지 - 1
// 다음 버튼 : 화면에 그려진 마지막 페이지 + 1
const pagenation = document.querySelector('.text-center .pagination');

(async function(){
    myWishList(0);
})();

async function myWishList(page){
    const res = await fetch(`/api/user/wish/${page}`)
    if(res.ok){
        wish_list.innerHTML = '';
        const data = await res.json();
        renderPagination(data[0].totalPage, data[0].totalElement, myWishList);
        for(let i=0; i < data.length; i++){
            wish_list.innerHTML +=`
                <div class="row align-items-center mb-10">
                    <div class="col-lg-1 col-md-1 col-12">
                        <input type="checkbox" name="wishItem" value="${data[i].wishId}"/>
                    </div>
                    <div class="col-lg-1 col-md-1 col-12">
                        <h6 class="product-name">
                                ${data[i].companyName}
                        </h6>
                    </div>
                    <div class="col-lg-2 col-md-2 col-12">
                        <p>${data[i].productBrand === "" ? "브랜드 없음" : data[i].productBrand}</p>
                    </div>
                    <div class="col-lg-4 col-md-3 col-12">
                           <a href="/product/info?productId=${data[i].productId}">
                        <p>${data[i].productName}</p>
                            </a>
                    </div>
                    <div class="col-lg-2 col-md-2 col-12">
                        <p>${data[i].productPrice}원</p>
                    </div>
                    <div class="col-lg-1 col-md-2 col-12">
                        <button class="remove-item item-${data[i].wishId}" id=${data[i].wishId}><i class="lni lni-close"></i></button>
                    </div>
                </div>`
        }
        const deleteBtn = document.querySelectorAll(`.remove-item`);
        for(const button of deleteBtn){
            button.addEventListener('click', async (event) =>{
                console.log(event.target.id);
                if(confirm('관심 상품에서 삭제하시겠습니까?')){
                    const res = await fetch('/api/user/wish',{
                        method: 'DELETE',
                        headers: {
                            'header': header,
                            'X-Requested-With': 'XMLHttpRequest',
                            "Content-Type": "application/json",
                            'X-CSRF-Token': token
                        },
                    })
                    console.log(JSON.stringify([{wishId: event.target.id}]));
                    if(res.ok) {
                        const data = await res.json();
                        alert('삭제되었습니다!');
                        location.reload();
                    }else{
                        alert('삭제 실패!');
                    }
                }
            })
        }
        wishSelectDelete.addEventListener('click', async () => {
            const wisharray = [];
            const selectWish = document.querySelectorAll('input[name="wishItem"]:checked');
            selectWish.forEach((el)=>{
                wisharray.push({wishId:el.value})
            })
            if(confirm('선택한 상품을 관심 목록에서 삭제하시겠습니까?')){
                const res = await fetch('/api/user/wish',{
                    method: 'DELETE',
                    headers: {
                        'header': header,
                        'X-Requested-With': 'XMLHttpRequest',
                        "Content-Type": "application/json",
                        'X-CSRF-Token': token
                    },
                    body: JSON.stringify(wisharray)
                })
                if(res.ok) {
                    const data = await res.json();
                    alert('삭제되었습니다!');
                    location.reload();
                }else{
                    alert('삭제 실패!');
                }
            }
        })
    }
}

function renderPagination(currentPage, totalElement, setList) {
    //총 페이지 수
    const total = Math.ceil(totalElement/10);
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
            else setList(id);



            pagenation.innerHTML = "";
            renderPagination(selectedPage);//페이지네이션 그리는 함수

        })
    }
}