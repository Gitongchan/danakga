const tablelist = document.getElementById("boardlist");

async function renderQnaList(page = 0) {
    const res = await fetch(`/api/qna/list/site/0?page=${page}`)

    if (!res.ok) {
        alert('조회 실패!');
        return null;
    }

    const data = await res.json();
    const {qnaList} = data;
    console.log(qnaList);
    tablelist.innerHTML = '';

    for (const item of qnaList) {
        const tr = document.createElement('tr');
        tr.innerHTML =
            `<td>${item.qn_id}</td>
                 <td>${item.qn_type}</a></td>
                 <td><a href="/qnaboard/info?qnId=${item.qn_id}&userId=${item.qn_userid}">${item.qn_title}</a></td>
                 <td>${item.qn_userid}</td>
                 <td>${item.qn_created.split('.')[0]}</td>`

        tablelist.appendChild(tr);
    }
    renderPagination(qnaList[0].totalPage, qnaList[0].totalElement, renderQnaList);
}

renderQnaList();