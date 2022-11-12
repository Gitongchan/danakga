//토근값 가져오기
const token = document.querySelector('meta[name="_csrf"]').content;
const header = document.querySelector('meta[name="_csrf_header"]').content;
const pagenation = document.querySelector('.text-center .pagination');

const menuId = document.querySelector('#menuId');
const $searchInput = document.querySelector('#search-input');

// nav에 공통검색기능
function searchText(e){
    const txt = $searchInput.value;
    const code = e.code;

    if(code === 'Enter'){
        if(menuId.value === '0'){
            userList($selectEnabled.value, $selectSearchRequirements.value, $searchInput.value ? $searchInput.value : '%25', $selectSortMethod.value, $selectSortBy.value,0)
        }else if( menuId.value === '1'){
            manager($selectEnabled.value, $selectSearchRequirements.value, $searchInput.value ? $searchInput.value : '%25', $selectSortMethod.value, $selectSortBy.value,0)
        }else if(menuId.value === '2'){
            searchProductList($category.value, $searchInput.value, 0)
        }else if(menuId.value === '3'){
            searchBaordList($category.value, $deletedConfig.value, $searchType.value, $searchInput.value, 0);
        }

        $searchInput.value = '';
    }
}
// 공통적인 페이징 부분
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
                    renderPagination(selectedPage, totalElement, setList);//페이지네이션 그리는 함수
                })
            }
}
