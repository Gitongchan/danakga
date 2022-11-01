const pName = document.getElementById('p-title');
const pCompany = document.getElementById('companyName');
const pPrice = document.getElementById('p-price');
const pBrand = document.getElementById('brand-type-text');
const pMaintype = document.getElementById('main-type-text');
const pSubtype = document.getElementById('sub-type-text');
const pContent = document.getElementById('product-info');
const thumbPhoto = document.querySelector('.main-img');
const pImgs = document.querySelector('.images');
const pViews = document.getElementById('views-count');
const pHeart = document.getElementById('heart-count');

const wishButton = document.querySelector('#wish_btn');
const editBtnAfter = document.querySelector('.product-info');
const cartButton = document.querySelector('#cart_btn');

// 리뷰작성 버튼이 들어갈 곳
const reviewBtnWrap = document.getElementById('review_btn');

// 별점 관련된 거
const sum_star = document.querySelector('.sum_star');
const star_1 = document.querySelector('.star-1');
const star_2 = document.querySelector('.star-2');
const star_3 = document.querySelector('.star-3');
const star_4 = document.querySelector('.star-4');
const star_5 = document.querySelector('.star-5');

// 리뷰 목록
const reviewWrap = document.getElementById('reviews_list_wrap');

//리뷰 수정가능 여부 hidden
const review_hidden = document.getElementById('review_edit');

const urlID = getParameterByName('productId');
const orderID = getParameterByName('orderId');
const status = getParameterByName('status');


