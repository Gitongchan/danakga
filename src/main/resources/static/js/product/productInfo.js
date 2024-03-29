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

//Q&A작성 관련 부분
const $reviewList_btn = document.querySelector('#reviewList_btn');
const $qnaList_btn = document.querySelector('#qnaList_btn');
const $qnaComment = document.querySelector('#qna-post');

const productId = getParameterByName('productId');
const orderID = getParameterByName('orderId');
const status = getParameterByName('status');


(async ()=> {
        const res = await fetch(`/api/product/item/${productId}`);
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
                         <button class="btn" id="product-edit-page" onclick="location.replace('/product/edit?productId=${productId}');">수정</button>
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
                const wishRes = await fetch(`/api/user/wish/check/${productId}`);
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



        await star(0);
        await reviewList(0);
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
                    },
                    body: JSON.stringify({orderId: orderID, productId: productId, reviewContent:text.value,reviewScore:reviewRatingValue+""})
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
                },
                body: JSON.stringify({orderId: orderID, productId: productId, reviewContent:text.value,reviewScore:reviewRatingValue+""})
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
    //         fetch(`/api/user/orders/${productId}`,{
    //             method : 'POST',
    //             headers: {
    //                 'header': header,
    //                 'X-CSRF-Token': token,
    //                 'Content-Type': 'application/json',
    //
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
                },
                body: JSON.stringify({productId : productId})
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
                        },
                        body: JSON.stringify([{productId:productId, cartAmount: Number(quantity)}])
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
    async function star(page){
        let star1 = 0;
        let star2 = 0;
        let star3 = 0;
        let star4 = 0;
        let star5 = 0;
        let startSum = 0;
        const res = await fetch(`/api/review/reviewList/${productId}?page=${page}`);
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
                const allres = await fetch(`/api/review/reviewList/${productId}?page=${i}`);
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
    async function reviewList(page){
        const res = await fetch(`/api/review/reviewList/${productId}?page=${page}`);
        pagenation.innerHTML = "";

        if(res.ok) {
            reviewWrap.innerHTML = '';
            const data = await res.json();
            // re_content: "별로임..근데 뭐 쓸만은 함ㅋㅋㅋ"
            // re_created: "2022-08-18T18:58:32.622824"
            // re_id: 1
            // re_score: 3
            // re_writer: "ppwm1111"
            // totalElements: 1
            // totalPages: 1

            productInfoPagination(data.reviewList[0].totalPages, data.reviewList[0].totalElements, reviewList)
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
                                                <button class="btn review-delete" data-value="${data.reviewList[i].re_id}">
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
                            },
                            body: JSON.stringify({orderId:orderID, productId: productId})
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

    // 리뷰버튼, Q&A버튼 클릭 시 동작하는 곳
    $reviewList_btn.addEventListener('click', () => {
        $reviewList_btn.classList.add('active');
        $qnaList_btn.classList.remove('active');
        document.querySelector('.comment-form').classList.add('hide');
        reviewList(0);
    })

    $qnaList_btn.addEventListener('click', () => {
        reviewWrap.innerHTML = '';
        $reviewList_btn.classList.remove('active');
        $qnaList_btn.classList.add('active');
        document.querySelector('.comment-form').classList.remove('hide');
        qnaList(0);
    })

    //qna작성버튼 클릭 시
    $qnaComment.addEventListener('click', async (event) =>{
        const content = document.querySelector('#qna-text');
        event.preventDefault();
        const res = await fetch(`/api/user/qna/product_write/${productId}`,{
            method: 'POST',
            headers: {
                'header': header,
                'X-CSRF-Token': token,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                qnaType:'',
                qnaTitle:'',
                qnaContent: content.value,
                qnaSort: 1
            })
        })

        if(!res.ok){
            alert("문의사항 작성에 실패했습니다!")
        }
        alert('Q&A를 작성하였습니다!');
        content.value = '';
        qnaList(0);
    })

    async function qnaList(page){
        const res = await fetch(`/api/qna/list/product/${productId}?page=${page}`);
        const data = await res.json();

        pagenation.innerHTML = "";
        if(!res.ok){
            alert("로드 실패!")
            return null;
        }

        productInfoPagination(data.qnaList[0].totalPage, data.qnaList[0].totalElement, qnaList);
        console.log(data);
        reviewWrap.innerHTML = '';
        for (const item of data.qnaList) {
            if(item.qn_userid === checkName.value){
                reviewWrap.innerHTML+= `
                                <div class="single-review" id="review-info${item.qn_id}">
                                    <div class="review-info" >
                                        <div class="d-flex justify-content-between">
                                            <div>
                                                <h4>${item.qn_userid}
                                                  <span>${item.qn_created.split('.')[0]}</span>
                                                </h4>
                                            </div>
                                             <div>
                                                <button type="button" class="btn product-qna-edit" data-id="${item.qn_id}">
                                                     <i class="lni lni-comments-reply"></i>수정하기
                                                </button>
                                                <button class="btn product-qna-delete" data-id="${item.qn_id}">
                                                    <i class="lni lni-close"></i>삭제하기
                                                </button>
                                            </div>
                                        </div>
                                        <p id="qna">${item.qn_content}</p>
                                    </div>
                                </div>
                `;
            }else if(item.company_id === Number(checkCompany.value)){
                reviewWrap.innerHTML+= `
                                    <div class="single-review" id="review-info${item.qn_id}">
                                        <div class="review-info">
                                            <div class="d-flex justify-content-between">
                                                <div>
                                                    <h4>${item.qn_userid}
                                                      <span>${item.qn_created.split('.')[0]}</span>
                                                    </h4>
                                                </div>
                                                     <div>
                                                    <button type="button" class="btn product-qna-reply" data-id="${item.qn_id}">
                                                         <i class="lni lni-comments-reply"></i>답글달기
                                                    </button>
                                                </div>
                                            </div>
                                            <p>${item.qn_content}</p>
                                        </div>
                                    </div>
                    `;
            }else{
                reviewWrap.innerHTML+= `
                                    <div class="single-review" id="review-info${item.qn_id}">
                                        <div class="review-info">
                                            <div class="d-flex justify-content-between">
                                                <div>
                                                    <h4>${item.qn_userid}
                                                      <span>${item.qn_created.split('.')[0]}</span>
                                                    </h4>
                                                </div>
                                                     <div>
                                                    <button type="button" class="btn product-qna-reply" data-id="${item.qn_id}">
                                                         <i class="lni lni-comments-reply"></i>답글달기
                                                    </button>
                                                </div>
                                            </div>
                                            <p>${item.qn_content}</p>
                                        </div>
                                    </div>
                    `;
            }


            if(item.answer.length !== 0 ){
                // 답글 출력?
                for(const reply of item.answer){
                    if(reply.company_id === Number(checkCompany.value)){
                        reviewWrap.innerHTML += `
                                <div class="single-review children">
                                    <div class="review-info">
                                        <div class="d-flex justify-content-between">
                                            <div>
                                                <h4>${item.company_name}
                                                  <span>${reply.an_create.split('.')[0]}</span>
                                                </h4>
                                            </div>
                                             <div>
                                                <button type="button" class="btn product-qna-manager-edit" data-id="${reply.an_id}" data-qnid="${item.qn_id}">
                                                     <i class="lni lni-comments-reply"></i>수정하기
                                                </button>
                                                <button class="btn product-qna-manager-delete" data-id="${reply.an_id}" data-qnid="${item.qn_id}">
                                                    <i class="lni lni-close"></i>삭제하기
                                                </button>
                                            </div>
                                        </div>
                                        <p id="qna">${reply.an_content}</p>
                                    </div>
                                </div>`
                    }else{
                        reviewWrap.innerHTML += `
                                <div class="single-review children">
                                    <div class="review-info">
                                        <div class="d-flex justify-content-between">
                                            <div>
                                                <h4>${item.company_name}
                                                  <span>${reply.an_create.split('.')[0]}</span>
                                                </h4>
                                            </div>
                                        </div>
                                        <p id="qna">${reply.an_content}</p>
                                    </div>
                                </div>`
                    }
                }
            }
        }

        // qna 수정 버튼
        const qnaEditBtn = document.querySelectorAll('.product-qna-edit');
        for(const button of qnaEditBtn){
            button.addEventListener('click', (event) => {
                const id = event.target.dataset.id;
                console.log(id);
                document.querySelector('#qna').innerHTML = `<div class="form-group col-sm-12">
                    <textarea class="form-control" id="comment-val${id}"></textarea>
                </div>
                <div class="align-right mt-10">
                    <span class="button">
                        <button class="btn completeEdit${id}">수정하기</button>
                    </span>
                    <span class="button">
                        <button class="btn completeCancel${id}">취소</button>
                    </span>
                </div>`

                document.querySelector(`.btn.completeEdit${id}`).addEventListener('click',async (event)=>{
                    //댓글 수정 버튼 클릭 시
                    const res = await fetch(`/api/user/qna/product_edit/${productId}/${id}`,{
                        method:'PUT',
                        headers: {
                            'header': header,
                            "Content-Type": "application/json",
                            'X-CSRF-Token': token
                        },
                        body: JSON.stringify({qnaType:'',qnaTitle:'', qnaContent:document.getElementById(`comment-val${id}`).value})
                    });
                    const data = await res.json();

                    if(res.status === 200){
                        alert(data.message);
                        await qnaList(0);
                    }
                })
                document.querySelector(`.btn.completeCancel${id}`).addEventListener('click',async (event)=>{
                    qnaList(0);
                })
            })

        }

        // qna 삭제 버튼
        const qnaDeleteBtn = document.querySelectorAll('.product-qna-delete');
        for(const button of qnaDeleteBtn){
            button.addEventListener('click', async (event) => {
                const id = event.target.dataset.id; // qn_id값
                if(confirm('삭제하시겠습니까?')){
                    const res = await fetch(`/api/user/qna/product_delete/${productId}/${id}`,{
                        method:"PUT",
                        headers: {
                            'header': header,
                            'X-CSRF-Token': token,
                            'Content-Type': 'application/json',
                        },
                    })
                    if(!res.ok){
                        alert("삭제 실패!")
                    }
                    alert("삭제하였습니다!")
                    qnaList(0);
                }
            })
        }

        // qna 답글달기 버튼
        const qnaReplyBtn = document.querySelectorAll('.product-qna-reply');
        for(const button of qnaReplyBtn){
            button.addEventListener('click',  (event) => {
                const id = event.target.dataset.id; // qn_id값
                document.querySelector(`#review-info${id}`).insertAdjacentHTML('beforeend', `<div class="form-group col-sm-12">
                                        <textarea class="form-control" id="comment-val${id}"></textarea>
                                    </div>
                                    <div class="align-right mt-10">
                                        <span class="button">
                                            <button class="btn completeReply${id}">답글달기</button>
                                        </span>
                                        <span class="button">
                                            <button class="btn completeCancel${id}">취소</button>
                                        </span>
                                    </div>`
                )

                document.querySelector(`.btn.completeReply${id}`).addEventListener('click',async (event)=>{
                    //댓글 수정 버튼 클릭 시
                    const res = await fetch(`/api/manager/product_answer/write/${productId}/${id}`,{
                        method:'POST',
                        headers: {
                            'header': header,
                            "Content-Type": "application/json",
                            'X-CSRF-Token': token
                        },
                        body: JSON.stringify({answerContent:document.getElementById(`comment-val${id}`).value})
                    });
                    const data = await res.json();

                    alert(data.message);
                    await qnaList(0);
                })
                document.querySelector(`.btn.completeCancel${id}`).addEventListener('click',async (event)=>{
                    qnaList(0);
                })
            })
        }

        // 매니저가 작성한 qna 수정 버튼
        const qnaManagerEditBtn = document.querySelectorAll('.product-qna-manager-edit');
        for(const button of qnaManagerEditBtn){
            button.addEventListener('click', (event) => {
                const id = event.target.dataset.id;
                const qnId = event.target.dataset.qnid;

                console.log(id);
                document.querySelector('#qna').innerHTML = `<div class="form-group col-sm-12">
                    <textarea class="form-control" id="comment-val${id}"></textarea>
                </div>
                <div class="align-right mt-10">
                    <span class="button">
                        <button class="btn completeEdit${id}">수정하기</button>
                    </span>
                    <span class="button">
                        <button class="btn completeCancel${id}">취소</button>
                    </span>
                </div>`

                document.querySelector(`.btn.completeEdit${id}`).addEventListener('click',async (event)=>{
                    //댓글 수정 버튼 클릭 시
                    const res = await fetch(`/api/manager/product_answer/edit/${productId}/${qnId}/${id}`,{
                        method:'PUT',
                        headers: {
                            'header': header,
                            "Content-Type": "application/json",
                            'X-CSRF-Token': token
                        },
                        body: JSON.stringify({answerContent:document.getElementById(`comment-val${id}`).value})
                    });
                    const data = await res.json();

                    if(res.status === 200){
                        alert(data.message);
                        await qnaList(0);
                    }
                })
                document.querySelector(`.btn.completeCancel${id}`).addEventListener('click',async (event)=>{
                    qnaList(0);
                })
            })

        }

        // 매니저가 작성한 qna 삭제 버튼
        const qnaManagerDeleteBtn = document.querySelectorAll('.product-qna-manager-delete');
        for(const button of qnaManagerDeleteBtn){
            button.addEventListener('click', async (event) => {
                const id = event.target.dataset.id; // an_id값
                const qnId = event.target.dataset.qnid;
                if(confirm('삭제하시겠습니까?')){
                    const res = await fetch(`/api/manager/product_answer/delete/${productId}/${qnId}/${id}`,{
                        method:"PUT",
                        headers: {
                            'header': header,
                            'X-CSRF-Token': token,
                            'Content-Type': 'application/json',
                        },
                    })
                    if(!res.ok){
                        alert("삭제 실패!")
                    }
                    alert("삭제하였습니다!")
                    qnaList(0);
                }
            })
        }

    }
