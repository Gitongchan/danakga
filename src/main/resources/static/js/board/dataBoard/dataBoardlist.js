const tablelist = document.getElementById("boardlist");

fetch(`/api/board/list/정보게시판?page=0`)
    .then((res)=>res.json())
    .then((data)=>{
        for(let i=0; i<data.lists.length; i++){
            console.log(data.lists[i]);
            const tr = document.createElement('tr');
            tr.innerHTML =
                `<td>${data.lists.length - i}</td>
                 <td><a href="/board/info?bdType=data&boardid=${data.lists[i].bd_id}?bdwriter=${data.lists[i].bd_writer}">${data.lists[i].bd_title}</a></td>
                 <td>${data.lists[i].bd_writer}</td>
                 <td>${data.lists[i].bd_created.split('.')[0]}</td>
                 <td>${data.lists[i].bd_views}</td>`

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