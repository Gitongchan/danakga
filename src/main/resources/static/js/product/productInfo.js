const pName = document.getElementById('p-title');
const pCompany = document.getElementById('p-company');
const pPrice = document.getElementById('p-price');
const pBrand = document.getElementById('brand-type-text');
const pMaintype = document.getElementById('main-type-text');
const pSubtype = document.getElementById('sub-type-text');
const pContent = document.getElementById('product-info');
const thumbPhoto = document.querySelector('.main-img');
const pImgs = document.querySelector('.images');

const editBtnAfter = document.querySelector('.product-info');

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

const urlID = getParameterByName('productId');

console.log(urlID);
console.log(comID);

(async ()=> {
        const res = await fetch(`/api/product/item/${urlID}`);
        const data = await res.json();
        console.log(data);

        if(res.status===200){
            const cRes = await fetch(`/api/manager`);
            if(cRes.status === 200){
                const cData = await cRes.json();
                if(data.companyId === cData.companyId){
                    alert("아이디비교도 성공")
                    const editDiv = document.createElement("div");
                    editDiv.className = "button mt-5 align-right";
                    editDiv.innerHTML =
                        `
                         <button class="btn">수정</button>
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
            pCompany.innerText = data.companyName;
            pMaintype.innerText = "메인종류 : " + data.productType;
            pSubtype.innerText = "세부종류 : " + data.productSubType;
            pBrand.innerText = "브랜드 : " + data.productBrand;
            pPrice.innerText = data.productPrice + "원";
            pContent.innerHTML = data.productContent;

        }
    })();


