// 활동내역 관리
/* 관리자 댓글, 대댓글 목록 */
/* type = 댓글, 대댓글
*  sort == deleted -> N, Y */
// "/commentList/{type}/{sort}"

/* 관리자 댓글, 대댓글 검색 *
    * category = 전체, 내용, 작성자
    * sort = N, Y
    * type = 댓글, 대댓글
    * content = 검색어
    * 전체 검색은 2번 url 사용
    */
// "/commentSearch/{category}/{sort}/{type}/{content}", "/commentSearch/{category}/{sort}/{type}"

// commentDelete/{bd_id}/{cm_id}
// commentDelete/{bd_id}/{cm_id}/${an_id}

menuId.value = '4';
const $table = document.querySelector('tbody');
const $commentList = document.querySelector('#comment_list'); // 버튼
const $category = document.querySelector('#searchCategory');
const $deletedConfig = document.querySelector('#deletedConfig');
const $searchType = document.querySelector('#searchType');

/*
cm_answerNum: 0
cm_content: "dkd?\n\n"
cm_created: "2022-11-12T15:00:08.922728"
cm_deleted: "N"
cm_id: 2
cm_modify: "2022-11-12T15:00:08.922754"
cm_step: 0
cm_writer: "shop123"
totalElement: 2
totalPage: 1
* */

// 버튼기능
const commentActions = {
    info: async ({bd_id, cm_id, an_id}) => {
        window.open(`/board/info?boardid=${bd_id}`)
    },
    delete: async ({bd_id, cm_id, parent_id}) => {
        if(confirm("삭제하시겠습니까?")){
            //an_id가 없을 때(댓글 삭제)
            if(parent_id === 'undefined'){
                console.log('댓글삭제 실행')
                const res = await fetch(`/admin/commentDelete/${bd_id}/${cm_id}`,{
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
            }else{
                //대댓글 삭제
                const res = await fetch(`/admin/commentAnswerDelete/${bd_id}/${parent_id}/${cm_id}`,{
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
            }
            commentList($searchType.value, $deletedConfig.value,0);
        }
    },
}


/* 관리자 댓글 목록 */
// type = 댓글, 대댓글
/* sort == deleted N, Y */

async function commentList(type, sort, page){
    const res = await fetch(`/admin/commentList/${type}/${sort}?page=${page}`);
    const data = await res.json();

    pagenation.innerHTML = "";
    console.log(data);
    if(!res.ok){
        alert("댓글 조회 실패!")
    }

    $table.innerHTML = '';

    console.log(data);
    const pageData = data.comments[0];
    renderPagination(pageData.totalPage, pageData.totalElement, commentList);
    for(const item of data.comments){
        $table.innerHTML+= `
                      <tr>
                        <td><i class="fab fa-angular fa-lg text-danger me-3"></i> <strong>${item.cm_writer}</strong></td>
                        <td>${item.cm_content}</td>
                        <td>${item.cm_deleted}</td>
                        <td>
                            ${item.cm_created.split('.')[0]}
                        </td>
                        <td>${item.cm_modify.split('.')[0]}</td>
                        <td>
                                <button class="btn user-info" data-action="info" data-bdid="${item.board_id}"><i class="bx bx-book-bookmark me-1"></i> 게시글보기</button>
                        </td>
                        <td>
                                <button class="btn user-stop" data-action="delete" data-bdid="${item.board_id}" data-myid="${item.cm_id}" data-parentid="${item.cm_parentNum}"><i class="bx bx-trash me-1"></i> 삭제</button>
                        </td>
                        <td>
                                
                        </td>
                      </tr>
        `
    }
}

/* 관리자 댓글/대댓글 검색 */
/* category = 전체, 제목, 내용, 작성자
*  sort = N, Y
*  type = 댓글, 대댓글
*  content = 검색어
* */

async function searchCommentList(category, sort, type, content, page){
    let url;
    if(content){
        url = `/admin/commentSearch/${category}/${sort}/${type}/${content}?page=${page}`
    }else{
        url= `/admin/commentSearch/${category}/${sort}/${type}?page=${page}`
    }
    const res = await fetch(url);
    const data = await res.json();
    pagenation.innerHTML = "";

    if(!res.ok){
        alert("댓글 검색 실패!")
    }
    $table.innerHTML = '';

    const pageData = data.comments[0];
    renderPagination(pageData.totalPage, pageData.totalElement, searchCommentList);

    for(const item of data.comments){
        $table.innerHTML+= `
                      <tr>
                        <td><i class="fab fa-angular fa-lg text-danger me-3"></i> <strong>${item.cm_writer}</strong></td>
                        <td>${item.cm_content}</td>
                        <td>${item.cm_deleted}</td>
                        <td>${item.cm_created.split('.')[0]}</td>
                        <td>
                            ${item.cm_modify.split('.')[0]}
                        </td>
                        <td>
                                <button class="btn user-info" data-action="info" data-bdid="${item.board_id}"><i class="bx bx-book-bookmark me-1"></i> 게시글</button>
                        </td>
                        <td>
                                <button class="btn user-stop" data-action="delete" data-bdid="${item.board_id}" data-myid="${item.cm_id}" data-parentid="${item.cm_parentNum}"><i class="bx bx-trash me-1"></i> 삭제</button>
                        </td>
                        <td>
                                
                        </td>
                      </tr>
        `
    }
}

$commentList.addEventListener('click', () => {
    commentList($searchType.value, $deletedConfig.value, 0);
})

document.querySelector('tbody').addEventListener('click', e => {
    const action = e.target.dataset.action
    // 게시판 아이디
    const bd_id = e.target.dataset.bdid;
    // 해당 댓글 아이디
    const cm_id = e.target.dataset.myid;
    // 상위 부모 댓글 아이디
    const parent_id = e.target.dataset.parentid;
    console.log('버튼발생',bd_id, cm_id, parent_id);

    if (action) {
        commentActions[action]({bd_id, cm_id, parent_id});
    }
});



commentList($searchType.value, $deletedConfig.value,0);