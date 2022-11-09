(async ()=>{
    // p_brand: "용성"
    // p_name: "용성2번"
    // p_price: "123"
    // re_created: "2022-08-18T18:58:32.622824"
    // re_score: 3
    // c: "ppwm1111"
    // totalElements: 2
    // totalPages: 1
    // data.reviewList[].~~~

    await myReviewList(0)
})();

//data[0].totalElement //총 개수
//data[0].totalPage // 페이지 그룹 개수
// 이전 버튼 : 화면에 그려진 첫번째 페이지 - 1
// 다음 버튼 : 화면에 그려진 마지막 페이지 + 1
const pagenation = document.querySelector('.text-center .pagination');

const reviewListWrap = document.getElementById('myReviewList');

async function myReviewList(page){
    const res = await fetch(`/api/user/myReviewList?page=${page}`);

    if(res.ok){
        reviewListWrap.innerHTML='';
        const data = await res.json();
        console.log(data);
        renderPagination(data.reviewList[0].totalPages, data.reviewList[0].totalElements, myReviewList);
        for(let i in data.reviewList){
            console.log(data.reviewList[i]);
            const tr = document.createElement('tr');
            tr.innerHTML =
                `<td>${data.reviewList[i].p_brand}</td>
                 <td>${data.reviewList[i].p_name}</a></td>
                 <td>${data.reviewList[i].re_writer}</td>
                 <td>${data.reviewList[i].re_created.split('.')[0]}</td>
                 <td>${data.reviewList[i].re_score}</td>`
            reviewListWrap.appendChild(tr);
        }
    }
}

function renderPagination(currentPage, totalElement, setList) {
    pagenation.innerHTML = "";

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
        id.addEventListener('click', async function(e) {
            e.preventDefault();
            let item = e.target;
            let id = item.id;
            let selectedPage = item.innerText;

            if (id === "next") selectedPage = next;
            else if (id === "prev") selectedPage = prev;
            else if (id === "allprev") selectedPage = 1;
            else if (id === "allnext") selectedPage = total;
            //페이지 그리는 함수
            else await setList(id);


            renderPagination(selectedPage);//페이지네이션 그리는 함수
        })
    }
}
