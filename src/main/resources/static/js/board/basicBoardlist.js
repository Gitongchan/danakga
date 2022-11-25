const tablelist = document.getElementById("boardlist");

const renderBoardList = async (page) => {
    const res = await fetch(`/api/board/list/자유게시판?page=${page}`);

    if(!res.ok){
        alert("오류가 발생했습니다!")
    }
    const { lists } = await res.json();

    tablelist.innerHTML = '';
    renderPagination(lists[0].totalPage, lists[0].totalElement, renderBoardList);

    for(let item in lists){
        const tr = document.createElement('tr');
        tr.innerHTML =
            `<td>${lists[item].bd_id}</td>
                 <td><a href="/board/info?bdType=free&boardid=${lists[item].bd_id}?bdwriter=${lists[item].bd_writer}">${lists[item].bd_title}</a></td>
                 <td>${lists[item].bd_writer}</td>
                 <td>${lists[item].bd_created.split('.')[0]}</td>
                 <td>${lists[item].bd_views}</td>`

        tablelist.appendChild(tr);
    }
}

renderBoardList(0);