(async ()=> {
        const res = await fetch(`/api/product/item/${urlID}`);
        const data = await res.json();
        console.log(data);

        if(res.status===200){
            try{
                const cRes = await fetch(`/api/manager`);
                if(cRes.status === 200){
                    const cData = await cRes.json();
                    if(data.companyId === cData.companyId){
                        const editDiv = document.createElement("div");
                        editDiv.className = "button mt-5 align-right";
                        editDiv.innerHTML =
                            `
                         <button class="btn" id="product-edit-page" onclick="location.replace('/product/edit?productId=${urlID}');">수정</button>
                        `
                        editBtnAfter.insertAdjacentElement("afterend",editDiv);
                    }
                }
                thumbPhoto.innerHTML =
                    `
            <img src="${data.productPhoto}" id="current" alt="#">
            `

                for(let i =0; i< data.files.length; i++){
                    const pImg = document.createElement('img');
                    pImg.classList.add('img');
                    pImg.src= data.files[i].file_path;
                    pImgs.appendChild(pImg);
                }


                pName.innerText = data.productName;
                pViews.textContent = data.productViewCount==="0" ? 0 : data.productViewCount;
                pHeart.textContent = data.productWishCount==="0" ? 0 : data.productWishCount;
                pCompany.innerHTML = `
                <i class="lni lni-tag"></i> 회사명:
                            <a href="/product/grid?shopName=${data.companyName}">
                                <span id="p-company">${data.companyName}</span>
                            </a>`
                pBrand.innerText = "브랜드 : " + (data.productBrand === "" ? "브랜드 없음" : data.productBrand);
                pMaintype.innerText = "메인종류 : " + data.productType;
                pSubtype.innerText = "세부종류 : " + data.productSubType;
                pPrice.innerText = data.productPrice + "원";
                pContent.innerHTML = data.productContent;

                // 해당 상품이 내 위시리스트에 있나 확인하기
                const wishRes = await fetch(`/api/user/wish/check/${urlID}`);
                const wishData = await wishRes.json();
                if(wishData.id === -1){
                    wishButton.innerHTML = `<i class="lni lni-heart"></i>찜하기`
                }else{
                    wishButton.innerHTML = `<i class="lni lni-heart-filled"></i></i>찜하기`
                }
                
            }catch (e) {
                thumbPhoto.innerHTML =
                    `
            <img src="${data.productPhoto}" id="current" alt="#">
            `

                for(let i =0; i< data.files.length; i++){
                    const pImg = document.createElement('img');
                    pImg.classList.add('img');
                    pImg.src= data.files[i].file_path;
                    pImgs.appendChild(pImg);
                }
                pName.innerText = data.productName;
                pViews.textContent = data.productViewCount==="0" ? 0 : data.productViewCount;
                pHeart.textContent = data.productWishCount==="0" ? 0 : data.productWishCount;
                pCompany.innerHTML = `
                <i class="lni lni-tag"></i> 회사명:
                            <a href="/product/grid?shopName=${data.companyName}">
                                <span id="p-company">${data.companyName}</span>
                            </a>`
                pBrand.innerText = "브랜드 : " + (data.productBrand === "" ? "브랜드 없음" : data.productBrand);
                pMaintype.innerText = "메인종류 : " + data.productType;
                pSubtype.innerText = "세부종류 : " + data.productSubType;
                pPrice.innerText = data.productPrice + "원";
                pContent.innerHTML = data.productContent;
            }
        }

        if(orderID === ''){
        }else{
            reviewBtnWrap.innerHTML = `
                            <button type="button" class="btn review-btn" data-bs-toggle="modal"
                                    data-bs-target="#exampleModal">
                                리뷰작성
                            </button>`

            //리뷰 작성버튼 눌렀을 때
            document.querySelector('#review_btn .btn.review-btn').addEventListener('click', () => {
                review_hidden.value = '';
            })
        }



        await star(urlID, 0);
        await reviewList(urlID, 0);
    })();

    //리뷰 작성버튼 누른후 작성하기 버튼 눌렀을 때
    const review_btn = document.querySelector('#write_review');
    review_btn.addEventListener('click', async () => {
        const reviewRating = document.getElementById('review-rating');
        const reviewRatingValue = reviewRating.options[reviewRating.selectedIndex].value;

        const text = document.getElementById('review-message');
        console.log(reviewRatingValue);
        if(review_hidden.value === ''){
            if(confirm("리뷰 작성을 하시겠습니까?")){
                const reviewWrite = await fetch(`/api/user/review/write`,{
                    method: 'POST',
                    headers: {
                        'header': header,
                        'X-CSRF-Token': token,
                        'Content-Type': 'application/json',
                        'X-Requested-With': 'XMLHttpRequest'
                    },
                    body: JSON.stringify({orderId: orderID, productId: urlID, reviewContent:text.value,reviewScore:reviewRatingValue+""})
                })
                if(reviewWrite.ok){
                    alert("리뷰작성이 완료되었습니다!");
                    location.reload();
                }
            }

        }else{
            const res = await fetch(`/api/user/review/edit/${review_hidden.value}`,{
                method: 'PUT',
                headers: {
                    'header': header,
                    'X-CSRF-Token': token,
                    'Content-Type': 'application/json',
                    'X-Requested-With': 'XMLHttpRequest'
                },
                body: JSON.stringify({orderId: orderID, productId: urlID, reviewContent:text.value,reviewScore:reviewRatingValue+""})
            })
            if(res.ok){
                const data = await res.json();
                alert("리뷰가 수정되었습니다!");
                location.reload();
            }
        }


    })


    //구매하기 버튼
    // document.getElementById('buyBtn').addEventListener('click',function (){
    //     const header = document.querySelector('meta[name="_csrf_header"]').content;
    //     const token = document.querySelector('meta[name="_csrf"]').content;
    //     const quant = document.getElementById('quantity');
    //
    //     if(confirm('바로 주문하시겠습니까?')) {
    //         const sum = (quant.options[quant.selectedIndex].value * pPrice.innerText.replace(regex, ""))
    //         console.log(`${sum}원 주문완료`);
    //         const data = {
    //             ordersQuantity: quant.options[quant.selectedIndex].value,
    //             ordersPrice: sum,
    //         }
    //         fetch(`/api/user/orders/${urlID}`,{
    //             method : 'POST',
    //             headers: {
    //                 'header': header,
    //                 'X-CSRF-Token': token,
    //                 'Content-Type': 'application/json',
    //                 'X-Requested-With': 'XMLHttpRequest'
    //             },
    //             body : JSON.stringify(data)
    //         })
    //             .then((res)=>res.json())
    //             .then((data)=>{
    //                 console.log(data);
    //                 if(data.result===1){
    //                     alert('주문성공');
    //                 }
    //             })
    //     }
    // })

    wishButton.addEventListener('click', async () => {
        // 찜하기 버튼을 눌렀을 때 동작해야 하는 곳
        try{
            const res = await fetch('/api/user/wish',{
                method: 'POST',
                headers: {
                    'header': header,
                    'X-CSRF-Token': token,
                    'Content-Type': 'application/json',
                    'X-Requested-With': 'XMLHttpRequest'
                },
                body: JSON.stringify({productId : urlID})
            });
            const data = await res.json();
            //위시리스트에서 제거 됐을 때
            if(data.id === -1){
                alert(data.message);
                location.reload();
            }else{
                // 위시리스트에 추가될 때
                alert(data.message);
                location.reload();
            }
        }catch (e) {
            console.log(e);
        }
    })

