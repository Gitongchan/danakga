const orderItem = document.getElementById('orderd-item');
const orderCount = document.getElementById('order-count');
const uploadCount = document.getElementById('upload-count');
const viewCount = document.getElementById('view-count');
const manyView = document.getElementById('many-view');

    //업로드 일 순
    fetch('/api/product/main-page/list/productUploadDate?page=0')
        .then((res) => res.json())
        .then((data) => {
            uploadCount.innerHTML+= `<h4 class="list-title">신규 상품 순</h4>`;
            for(let u1=0; u1<3; u1++){
                uploadCount.innerHTML+=` 
                    <!-- Start Single List -->
                    <div class="single-list">
                        <div class="list-image">
                            <a href="/product/info?productId=${data[u1].productId}">
                            <img src=${data[u1].productPhoto} alt="#"></a>
                        </div>
                        <div class="list-info">
                            <h3>
                                <a href="/product/info?productId=${data[u1].productId}">${data[u1].productName}</a>
                            </h3>
                            <span>${data[u1].productPrice}원</span>
                        </div>
                    </div>`
            }
        })

    //주문순
    //productRating
    fetch('/api/product/main-page/list/productOrderCount?page=0')
        .then((res) => res.json())
        .then((data) => {
            for (const item of data) {

                console.log(data)
                orderItem.innerHTML += `
                    <div class="col-lg-3 col-md-6 col-12">
                        <!-- Start Single Product -->
                        <div class="single-product">
                            <div class="product-image">
                                <img src=${item.productPhoto} alt="#">
                            </div>
                            <div class="product-info">
                                <span class="category">${item.productType}</span>
                                <h4 class="title">
                                    <a href="/product/info?productId=${item.productId}">${item.productName}</a>
                                </h4>
                                <ul class="review">
                                    <li><i class="lni lni-star star-1_${item.productId}"></i></li>
                                    <li><i class="lni lni-star star-2_${item.productId}"></i></li>
                                    <li><i class="lni lni-star star-3_${item.productId}"></i></li>
                                    <li><i class="lni lni-star star-4_${item.productId}"></i></li>
                                    <li><i class="lni lni-star star-5_${item.productId}"></i></li>
                                    <li><span>${item.productRating}점</span></li>
                                </ul>
                                <div class="price">
                                    <span>${item.productPrice}원</span>
                                </div>
                            </div>
                        </div>
                        <!-- End Single Product -->
                    </div>`

                for(let i=1; i <= item.productRating; i++){
                    document.querySelector(`.star-${i}_${item.productId}`).classList.remove('lni-star')
                    document.querySelector(`.star-${i}_${item.productId}`).classList.add('lni-star-filled')
                }

            }

            orderCount.innerHTML+= `<h4 class="list-title">주문 최다 순</h4>`;
            for(let o = 0; o < 3; o++){
                orderCount.innerHTML+=` 
                    <!-- Start Single List -->
                    <div class="single-list">
                        <div class="list-image">
                            <a href="/product/info?productId=${data[o].productId}">
                            <img src=${data[o].productPhoto} alt="#"></a>
                        </div>
                        <div class="list-info">
                            <h3>
                                <a href="/product/info?productId=${data[o].productId}">${data[o].productName}</a>
                            </h3>
                            <span>${data[o].productPrice}원</span>
                        </div>
                    </div>`
            }
        })

    //조회순
    fetch('/api/product/main-page/list/productViewCount?page=0')
        .then((res)=>res.json())
        .then((data)=>{
                viewCount.innerHTML+= `<h4 class="list-title">조회 최다 순</h4>`;
            for(let v = 0; v < 3; v++){
                viewCount.innerHTML+=` 
                    <!-- Start Single List -->
                    <div class="single-list">
                        <div class="list-image">
                            <a href="/product/info?productId=${data[v].productId}">
                            <img src=${data[v].productPhoto} alt="#"></a>
                        </div>
                        <div class="list-info">
                            <h3>
                                <a href="/product/info?productId=${data[v].productId}">${data[v].productName}</a>
                            </h3>
                            <span>${data[v].productPrice}원</span>
                        </div>
                    </div>`
            }


            manyView.innerHTML+=
                `<div class="col-12">
                    <div class="section-title">
                        <h2>최다 조회 상품</h2>
                        <p>조회가 가장 많은 장비들이에요!</p>
                    </div>
                </div>`
            for(let v1 = 0; v1 < 2; v1++){
                manyView.innerHTML+=`
                <div class="col-lg-6 col-md-6 col-12">
                    <div class="single-banner">
                        <img src=${data[v1].productPhoto} alt="#"></a>
                        <div class="content" >
                            <h2>${data[v1].productName}</h2>
                            <p>조회 수 ${v1+1}등 <br>가장 많이 조회된 장비! </p>
                            <div class="button">
                                <a href="/product/info?productId=${data[v1].productId}" class="btn">상세보기</a>
                            </div>
                        </div>
                    </div>
                </div>
                `
            }

        })

// 이미지에 장바구니 버튼
// `
// <div class="product-image">
//                                 <img src=${item.productPhoto} alt="#">
//                                     <div class="button">
//                                         <a href="#" class="btn"><i class="lni lni-cart"></i> 장바구니</a>
//                                     </div>
//                             </div>`
