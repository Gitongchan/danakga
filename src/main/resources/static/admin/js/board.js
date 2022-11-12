// 게시판 관리

menuId.value = '3';
const $table = document.querySelector('tbody');
const $boardList = document.querySelector('#board_list');
const $category = document.querySelector('#searchCategory');
const $deletedConfig = document.querySelector('#deletedConfig');
const $searchType = document.querySelector('#searchType');

/*
* bd_created
* bd_deleted
* bd_id
* bd_title
* bd_type
* bd_views
* bd_writer
* totalElement
* totalPage
* */

// 버튼기능
const boardActions = {
    info: async (bd_id) => {
        window.open(`/board/info?boardid=${bd_id}`)
    },
    delete: async (bd_id) => {
        if(confirm("삭제하시겠습니까?")){
            const res = await fetch(`/admin/postDelete/${bd_id}`,{
                method: "DELETE",
                headers: {
                    'header': header,
                    'X-CSRF-Token': token,
                    "Content-Type": "application/json"
                },
            });

            if(!res.ok){
                alert("삭제 실패!")
                return;
            }

            const data = await res.json();
            alert(data.message);
            boardList($deletedConfig.value,$searchType.value,0);
        }
    },
}


/* 관리자 게시판 목록 */
/* sort == deleted N, Y */

async function boardList(bool, type, page){
    const res = await fetch(`/admin/boardList/${bool}/${type}?page=${page}`);
    const data = await res.json();

    pagenation.innerHTML = "";

    if(!res.ok){
        alert("상품조회 실패!")
    }

    $table.innerHTML = '';

    console.log(data);
    const pageData = data.lists[0];
    renderPagination(pageData.totalPage, pageData.totalElement, boardList);
    for(const item of data.lists){
        $table.innerHTML+= `
                      <tr>
                        <td><i class="fab fa-angular fa-lg text-danger me-3"></i> <strong>${item.bd_type}</strong></td>
                        <td>${item.bd_title}</td>
                        <td>${item.bd_writer}</td>
                        <td>
                            ${item.bd_deleted}
                        </td>
                        <td>${item.bd_created.split('.')[0]}</td>
                        <td>
                                <button class="btn user-info" data-action="info" data-bdid="${item.bd_id}"><i class="bx bx-book-bookmark me-1"></i> 게시글보기</button>
                        </td>
                        <td>
                                <button class="btn user-stop" data-action="delete" data-bdid="${item.bd_id}"><i class="bx bx-trash me-1"></i> 삭제</button>
                        </td>
                        <td>
                                
                        </td>
                      </tr>
        `
    }
}

/* 관리자 게시판 검색 */
/* category = 전체, 제목, 내용, 작성자
*  sort = N, Y
*  type = 자유, 정보
*  content = 검색어
* */

async function searchBaordList(category, sort, type, content, page){
    const res = await fetch(`/admin/boardSearch/${category}/${sort}/${type}/${content}?page=${page}`);
    const data = await res.json();

    if(!res.ok){
        alert("상품 검색 실패!")
    }
    pagenation.innerHTML = "";

    const pageData = data.lists[0];
    renderPagination(pageData.totalPage, pageData.totalElement, searchBaordList);

    $table.innerHTML = '';
    for(const item of data.lists){
        $table.innerHTML+= `
                      <tr>
                        <td><i class="fab fa-angular fa-lg text-danger me-3"></i> <strong>${item.bd_type}</strong></td>
                        <td>${item.bd_title}</td>
                        <td>${item.bd_writer}</td>
                        <td>
                            ${item.bd_deleted}
                        </td>
                        <td>${item.bd_created.split('.')[0]}</td>
                        <td>
                                <button class="btn user-info" data-action="info" data-bdid="${item.bd_id}"><i class="bx bx-book-bookmark me-1"></i> 상품보기</button>
                        </td>
                        <td>
                                <button class="btn user-stop" data-action="delete" data-bdid="${item.bd_id}"><i class="bx bx-trash me-1"></i> 삭제</button>
                        </td>
                        <td>
                                
                        </td>
                      </tr>
        `
    }
}

$boardList.addEventListener('click', () => {
    boardList($deletedConfig.value, $searchType.value, 0);
})

document.querySelector('tbody').addEventListener('click', e => {
    const action = e.target.dataset.action
    const bd_id = e.target.dataset.bdid;
    if (action) {
        boardActions[action](bd_id);
    }
});



boardList($deletedConfig.value,$searchType.value,0);