//내가 작성한 게시글
const myBoardBtn = document.getElementById('my_board_search');
//내가 댓글을 남긴 게시글
const myCommentBoardBtn = document.getElementById('my_commentBoard_search');
//내 댓글
const myCommentBtn = document.getElementById('my_comment_search');

const cateValue = document.getElementById('category_value');
const freeBtn = document.getElementById('free');

const qnaBtn = document.getElementById('qna');

//table에 값넣기
const tableList = document.getElementById("myBoardTableList");

const li_board = document.getElementById("li_board-search");
const li_commentBoard = document.getElementById("li-comment-board-search");
const li_comment = document.getElementById("li-comment-search");
const li_free = document.getElementById('free');
const li_qna = document.getElementById('qna');

myBoardBtn.addEventListener('click', (event) => {
    tableList.innerHTML = '';
    cateValue.value = "myboard"
    pagenation.innerHTML='';
    li_qna.classList.remove('active');
    li_free.classList.remove('active');
    li_commentBoard.classList.remove('select');
    li_comment.classList.remove('select');
    event.target.parentElement.classList.add("select");
})

myCommentBoardBtn.addEventListener('click',  (event) => {
    tableList.innerHTML = '';
    cateValue.value = "commentBoard"
    pagenation.innerHTML='';
    li_qna.classList.remove('active');
    li_free.classList.remove('active');
    li_board.classList.remove('select');
    li_comment.classList.remove('select');
    event.target.parentElement.classList.add("select");

})

myCommentBtn.addEventListener('click',  (event) => {
    tableList.innerHTML = '';
    cateValue.value = "comment"
    pagenation.innerHTML='';
    li_qna.classList.remove('active');
    li_free.classList.remove('active');
    li_board.classList.remove("select");
    li_commentBoard.classList.remove("select");
    event.target.parentElement.classList.add("select");

})

freeBtn.addEventListener('click', async ()=>{
    li_qna.classList.remove('active');
    li_free.classList.add('active');
    if(cateValue.value === 'myboard'){
        try{
            console.log(cateValue.value);
            // 내가 작성한 게시글 찾기 이벤트
            const res = await fetch(`/api/user/myPostList/자유게시판?page=0`);

            if(res.ok){
                const postList = await res.json();
                tableList.innerHTML = '';

                for(let i=0; i<postList.lists.length; i++){
                    const tr = document.createElement('tr');
                    tr.innerHTML =
                        `<td>${postList.lists[i].bd_id}</td>
                 <td><a href="/board/info?boardid=${postList.lists[i].bd_id}?bdwriter=${postList.lists[i].bd_writer}">${postList.lists[i].bd_title}</a></td>
                 <td>${postList.lists[i].bd_writer}</td>
                 <td>${postList.lists[i].bd_created.split('.')[0]}</td>
                 <td>${postList.lists[i].bd_views}</td>`

                    tableList.appendChild(tr);
                }
                freeRender('/api/user/myPostList/자유게시판?');
            }
        }catch (e) {

        }
    } else if(cateValue.value === 'commentBoard'){
        // 내가 댓글을 남긴 게시글 찾기 이벤트
        try{
            console.log(cateValue.value);

            const res = await fetch(`/api/user/myCommentsPost/자유게시판?page=0`);
            if(res.ok){
                const postList = await res.json();
                tableList.innerHTML = '';

                for(let i=0; i<postList.lists.length; i++){
                    const tr = document.createElement('tr');
                    tr.innerHTML =
                        `<td>${postList.lists[i].bd_id}</td>
                 <td><a href="/board/info?boardid=${postList.lists[i].bd_id}?bdwriter=${postList.lists[i].bd_writer}">${postList.lists[i].bd_title}</a></td>
                 <td>${postList.lists[i].bd_writer}</td>
                 <td>${postList.lists[i].bd_created.split('.')[0]}</td>
                 <td>${postList.lists[i].bd_views}</td>`

                    tableList.appendChild(tr);
                }
                freeRender('/api/user/myCommentsPost/자유게시판?');
            }
        }catch (e) {

        }
    } else if(cateValue.value === 'comment'){
        // 내가 작성한 댓글 찾기 이벤트
        try{
            console.log(cateValue.value);
            const res = await fetch(`/api/user/myCommentsList/자유게시판?page=0`);

            if(res.ok){
                const commentList = await res.json();
                tableList.innerHTML = '';

                for(let i=0; i<commentList.comments.length; i++){
                    const tr = document.createElement('tr');
                    tr.innerHTML =
                        `<td>${commentList.comments[i].bd_id}</td>
                 <td><a href="/board/info?boardid=${commentList.comments[i].bd_id}">${commentList.comments[i].cm_content}</a></td>
                 <td>${commentList.comments[i].cm_writer}</td>
                 <td>${commentList.comments[i].cm_created.split('.')[0]}</td>
                 <td>${commentList.comments[i].cm_modify}</td>`

                    tableList.appendChild(tr);
                }
                qnaRender('/api/user/myCommentsList/자유게시판?');
            }
        }catch (e) {

        }
    }
})

