const wish_list = document.querySelector('.cart-single-list');
const wishSelectDelete = document.getElementById('wishSelectDelete');
(async function(){
    const res = await fetch(`/api/user/wish/0`)
    try{
        if(res.ok){
            wish_list.innerHTML = '';
            const data = await res.json();
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
    }catch (e) {

    }
})();