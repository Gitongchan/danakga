const tablelist = document.getElementById("boardlist");

fetch(`/api/board/list/자유게시판?page=0`)
    .then((res)=>res.json())
    .then((data)=>{
        console.log(data);
        for(let datalist in data){
            const item = data[datalist];
            const tr = document.createElement('tr');
            tr.innerHTML =
                `<td>${item.bdId}</td>
                 <td><a href="/blog?boardid=${item.bdId}">${item.bdTitle}</a></td>
                 <td>${item.bdWriter}</td>
                 <td>${item.bdCreated.split('.')[0]}</td>
                 <td>${item.bdViews}</td>`

            tablelist.appendChild(tr);
        }
        // (function() {
        //     pagenation.innerHTML += '<li class="page-item"><a class="page-link" href="#"><</a></li>';
        //     for(let i =0;i<data[0].totalPage;i++){
        //         pagenation.innerHTML += `<li class="page-item"><a class="page-link" href="${i}">${i+1}</a></li>`
        //     }
        //     pagenation.innerHTML += '<li class="page-item"><a class="page-link" href="#">></a></li>';
        // })();
    })
.catch((err)=> console.log(err));