qnaBtn.addEventListener('click',async ()=>{
    li_free.classList.remove('active');
    li_qna.classList.add('active');

    if(cateValue.value === 'myboard'){
        try{
            console.log(cateValue.value);
            // 내가 작성한 게시글 찾기 이벤트
            const res = await fetch(`/api/user/myPostList/문의게시판?page=0`);

            if(res.ok){
                const postList = await res.json();
                tableList.innerHTML = '';

                for(let i=0; i<postList.lists.length; i++){
                    const tr = document.createElement('tr');
                    tr.innerHTML =
                        `<td>${postList.lists[i].bd_id}</td>
                 <td><a href="/board/info?boardid=${postList.lists[i].bd_id}?bdwriter=${postList.lists[i].bd_writer}">${postList.lists[i].bd_title}</a></td>
                 <td>${postList.lists[i].bd_writer}</td>
                 <td>${postList.lists[i].bd_created.split('.')[0]}</td>
                 <td>${postList.lists[i].bd_views}</td>`

                    tableList.appendChild(tr);
                }
                freeRender('/api/user/myPostList/문의게시판?');
            }
        }catch (e) {

        }
    } else if(cateValue.value === 'commentBoard'){
        // 내가 댓글을 남긴 게시글 찾기 이벤트
        try{
            console.log(cateValue.value);

            const res = await fetch(`/api/user/myCommentsPost/문의게시판?page=0`);
            if(res.ok){
                const postList = await res.json();
                tableList.innerHTML = '';

                for(let i=0; i<postList.lists.length; i++){
                    const tr = document.createElement('tr');
                    tr.innerHTML =
                        `<td>${postList.lists[i].bd_id}</td>
                 <td><a href="/board/info?boardid=${postList.lists[i].bd_id}?bdwriter=${postList.lists[i].bd_writer}">${postList.lists[i].bd_title}</a></td>
                 <td>${postList.lists[i].bd_writer}</td>
                 <td>${postList.lists[i].bd_created.split('.')[0]}</td>
                 <td>${postList.lists[i].bd_views}</td>`

                    tableList.appendChild(tr);
                }
                freeRender('/api/user/myCommentsPost/문의게시판?');
            }
        }catch (e) {

        }
    } else if(cateValue.value === 'comment'){
        // 내가 작성한 댓글 찾기 이벤트
        try{
            console.log(cateValue.value);
            const res = await fetch(`/api/user/myCommentsList/문의게시판?page=0`);

            if(res.ok){
                const commentList = await res.json();
                tableList.innerHTML = '';

                for(let i=0; i<commentList.comments.length; i++){
                    const tr = document.createElement('tr');
                    tr.innerHTML =
                        `<td>${commentList.comments[i].bd_id}</td>
                 <td><a href="/board/info?boardid=${commentList.comments[i].bd_id}">${commentList.comments[i].cm_content}</a></td>
                 <td>${commentList.comments[i].cm_writer}</td>
                 <td>${commentList.comments[i].cm_created.split('.')[0]}</td>
                 <td>${commentList.comments[i].cm_modify}</td>`

                    tableList.appendChild(tr);
                }
                qnaRender('/api/user/myCommentsList/문의게시판?')
            }
        }catch (e) {

        }
    }
})
