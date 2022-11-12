// 상품관리
    /*
    * company_id: 1
    company_name: "아난"
    p_brand: "아부가르시아"
    p_id: 6
    p_name: "아부가르시아 바늘"
    p_photo: "../product_thumbNail/63a9b3d6-ebb7-4b4a-9f7c-a77400481a43__6418100_1.jpg"
    p_price: 5000
    p_rating: 0
    p_stock: 10
    p_subtype: "다운샷"
    p_type: "바늘/훅"
    p_uploadDate: "2022-11-11T15:02:14.751655"
    p_viewCount: 1
    totalElement: 4
    totalPage: 1
    * */
menuId.value = '2';
const $table = document.querySelector('tbody');
const $productList = document.querySelector('#product_list');
const $category = document.querySelector('#searchCategory');

// 버튼기능
const productActions = {
    info: async ({c_id, p_id}) => {
        window.open(`/product/info?productId=${p_id}`)
    },
    delete: async ({c_id, p_id}) => {
        if(confirm("삭제하시겠습니까?")){
            const res = await fetch(`/admin/productDelete/${c_id}/${p_id}`,{
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
            productList(0);
        }
    },
}

async function productList(page){
    const res = await fetch(`/admin/productList?page=${page}`);
    const data = await res.json();

    if(!res.ok){
        alert("상품조회 실패!")
    }
    $table.innerHTML = '';

    const pageData = data.adminProductList[0];
    renderPagination(pageData.totalPage, pageData.totalElement, productList);
    for(const item of data.adminProductList){
        $table.innerHTML+= `
                      <tr>
                        <td><i class="fab fa-angular fa-lg text-danger me-3"></i> <strong>${item.company_name}</strong></td>
                        <td>${item.p_brand}</td>
                        <td>${item.p_name}</td>
                        <td>
                            ${item.p_type}
                        </td>
                        <td>${item.p_subtype}</td>
                        <td>
                                <button class="btn user-info" data-action="info" data-productid="${item.p_id}"><i class="bx bx-user me-1"></i> 상품보기</button>
                        </td>
                        <td>
                                <button class="btn user-stop" data-action="delete" data-companyid="${item.company_id}" data-productid="${item.p_id}"><i class="bx bx-trash me-1"></i> 삭제</button>
                        </td>
                        <td>
                                
                        </td>
                      </tr>
        `
    }
    console.log(data);
}

// 카테고리 = 가게, 상품, 브랜드
async function searchProductList(category, searchWord, page){
    const res = await fetch(`/admin/productSearch/${category}/${searchWord}?page=${page}`);
    const data = await res.json();

    if(!res.ok){
        alert("상품 검색 실패!")
    }

    const pageData = data.adminProductList[0];
    renderPagination(pageData.totalPage, pageData.totalElement, productList);

    $table.innerHTML = '';
    for(const item of data.adminProductList){
        $table.innerHTML+= `
                      <tr>
                        <td><i class="fab fa-angular fa-lg text-danger me-3"></i> <strong>${item.company_name}</strong></td>
                        <td>${item.p_brand}</td>
                        <td>${item.p_name}</td>
                        <td>
                            ${item.p_type}
                        </td>
                        <td>${item.p_subtype}</td>
                        <td>
                                <button class="btn user-info" data-action="info" data-productid="${item.p_id}"><i class="bx bx-user me-1"></i> 상품보기</button>
                        </td>
                        <td>
                                <button class="btn user-stop" data-action="delete" data-companyid="${item.company_id}" data-productid="${item.p_id}"><i class="bx bx-trash me-1"></i> 삭제</button>
                        </td>
                        <td>
                                
                        </td>
                      </tr>
        `
    }
    console.log(data);
}

$productList.addEventListener('click', () => {
    productList(0);
})

document.querySelector('tbody').addEventListener('click', e => {
    const action = e.target.dataset.action
    const c_id = e.target.dataset.companyid;
    const p_id = e.target.dataset.productid;
    console.log(c_id,p_id);
    if (action) {
        productActions[action]({c_id, p_id});
    }
});



productList(0);