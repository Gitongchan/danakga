//내가 작성한 게시글
const myBoardBtn = document.getElementById('my_board_search');
//내가 댓글을 남긴 게시글
const myCommentBoardBtn = document.getElementById('my_commentBoard_search');
//내 댓글
const myCommentBtn = document.getElementById('my_comment_search');

const cateValue = document.getElementById('category_value');
const freeBtn = document.getElementById('free');

const qnaBtn = document.getElementById('qna');

myBoardBtn.addEventListener('click', async () => {
    cateValue.value = "myboard"
})

myCommentBoardBtn.addEventListener('click',  () => {
    cateValue.value = "commentBoard"
})

myCommentBtn.addEventListener('click',  () => {
    cateValue.value = "comment"
})

freeBtn.addEventListener('click', async ()=>{
    if(cateValue.value === 'myboard'){
        try{
            console.log(cateValue.value);
            // 내가 작성한 게시글 찾기 이벤트
            const res = await fetch(`/api/user/myPostList/자유게시판?page=0`);
        }catch (e) {

        }
    } else if(cateValue.value === 'commentBoard'){
        // 내가 댓글을 남긴 게시글 찾기 이벤트
        try{
            console.log(cateValue.value);

            const res = await fetch(`/api/user/myCommentsPost/자유게시판?page=0`);
        }catch (e) {

        }
    } else if(cateValue.value === 'comment'){
        // 내가 작성한 댓글 찾기 이벤트
        try{
            console.log(cateValue.value);
            const res = await fetch(`/api/user/myCommentsList/자유게시판?page=0`);
        }catch (e) {

        }
    }
})

qnaBtn.addEventListener('click',async ()=>{
    if(cateValue.value === 'myboard'){
        try{
            console.log(cateValue.value);
            // 내가 작성한 게시글 찾기 이벤트
            const res = await fetch(`/api/user/myPostList/문의게시판?page=0`);
        }catch (e) {

        }
    } else if(cateValue.value === 'commentBoard'){
        // 내가 댓글을 남긴 게시글 찾기 이벤트
        try{
            console.log(cateValue.value);

            const res = await fetch(`/api/user/myCommentsPost/문의게시판?page=0`);
        }catch (e) {

        }
    } else if(cateValue.value === 'comment'){
        // 내가 작성한 댓글 찾기 이벤트
        try{
            console.log(cateValue.value);
            const res = await fetch(`/api/user/myCommentsList/문의게시판?page=0`);
        }catch (e) {

        }
    }
})

