const wish_list = document.querySelector('.cart-single-list > div');

(async function(){
    const res = await fetch(`/api/user/wish/0`)
    try{
        if(res.ok){
            wish_list.innerHTML = '';
            const data = await res.json();

            for(let i=0; i < data.length; i++){
                wish_list.innerHTML +=`
                <div class="col-lg-2 col-md-2 col-12">
                    <h6 class="product-name">
                            ${data[i].companyName}
                    </h6>
                    </div>
                <div class="col-lg-2 col-md-2 col-12">
                    <p>${data[i].productBrand}</p>
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
                </div>`
                document.querySelector(`.item-${data[i].wishId}`).addEventListener('click', async (event) =>{
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
                            body: JSON.stringify({wishId: [event.target.id]})
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

    }catch (e) {

    }
})();