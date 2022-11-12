//리뷰 관리

menuId.value = '5';
const $table = document.querySelector('tbody');
const $reviewList = document.querySelector('#review_list'); // 버튼
const $category = document.querySelector('#searchCategory');
const $deletedConfig = document.querySelector('#deletedConfig');
const $searchType = document.querySelector('#searchType');


/*
* company_id: 1
company_name: "아난"
product_id: 5
product_name: "1번찌"
re_content: "ㅇㅈ합니다"
re_created: "2022-11-12T20:17:31.733742"
re_id: 2
re_score: 5
re_writer: "asdf123"
totalElements: 2
totalPages: 1
* */

// 버튼기능
const reviewActions = {
    info: async ({c_id, p_id, re_id}) => {
        window.open(`/product/info?productId=${p_id}`)
    },
    delete: async ({c_id, p_id, re_id}) => {
        if(confirm("삭제하시겠습니까?")){
                //대댓글 삭제
                const res = await fetch(`/admin/reviewDelete/${c_id}/${p_id}/${re_id}`,{
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
            reviewList($searchType.value, $deletedConfig.value,0);
        }
}


/* 관리자 댓글 목록 */
// type = 댓글, 대댓글
/* sort == deleted N, Y */

async function reviewList(sort, page){
    const res = await fetch(`/admin/reviewList/${sort}?page=${page}`);
    const data = await res.json();

    pagenation.innerHTML = "";
    console.log(data);
    if(!res.ok){
        alert("후기 조회 실패!")
    }

    $table.innerHTML = '';

    console.log(data);
    const pageData = data.reviewList[0];
    renderPagination(pageData.totalPages, pageData.totalElements, reviewList);
    for(const item of data.reviewList){
        $table.innerHTML+= `
                      <tr>
                        <td><i class="fab fa-angular fa-lg text-danger me-3"></i> <strong>${item.company_name}</strong></td>
                        <td>${item.product_name}</td>
                        <td>${item.re_content}</td>
                        <td>
                            ${item.re_writer}
                        </td>
                        <td>${item.re_created.split('.')[0]}</td>
                        <td>
                                <button class="btn user-info" data-action="info" data-productid="${item.product_id}"><i class="bx bxl-product-hunt me-1"></i> 상품보기</button>
                        </td>
                        <td>
                                <button class="btn user-stop" data-action="delete" data-productid="${item.product_id}" data-companyid="${item.company_id}" data-reviewid="${item.re_id}"><i class="bx bx-trash me-1"></i> 삭제</button>
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

async function searchReviewList(category, sort, content, page){
    const res = await fetch(`/admin/reviewSearch/${category}/${sort}/${content}?page=${page}`);
    const data = await res.json();
    pagenation.innerHTML = "";

    if(!res.ok){
        alert("후기 검색 실패!")
    }
    $table.innerHTML = '';

    const pageData = data.reviewList[0];
    renderPagination(pageData.totalPages, pageData.totalElements, searchReviewList);

    for(const item of data.reviewList){
        $table.innerHTML+= `
                      <tr>
                        <td><i class="fab fa-angular fa-lg text-danger me-3"></i> <strong>${item.company_name}</strong></td>
                        <td>${item.product_name}</td>
                        <td>${item.re_content}</td>
                        <td>
                            ${item.re_writer}
                        </td>
                        <td>${item.re_created.split('.')[0]}</td>
                        <td>
                                <button class="btn user-info" data-action="info" data-productid="${item.product_id}"><i class="bx bxl-product-hunt me-1"></i> 상품보기</button>
                        </td>
                        <td>
                                <button class="btn user-stop" data-action="delete" data-productid="${item.product_id}" data-companyid="${item.company_id}" data-reviewid="${item.re_id}"><i class="bx bx-trash me-1"></i> 삭제</button>
                        </td>
                        <td>
                                
                        </td>
                      </tr>
        `
    }
}

$reviewList.addEventListener('click', () => {
    reviewList($deletedConfig.value, 0);
})

document.querySelector('tbody').addEventListener('click', e => {
    const action = e.target.dataset.action
    const c_id = e.target.dataset.companyid;
    const p_id = e.target.dataset.productid;
    const re_id = e.target.dataset.reviewid;

    if (action) {
        reviewActions[action]({c_id,p_id,re_id});
    }
});



reviewList($deletedConfig.value, 0);