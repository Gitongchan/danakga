const comment = document.getElementById('comment-list');
const commentBtn = document.getElementById('comment-post');


//댓글 작성하기
commentBtn.addEventListener('click',async(e) => {
   e.preventDefault();

   const res = await fetch(`/api/user/comment/write/${boardID}`,{
       method:'POST',
       headers: {
           'header': header,
           'X-Requested-With': 'XMLHttpRequest',
           "Content-Type": "application/json",
           'X-CSRF-Token': token
       },
       body: JSON.stringify({cmContent:document.getElementById('comment-text').value})
   });
   const data = await res.json();

   if(res.status === 200){
       alert(data.message);
   }
});



const reload = async ()=> {
    const res = await fetch(`/api/board/post/comments/${boardID}?page=0`)
    const data = await res.json();

    if (res.status === 200) {
        console.log(data);
        comment.innerHTML = "";
        for (let i in data.comments) {
            comment.innerHTML +=
                `
                <li>
                    <div class="comment-desc">
                        <div class="desc-top" id="${data.comments[i].cm_id}">
                            <div class="comment-info-wrap">
                                <h6 class="comment-uid">${data.comments[i].cm_writer}</h6>
                                <div class="btn-wrap">
                                    <button class="edit-link comment-edit">
                                        <i class="lni lni-comments-reply"></i>수정하기
                                    </button>
                                    <button class="delete-link comment-delete">
                                        <i class="lni lni-close"></i>삭제하기
                                    </button>
                                    <button class="reply-link comment-reply">
                                        <i class="lni lni-reply"></i>답글달기
                                    </button>
                                </div>
                            </div>
                        </div>
                        <span class="date">${data.comments[i].cm_created.split('.')[0]}</span>
                    </div>
                    <div class="form-group col-sm-12 comment" id="comment-text${data.comments[i].cm_id}">
                        <p>
                            ${data.comments[i].cm_content}
                        </p>
                    </div>
                    
                </div>
        </li>`

        }
        //수정버튼
        const commentEdit = document.querySelectorAll(".edit-link.comment-edit");
        for(const editBtn of commentEdit){
            editBtn.addEventListener('click',async (e)=>{
                const val = e.target.offsetParent.id;
                document.getElementById(`comment-text${val}`).innerHTML=
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
                document.querySelector(`.btn.completeEdit${val}`).addEventListener('click',async (e)=>{
                    //댓글 수정 버튼 클릭 시
                    const res = await fetch(`/api/user/comment/edit/${boardID}/${val}`,{
                        method:'PUT',
                        headers: {
                            'header': header,
                            'X-Requested-With': 'XMLHttpRequest',
                            "Content-Type": "application/json",
                            'X-CSRF-Token': token
                        },
                        body: JSON.stringify({cmContent:document.getElementById(`comment-val${val}`).value})
                    });
                    const data = await res.json();

                    if(res.status === 200){
                        alert(data.message);
                        await reload();
                    }
                    console.log(e)
                })
                document.querySelector(`.btn.completeCancel${val}`).addEventListener('click',async (e)=>{
                    //코멘트 아이디 값만 넘겨서 해당 부분만 값 변경하기 (api필요)
                    console.log(e)
                })
                })
        }

        //삭제 버튼
        const commentDelete = document.querySelectorAll(".delete-link.comment-delete");
        for(const delBtn of commentDelete){
            delBtn.addEventListener('click',async (e)=>{
                if(confirm("삭제하시겠습니까?")){
                    const res = await fetch(`/api/user/comment/delete/${boardID}/${e.target.offsetParent.id}`,{
                        method:'PUT',
                        headers: {
                            'header': header,
                            'X-Requested-With': 'XMLHttpRequest',
                            "Content-Type": "application/json",
                            'X-CSRF-Token': token
                        }
                    })
                    if(res.status === 200){
                        alert("삭제완료");
                        await reload();
                    }
                }
            })
        }

        //답글 버튼
        const commentReply = document.querySelectorAll(".reply-link.comment-reply");
        for(const replyBtn of commentReply){
            replyBtn.addEventListener('click',async (e)=>{
                console.log(e);
                console.log("코멘트 아이디",e.target.offsetParent.id);
            })
        }
    }
}


// 댓글 조회 후 뿌리기
(async ()=>{
    await reload();
})();

