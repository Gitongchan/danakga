const tablelist = document.getElementById("boardlist");

fetch(`/api/board/list/자유게시판?page=0`)
    .then((res)=>res.json())
    .then((data)=>{
        for(let i=0; i<data.lists.length; i++){
            const tr = document.createElement('tr');
            tr.innerHTML =
                `<td>${data.lists[i].bd_id}</td>
                 <td><a href="/board/info?bdType=free&boardid=${data.lists[i].bd_id}?bdwriter=${data.lists[i].bd_writer}">${data.lists[i].bd_title}</a></td>
                 <td>${data.lists[i].bd_writer}</td>
                 <td>${data.lists[i].bd_created.split('.')[0]}</td>
                 <td>${data.lists[i].bd_views}</td>`

            tablelist.appendChild(tr);
        }
    })
.catch((err)=> console.log(err));