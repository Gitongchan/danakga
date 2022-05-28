const tablelist = document.getElementById("boardlist");
const pagenation = document.querySelector('.text-center .pagination');

fetch(`/api/board/list/자유게시판?page=0`)
    .then((res)=>res.json())
    .then((data)=>{
        console.log(data);
        for(let datalist in data){
            const item = data[datalist];
            const tr = document.createElement('tr');
            const bdid = document.createElement('td');
            const bdtitle = document.createElement('td');
            const bdwriter = document.createElement('td');
            const bddate = document.createElement('td');
            const bdviews = document.createElement('td');

            bdid.innerText = item.bdId;
            bdtitle.innerText = item.bdTitle;
            bdwriter.innerText = item.bdWriter;
            bddate.innerText = item.bdCreated.split('.')[0];
            bdviews.innerText = item.bdViews;

            tr.appendChild(bdid);
            tr.appendChild(bdtitle);
            tr.appendChild(bdwriter);
            tr.appendChild(bddate);
            tr.appendChild(bdviews);
            tablelist.appendChild(tr);
        }
        (function() {
            pagenation.innerHTML += '<li class="page-item"><a class="page-link" href="#"><</a></li>';
            for(let i =0;i<data[0].totalPage;i++){
                pagenation.innerHTML += `<li class="page-item"><a class="page-link" href="${i}">${i+1}</a></li>`
            }
            pagenation.innerHTML += '<li class="page-item"><a class="page-link" href="#">></a></li>';
        })();
    })