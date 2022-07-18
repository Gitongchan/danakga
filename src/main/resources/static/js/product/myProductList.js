// (상품번호, 상품아이디), 사업자 등록 번호, 상품종류, 상품 서브 종류, 상품 브랜드, 상품명
// 상품대표사진, 상품내용, 가격, 재고, 상품등록일, 조회수, 누적 구매수
const list = document.querySelector('#product-list');

document.getElementById('product-listBtn').addEventListener('click',async function () {

});

async function myProductList(){
    const res = await fetch('/api/manager/product/list?startDate=2022-05-01T01:30&endDate=2100-06-14T17:30&productName=%25&productStock=1&page=0')
    const data = await res.json();
    console.log(data);

    try{
        if(res.status===200){
            list.innerHTML= ''
            for(let datalist in data) {
                const item = data[datalist];
                const tr = document.createElement('tr');
                tr.classList.add('product-item');
                tr.innerHTML = `
                        <td class="p-id">${item.productId}</td>
                        <td class="pname">${item.productName}</td>
                        <td class="main-type">${item.productType}</td>
                        <td class="sub-type">${item.productSubType}</td>
                        <td class="price">${item.productPrice}</td>
                        <td class="stock">${item.productStock}</td>
                        <td class="imageYN">${item.productPhoto===""?"N":"Y"}</td>
                        <td><button class="info-delete" id="${item.productId}">삭제</button></td>
                        <td class="info" id="${item.productId}"><a href="/product/info?productId=${item.productId}">상세정보</a></td>
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
                        myProductList();
                    }
                })
            }
        }
    }catch (e) {
        alert("불러오기 실패!");
    }
}

(async function(){
    myProductList();
})();