cartButton.addEventListener('click', async () => {
    const quantity = document.getElementById('quantity').value;
    if(quantity > 0){
        if(confirm('장바구니에 해당 상품을 추가하시겠습니까?')){
            try{
                const res = await fetch(`/api/user/cart`, {
                    method: 'POST',
                    headers: {
                        'header': header,
                        'X-CSRF-Token': token,
                        'Content-Type': 'application/json',
                        'X-Requested-With': 'XMLHttpRequest'
                    },
                    body: JSON.stringify([{productId:urlID, cartAmount: Number(quantity)}])
                })

                if(res.ok){
                    alert('장바구니에 상품이 추가되었습니다!');
                }
            }catch (e) {
                alert('장바구니에 상품추가 실패!');
            }
        }
    }else{
        alert("잘못입력된 수량입니다.")
    }
})

    //-------------------후기 부분 --------------------

    //별점 출력하는 곳
    async function star(p_id, page){
        let star1 = 0;
        let star2 = 0;
        let star3 = 0;
        let star4 = 0;
        let star5 = 0;
        let startSum = 0;
        const res = await fetch(`/api/review/reviewList/${p_id}?page=${page}`);
        if(res.ok){
            const data = await res.json();
            // re_content: "별로임..근데 뭐 쓸만은 함ㅋㅋㅋ"
            // re_created: "2022-08-18T18:58:32.622824"
            // re_id: 1
            // re_score: 3
            // re_writer: "ppwm1111"
            // totalElements: 1
            // totalPages: 1

            //모든 상품에 대한 별점 구하기
            const totalPages = data.reviewList[0].totalPages;
            for(let i = 0; i < totalPages; i++){
                const allres = await fetch(`/api/review/reviewList/${p_id}?page=${i}`);
                const allStar = await allres.json();
                for(let i in allStar.reviewList){
                    console.log(allStar.reviewList[i]);
                    if(allStar.reviewList[i].re_score === 1){
                        star1 += 1;
                        startSum += allStar.reviewList[i].re_score;
                    }else if(allStar.reviewList[i].re_score === 2){
                        star2 += 1;
                        startSum += allStar.reviewList[i].re_score;
                    }else if(allStar.reviewList[i].re_score === 3){
                        star3 += 1;
                        startSum += allStar.reviewList[i].re_score;
                    }else if(allStar.reviewList[i].re_score === 4){
                        star4 += 1;
                        startSum += allStar.reviewList[i].re_score;
                    }else if(allStar.reviewList[i].re_score === 5){
                        star5 += 1;
                        startSum += allStar.reviewList[i].re_score;
                    }
                }
            }
            star_1.textContent = `1 점 - ${star1}`;
            star_2.textContent = `2 점 - ${star2}`;
            star_3.textContent = `3 점 - ${star3}`;
            star_4.textContent = `4 점 - ${star4}`;
            star_5.textContent = `5 점 - ${star5}`;

            sum_star.textContent = `${startSum / (star1 + star2 + star3 + star4 + star5)}점`;
        }
    }

    // 리뷰 출력하는 곳
    async function reviewList(p_id, page){
        const res = await fetch(`/api/review/reviewList/${p_id}?page=${page}`);
        if(res.ok) {
            const data = await res.json();
            // re_content: "별로임..근데 뭐 쓸만은 함ㅋㅋㅋ"
            // re_created: "2022-08-18T18:58:32.622824"
            // re_id: 1
            // re_score: 3
            // re_writer: "ppwm1111"
            // totalElements: 1
            // totalPages: 1
            console.log(data);
            for (let i in data.reviewList) {
                if(data.reviewList[i].re_writer === checkName.value){
                    reviewWrap.innerHTML+= `
                                <div class="single-review">
                                    <div class="review-info">
                                        <div class="d-flex justify-content-between">
                                            <div>
                                                <h4>${data.reviewList[i].re_writer}
                                                  <span>${data.reviewList[i].re_created.split('.')[0]}</span>
                                                </h4>
                                            </div>
                                             <div>
                                                <button type="button" class="btn review-edit-btn" data-bs-toggle="modal"
                                                data-bs-target="#exampleModal" data-value="${data.reviewList[i].re_id}">
                                                     <i class="lni lni-comments-reply"></i>수정하기
                                                </button>
                                                <button class="review-delete" data-value="${data.reviewList[i].re_id}">
                                                    <i class="lni lni-close"></i>삭제하기
                                                </button>
                                            </div>
                                        </div>
                                        <ul class="stars stars${data.reviewList[i].re_id}">
                                        </ul>
                                        <p>${data.reviewList[i].re_content}</p>
                                    </div>
                                </div>
                `;
                    const star_wrap = document.querySelector(`.stars.stars${data.reviewList[i].re_id}`);
                    for(let j = 0; j < data.reviewList[i].re_score; j++){
                        star_wrap.innerHTML+=`
                     <li><i class="lni lni-star-filled"></i></li>
                    `;
                    }
                }else{
                    reviewWrap.innerHTML+= `
                                <div class="single-review">
                                    <div class="review-info">
                                        <div class="d-flex justify-content-between">
                                            <div>
                                                <h4>${data.reviewList[i].re_writer}
                                                  <span>${data.reviewList[i].re_created.split('.')[0]}</span>
                                                </h4>
                                            </div>
                                        </div>
                                        <ul class="stars stars${data.reviewList[i].re_id}">
                                        </ul>
                                        <p>${data.reviewList[i].re_content}</p>
                                    </div>
                                </div>
                `;
                    const star_wrap = document.querySelector(`.stars.stars${data.reviewList[i].re_id}`);
                    for(let j = 0; j < data.reviewList[i].re_score; j++){
                        star_wrap.innerHTML+=`
                     <li><i class="lni lni-star-filled"></i></li>
                    `;
                    }
                }

            }

            const reviews_edit = document.querySelectorAll('.btn.review-edit-btn');
            for(const button of reviews_edit){
                button.addEventListener('click', async (event) => {
                        review_hidden.value = event.target.dataset.value;
                })
            }

            const reviews_delete = document.querySelectorAll('.review-delete');
            for(const button of reviews_delete){
                button.addEventListener('click', async (event) => {
                    if(confirm('리뷰를 삭제하시겠습니까?')){
                        const res = await fetch(`/api/user/review/delete/${event.target.dataset.value}`, {
                            method: 'PUT',
                            headers: {
                                'header': header,
                                'X-CSRF-Token': token,
                                'Content-Type': 'application/json',
                                'X-Requested-With': 'XMLHttpRequest'
                            },
                            body: JSON.stringify({orderId:orderID, productId: urlID})
                        })
                        if(res.ok){
                            alert("리뷰가 삭제되었습니다!");
                            location.reload();
                        }
                    }
                })
            }
        }
    }
