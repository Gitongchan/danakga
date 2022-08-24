(async ()=>{
    // p_brand: "용성"
    // p_name: "용성2번"
    // p_price: "123"
    // re_created: "2022-08-18T18:58:32.622824"
    // re_score: 3
    // c: "ppwm1111"
    // totalElements: 2
    // totalPages: 1
    // data.reviewList[].~~~

    await myReviewList(0)
})();

const reviewListWrap = document.getElementById('myReviewList');

async function myReviewList(page){
    const res = await fetch(`/api/user/myReviewList?page=${page}`);

    if(res.ok){
        reviewListWrap.innerHTML='';
        const data = await res.json();
        for(let i in data.reviewList){
            console.log(data.reviewList[i]);
            const tr = document.createElement('tr');
            tr.innerHTML =
                `<td>${data.reviewList[i].p_brand}</td>
                 <td>${data.reviewList[i].p_name}</a></td>
                 <td>${data.reviewList[i].re_writer}</td>
                 <td>${data.reviewList[i].re_created.split('.')[0]}</td>
                 <td>${data.reviewList[i].re_score}</td>`
            reviewListWrap.appendChild(tr);
        }
    }
}