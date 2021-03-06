const pName = document.getElementById('productName');
const pPrice = document.getElementById('productPrice');
const pBrand = document.getElementById('brand-type');
const pMaintype = document.getElementById('main-type');
const pSubtype = document.getElementById('sub-type');
const pStock = document.getElementById('productStock');
const thumbPhoto = document.querySelector('.main-img');
const pImgs = document.querySelector('.images');

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

const urlID = getParameterByName('productId');

(async ()=>{
    const res = await fetch(`/api/product/item/${urlID}`);
    const data = await res.json();
    if(res.status === 200){
        pName.value = data.productName;
        pMaintype.value = data.productType;
        pSubtype.value = data.productSubType;
        pBrand.value = data.productBrand;
        pPrice.value = data.productPrice;
        pStock.value = data.productStock;
        editor.setData(data.productContent);
    }
})();