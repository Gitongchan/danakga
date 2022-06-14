// {
//     "companyName":"간지작살",
//     "productId":1,
//     "productType":"낚시대",
//     "productSubType":"바다",
//     "productBrand":"털보네",
//     "productName":"강력한 낚시대",
//     "productPrice":"30000",
//     "productStock":100,
//     "productUploadDate":"2022-06-14T16:45:31.462871",
//     "productViewCount":0,
//     "productOrderCount":0,
//     "files":[{
//         "file_path": "product_files\\e7a32787-a68b-4a90-aef6-b49f32dfd9e6__2021-11-19 (1).png",
//         "file_name": "e7a32787-a68b-4a90-aef6-b49f32dfd9e6__2021-11-19 (1).png"
//     }, {
//         "file_path": "product_files\\08b2f6e2-358e-4232-b49a-5eceb7e2546a__2021-11-19.png",
//         "file_name": "08b2f6e2-358e-4232-b49a-5eceb7e2546a__2021-11-19.png"
//     }, {
//         "file_path": "product_files\\a61686b8-f67b-4b0b-92a0-429e3b7f6a75__kakaotalk_20220604_174439165.jpg",
//         "file_name": "a61686b8-f67b-4b0b-92a0-429e3b7f6a75__kakaotalk_20220604_174439165.jpg"
//     }]
// }

const pName = document.getElementById('p-title');
const pCompany = document.getElementById('p-company');
const pPrice = document.getElementById('p-price');
const pMaintype = document.getElementById('product-main');
const pSubtype = document.getElementById('product-sub');
const pBrand = document.getElementById('product-brand');
const pContent = document.getElementById('product-info');


function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

const urlID = getParameterByName('productId');

(async function getProduct() {
    const res = await fetch(`/api/product/item/${urlID}`);
    const data = await res.json();
    console.log(data);

    if(res.status===200){
        pName.innerText = data.productName;
        pCompany.innerText = data.companyName;
        pMaintype.innerText = data.productType;
        pSubtype.innerText = data.productSubType;
        pBrand.innerText = data.productBrand;
        pPrice.innerText = data.productPrice + "원";
    }
})();

