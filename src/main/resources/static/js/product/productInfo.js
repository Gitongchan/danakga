const pName = document.getElementById('p-title');
const pCompany = document.getElementById('p-company');
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

const regex= /[^0-9]/gi;

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}
const urlID = getParameterByName('productId');

console.log(urlID);

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
                pCompany.innerText = data.companyName;
                pBrand.innerText = "브랜드 : " + (data.productBrand === "" ? "브랜드 없음" : data.productBrand);
                pMaintype.innerText = "메인종류 : " + data.productType;
                pSubtype.innerText = "세부종류 : " + data.productSubType;
                pPrice.innerText = data.productPrice + "원";
                pContent.innerHTML = data.productContent;
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
                pCompany.innerText = data.companyName;
                pBrand.innerText = "브랜드 : " + (data.productBrand === "" ? "브랜드 없음" : data.productBrand);
                pMaintype.innerText = "메인종류 : " + data.productType;
                pSubtype.innerText = "세부종류 : " + data.productSubType;
                pPrice.innerText = data.productPrice + "원";
                pContent.innerHTML = data.productContent;
            }
        }
    })();

    //구매하기 버튼
    document.getElementById('buyBtn').addEventListener('click',function (){
        const header = document.querySelector('meta[name="_csrf_header"]').content;
        const token = document.querySelector('meta[name="_csrf"]').content;
        const quant = document.getElementById('quantity');

        if(confirm('바로 주문하시겠습니까?')) {
            const sum = (quant.options[quant.selectedIndex].value * pPrice.innerText.replace(regex, ""))
            console.log(`${sum}원 주문완료`);
            const data = {
                ordersQuantity: quant.options[quant.selectedIndex].value,
                ordersPrice: sum,
            }
            fetch(`/api/user/orders/${urlID}`,{
                method : 'POST',
                headers: {
                    'header': header,
                    'X-CSRF-Token': token,
                    'Content-Type': 'application/json',
                    'X-Requested-With': 'XMLHttpRequest'
                },
                body : JSON.stringify(data)
            })
                .then((res)=>res.json())
                .then((data)=>{
                    console.log(data);
                    if(data.result===1){
                        alert('주문성공');
                    }
                })
        }
    })

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
