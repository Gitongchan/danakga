// (상품번호, 상품아이디), 사업자 등록 번호, 상품종류, 상품 서브 종류, 상품 브랜드, 상품명
// 상품대표사진, 상품내용, 가격, 재고, 상품등록일, 조회수, 누적 구매수
const list = document.querySelector('.product-list-container');

document.getElementById('product-listBtn').addEventListener('click',async function () {
    const res = await fetch('/api/manager/product/list?startDate=2022-05-01T01:30&endDate=2100-06-14T17:30&productName=%25&productStock=1&page=0')
    const data = await res.json();
    console.log(data);

    try{
        if(res.status===200){
            for(let datalist in data) {
                const item = data[datalist];
                const div = document.createElement('div');
                div.classList.add('product-item');
                div.innerHTML = `
                        <div>${item.productId}</div>
                        <div class="uid">${checkID.value}</div>
                        <div class="pname">${item.productName}</div>
                        <div class="maintype">${item.productType}</div>
                        <div class="subtype">${item.productSubType}</div>
                        <div class="price">${item.productPrice}</div>
                        <div class="stock">${item.productStock}</div>
                        <div class="imageYN">${item.productPhoto===""?"N":"Y"}</div>
                        <button class="info delete">삭제</button>
                        <div class="info" id="${item.productId}"><a href="/product/info?productId=${item.productId}">상세정보</a></div>
                `

                list.appendChild(div);
            }

            for(const btn of document.querySelectorAll('.info.delete')){
                btn.addEventListener('click',(event)=>{
                    console.log(event.target.nextElementSibling.id);
                })
            }
        }
    }catch (e) {

    }
});
