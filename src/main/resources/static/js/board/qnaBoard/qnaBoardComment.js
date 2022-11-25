const comment = document.getElementById('comment-list');
const commentBtn = document.getElementById('comment-post');


//댓글 작성하기
commentBtn.addEventListener('click', async (event) => {
    event.preventDefault();

    const res = await fetch(`/api/admin/site_answer/write/${qnId}`, {
        method: 'POST',
        headers: {
            'header': header,
            'X-Requested-With': 'XMLHttpRequest',
            "Content-Type": "application/json",
            'X-CSRF-Token': token
        },
        body: JSON.stringify({answerContent: document.getElementById('comment-text').value})
    });

    const data = await res.json();

    if (res.status === 200) {
        alert(data.message);
        document.getElementById('comment-text').value = "";
        reload(0);
    }
});


const reload = async (page = 0) => {
    const res = await fetch(`/api/qna/answer_post/${qnId}?page=${page}`)

    if (!res.ok) {
        alert("오류가 발생했습니다!");
        return
    }
    const data = await res.json();

    const {answerList} = data;

    console.log(data);
    comment.innerHTML = "";
    for (const item of answerList) {
        comment.innerHTML += `
                 <li class="parent${item.an_id}">
                    <div class="comment-desc">
                        <div class="desc-top" id="${item.an_id}">
                            <div class="comment-info-wrap">
                                <h6 class="comment-uid">${item.an_writer}</h6>
                                <div class="btn-wrap">
                                    <button class="edit-link comment-edit">
                                        <i class="lni lni-comments-reply"></i>수정하기
                                    </button>
                                    <button class="delete-link comment-delete">
                                        <i class="lni lni-close"></i>삭제하기
                                    </button>
                                </div>
                            </div>
                        </div>
                        <span class="date">${item.an_created.split('.')[0]}</span>
                    </div>
                    <div class="form-group col-sm-12 comment" id="comment-text${item.an_id}">
                        <p>
                            ${item.an_content}
                        </p>
                    </div>
                </li>`
    }
}
// ---------------------------------------------댓글 --------------------------------------
//수정버튼
const commentEdit = document.querySelectorAll(".edit-link.comment-edit");
for (const editBtn of commentEdit) {
    editBtn.addEventListener('click', async (event) => {
        const val = event.target.offsetParent.id;
        document.getElementById(`comment-text${val}`).innerHTML =
            `<div class="form-group col-sm-12">
                    <textarea class="form-control" id="comment-val${val}"></textarea>
                </div>
                <div class="align-right mt-10">
                    <span class="button">
                        <button class="btn completeEdit${val}">수정하기</button>
                    </span>
                    <span class="button">
                        <button class="btn completeCancel${val}">취소</button>
                    </span>
                </div>`
        document.querySelector(`.btn.completeEdit${val}`).addEventListener('click', async (event) => {
            //댓글 수정 버튼 클릭 시
            const res = await fetch(`/api/admin/site_answer/edit/${qnId}/${val}`, {
                method: 'PUT',
                headers: {
                    'header': header,
                    "Content-Type": "application/json",
                    'X-CSRF-Token': token
                },
                body: JSON.stringify({answerContent: document.getElementById(`comment-val${val}`).value})
            });
            const data = await res.json();

            if (res.status === 200) {
                alert(data.message);
                reload(0);
            }
        })
        document.querySelector(`.btn.completeCancel${val}`).addEventListener('click', async (event) => {
            reload(0);
        })
    })
}

//삭제 버튼
const commentDelete = document.querySelectorAll(".delete-link.comment-delete");
for (const delBtn of commentDelete) {
    delBtn.addEventListener('click', async (event) => {
        if (confirm("삭제하시겠습니까?")) {
            const res = await fetch(`/api/admin/site_answer/delete/${qnId}/${event.target.offsetParent.id}`, {
                method: 'PUT',
                headers: {
                    'header': header,
                    'X-Requested-With': 'XMLHttpRequest',
                    "Content-Type": "application/json",
                    'X-CSRF-Token': token
                }
            })
            if (res.status === 200) {
                alert("삭제완료");
                reload(0);
            }
        }
    })
    renderPagination(answerList[0].totalPage, answerList[0].totalElement, reload);
}

reload();

