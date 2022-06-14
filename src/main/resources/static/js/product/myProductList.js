// (상품번호, 상품아이디), 사업자 등록 번호, 상품종류, 상품 서브 종류, 상품 브랜드, 상품명
// 상품대표사진, 상품내용, 가격, 재고, 상품등록일, 조회수, 누적 구매수

(async function () {
    const res = await fetch('/api/manager/product/list?productName="%"&startDate=2022-05-01T01:30&endDate=2022-06-14T01:30&productStock=0')
    const data = await res.json();
    console.log(data);
})();
