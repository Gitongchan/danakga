const comment = document.getElementById('comment-list');
const commentBtn = document.getElementById('comment-post');


//댓글 작성하기
commentBtn.addEventListener('click',async(event) => {
    event.preventDefault();

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
       document.getElementById('comment-text').value = "";
       await reload(0);
   }
});



const reload = async (page = 0)=> {
    const res = await fetch(`/api/board/post/comments/${boardID}?page=${page}`)

    if (!res.ok) {
        console.log('오류 발생!');
        return;
    }
    const data = await res.json();

    const { comments } = data;

    console.log(comments);
        console.log(data);
        comment.innerHTML = "";
        for (let i in comments) {

            // 대댓글이 있나 확인
            if(comments[i].answer.length===0){
                if(comments[i].cm_writer==checkName.value){
                    comment.innerHTML +=
                        `
                <li class="parent${comments[i].cm_id}">
                    <div class="comment-desc">
                        <div class="desc-top" id="${comments[i].cm_id}">
                            <div class="comment-info-wrap">
                                <h6 class="comment-uid">${comments[i].cm_writer}</h6>
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
                        <span class="date">${comments[i].cm_created.split('.')[0]}</span>
                    </div>
                    <div class="form-group col-sm-12 comment" id="comment-text${comments[i].cm_id}">
                        <p>
                            ${comments[i].cm_content}
                        </p>
                    </div>
                 </li>`
                }else{
                    comment.innerHTML +=
                        `
                <li class="parent${comments[i].cm_id}">
                    <div class="comment-desc">
                        <div class="desc-top" id="${comments[i].cm_id}">
                            <div class="comment-info-wrap">
                                <h6 class="comment-uid">${comments[i].cm_writer}</h6>
                                <div class="btn-wrap">
                                    <button class="reply-link comment-reply">
                                        <i class="lni lni-reply"></i>답글달기
                                    </button>
                                </div>
                            </div>
                        </div>
                        <span class="date">${comments[i].cm_created.split('.')[0]}</span>
                    </div>
                    <div class="form-group col-sm-12 comment" id="comment-text${comments[i].cm_id}">
                        <p>
                            ${comments[i].cm_content}
                        </p>
                    </div>
                </li>`
                }
            }else{
                // 대댓글이 있을경우
                if(comments[i].cm_writer==checkName.value){
                    comment.innerHTML +=
                        `
                <li class="parent${comments[i].cm_id}">
                    <div class="comment-desc">
                        <div class="desc-top" id="${comments[i].cm_id}">
                            <div class="comment-info-wrap">
                                <h6 class="comment-uid">${comments[i].cm_writer}</h6>
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
                        <span class="date">${comments[i].cm_created.split('.')[0]}</span>
                    </div>
                    <div class="form-group col-sm-12 comment" id="comment-text${comments[i].cm_id}">
                        <p>
                            ${comments[i].cm_content}
                        </p>
                    </div>
                </li>`
                }else{
                    // 댓글작성자와 로그인한 아이디가 일치하지 않을떄
                    comment.innerHTML +=
                        `
                <li class="parent${comments[i].cm_id}">
                    <div class="comment-desc">
                        <div class="desc-top" id="${comments[i].cm_id}">
                            <div class="comment-info-wrap">
                                <h6 class="comment-uid">${comments[i].cm_writer}</h6>
                                <div class="btn-wrap">
                                    <button class="reply-link comment-reply">
                                        <i class="lni lni-reply"></i>답글달기
                                    </button>
                                </div>
                            </div>
                        </div>
                        <span class="date">${comments[i].cm_created.split('.')[0]}</span>
                       </div>
                    <div class="form-group col-sm-12 comment" id="comment-text${comments[i].cm_id}">
                        <p>
                            ${comments[i].cm_content}
                        </p>
                    </div>
                </li>`
                }
                // 대댓글을 순회하며 로그인 아이디와 대댓글 아이디 비교해서 수정삭제 버튼 보여지게
                for (let j in comments[i].answer) {
                    if(comments[i].answer[j].an_writer == checkName.value){
                        comment.innerHTML += `
                        <li class="children">
                            <div class="comment-desc">
                                <div class="desc-top" id="${comments[i].answer[j].an_id}">
                                    <div class="comment-info-wrap">
                                        <h6 class="comment-uid">${comments[i].answer[j].an_writer}</h6>
                                        <div class="btn-wrap">
                                            <button class="edit-link reply-edit" data-id="${comments[i].answer[j].an_parentNum}">
                                                <i class="lni lni-comments-reply"></i>수정하기
                                            </button>
                                            <button class="delete-link reply-delete" data-id="${comments[i].answer[j].an_parentNum}">
                                                <i class="lni lni-close"></i>삭제하기
                                            </button>
                                        </div>
                                    </div>
                                </div>
                                <span class="date">${comments[i].answer[j].an_created.split('.')[0]}</span>
                            </div>
                            <div class="form-group col-sm-12 comment" id="comment-text${comments[i].answer[j]._id}">
                                <p>
                                    ${comments[i].answer[j].an_content}
                                </p>
                            </div>
                        </li>`
                    }else{
                        comment.innerHTML += `
                        <li class="children">
                            <div class="comment-desc">
                                <div class="desc-top" id="${comments[i].answer[j].an_id}">
                                    <div class="comment-info-wrap">
                                        <h6 class="comment-uid">${comments[i].answer[j].an_writer}</h6>
                                        <div class="btn-wrap">
                                        </div>
                                    </div>
                                </div>
                                <span class="date">${comments[i].answer[j].an_created.split('.')[0]}</span>
                            </div>
                            <div class="form-group col-sm-12 comment" id="comment-text${comments[i].answer[j].an_id}">
                                <p>
                                    ${comments[i].answer[j].an_content}
                                </p>
                            </div>
                        </li>`
                    }
                }
            }
        }
        // ---------------------------------------------댓글 --------------------------------------
        //수정버튼
        const commentEdit = document.querySelectorAll(".edit-link.comment-edit");
        for(const editBtn of commentEdit){
            editBtn.addEventListener('click',async (event)=>{
                const val = event.target.offsetParent.id;
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
                document.querySelector(`.btn.completeEdit${val}`).addEventListener('click',async (event)=>{
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
                        await reload(e);
                    }
                })
                document.querySelector(`.btn.completeCancel${val}`).addEventListener('click',async (event)=>{
                    const res = await fetch(`/api/board/comment/check/${val}`)
                    const data = await res.json();
                    if(res.status === 200){
                        await reload(e);
                    }
                })
                })
        }

        //삭제 버튼
        const commentDelete = document.querySelectorAll(".delete-link.comment-delete");
        for(const delBtn of commentDelete){
            delBtn.addEventListener('click',async (event)=>{
                if(confirm("삭제하시겠습니까?")){
                    const res = await fetch(`/api/user/comment/delete/${boardID}/${event.target.offsetParent.id}`,{
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
                        await reload(e);
                    }
                }
            })
        }

        //답글 버튼
        const commentReply = document.querySelectorAll(".reply-link.comment-reply");
        for(const replyBtn of commentReply){
            replyBtn.addEventListener('click',async (event)=>{

                if(checkName.value === ''){
                    alert('로그인을 해주세요!');
                    return;
                }else{
                    if(document.querySelector('.reply_true') != null){
                        return;
                    }
                }
                console.log(event);
                const $parentNode = document.querySelector(`.parent${event.target.offsetParent.id}`);

                $parentNode.insertAdjacentHTML('afterend',
                    `
                <div class="reply_true children${event.target.offsetParent.id}">
                    <li class="children">
                        <div class="desc-top">
                             <h6>${checkName.value}</h6>
                             <span class="date"></span>
                        </div>
                        <textarea class="form-control" id="comment-reply${event.target.offsetParent.id}"></textarea>
                    </li>
                    <div class="align-right mt-10">
                        <span class="button">
                            <button class="btn reply" data-id="${event.target.offsetParent.id}">확인</button>
                        </span>
                        <span class="button">
                            <button class="btn reply_cancel" data-id="${event.target.offsetParent.id}">취소</button>
                        </span>
                    </div> 
                </div> `);
                console.log("코멘트 아이디",event.target.offsetParent.id);

                const reply = document.querySelectorAll('.reply');
                for(const button of reply){
                    button.addEventListener('click', async (event) =>{
                        const text = document.getElementById(`comment-reply${event.target.dataset.id}`);
                        const res = await fetch(`/api/user/comment/answer/write/${boardID}/${event.target.dataset.id}`,{
                            method:'POST',
                            headers: {
                                'header': header,
                                'X-Requested-With': 'XMLHttpRequest',
                                "Content-Type": "application/json",
                                'X-CSRF-Token': token
                            },
                            body: JSON.stringify({cmContent: text.value})
                        })
                        if(res.ok){
                            alert('답글 작성 완료!');
                            await reload(0);
                        }
                    })
                }

                const reply_cancel = document.querySelectorAll('.reply_cancel');
                for(const button of reply_cancel){
                    button.addEventListener('click', (event) =>{
                        document.querySelector(`.children${event.target.dataset.id}`).remove();
                    })
                }
            })
        }

        // ------------------------------------------대 댓글 --------------------------------
        //수정버튼
        const replyEdit = document.querySelectorAll(".edit-link.reply-edit");
        for(const replyBtn of replyEdit){
            replyBtn.addEventListener('click',async (event)=>{
                const val = event.target.offsetParent.id;
                const parentId = event.target.dataset.id;
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
                document.querySelector(`.btn.completeEdit${val}`).addEventListener('click',async (event)=>{
                    //댓글 수정 버튼 클릭 시
                    const res = await fetch(`/api/user/comment/answer/edit/${boardID}/${parentId}/${val}`,{
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
                        await reload(e);
                    }
                })
                document.querySelector(`.btn.completeCancel${val}`).addEventListener('click',async (event)=>{
                    const res = await fetch(`/api/board/comment/check/${val}`)
                    const data = await res.json();
                    if(res.status === 200){
                        await reload(e);
                    }
                })
            })
        }

        //삭제 버튼
        const replyDelete = document.querySelectorAll(".delete-link.reply-delete");
        for(const replyDelBtn of replyDelete){
            replyDelBtn.addEventListener('click',async (event)=>{
                console.log(event.target.offsetParent.id);
                console.log(event.target.dataset.id);
                if(confirm("삭제하시겠습니까?")){
                    const val = event.target.offsetParent.id;
                    const parentId = event.target.dataset.id;
                    const res = await fetch(`/api/user/comment/answer/delete/${boardID}/${parentId}/${val}`,{
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
                        await reload(e);
                    }
                }
            })
        }

        renderPagination(comments[0].totalPage, comments[0].totalElement, reload)
}


reload();


