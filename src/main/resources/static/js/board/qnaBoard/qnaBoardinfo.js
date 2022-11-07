
const title = document.getElementById('post-title');
const content = document.getElementById('post-inner');
const id = document.getElementById('userID');
const span = document.createElement('span');
const btnWrap = document.createElement('li');
const editBtn = document.createElement('button');
const deleteBtn = document.createElement('button');
const metaInfo = document.querySelector('.meta-info')

editBtn.innerText = "수정하기";
deleteBtn.innerText = "삭제하기";
btnWrap.appendChild(editBtn);
btnWrap.appendChild(deleteBtn);

const qnId = getParameterByName('qnId').split('?')[0];
const userId = getParameterByName('userId');

(async function() {
    try {
        const res = await fetch('/api/user')
        const data = await res.json();

        if(res.status === 200){
            if(data.userid === userId){
                fetch(`/api/qna/post/${qnId}`)
                    .then((res)=>res.json())
                    .then((data)=> {
                        console.log(data);
                    metaInfo.appendChild(btnWrap);
                        content.innerHTML += data.qnaList[0].qn_content;
                        title.innerText = data.qnaList[0].qn_title;
                        span.innerText= data.qnaList[0].qn_userid;
                        id.appendChild(span);
                    });
            }
            else{
                check();
            }
        }
    }
    catch (e) {
        check();
    }
})();

editBtn.addEventListener('click',()=>{
    location.replace(`/qnaboard/edit?qnId=${qnId}&userid=${userId}`)
})

deleteBtn.addEventListener('click',()=>{
    if(confirm('게시글을 삭제하시겠습니까?')) {
        fetch(`/api/user/qna/site_delete/${qnId}`,{
            method : "PUT",
            headers: {
                'header': header,
                'X-Requested-With': 'XMLHttpRequest',
                "Content-Type": "application/json",
                'X-CSRF-Token': token
            }
        })
            .then((res)=>res.json())
            .then((data)=>{
                location.replace('/board/qa?type=문의게시판');
            })
    }
})

const check = () => {
    fetch(`/api/qna/post/${qnId}`)
        .then((res)=>res.json())
        .then((data)=> {
            content.innerHTML += data.qnaList[0].qn_content;
            title.innerText = data.qnaList[0].qn_title;
            span.innerText= data.qnaList[0].qn_userid;
            id.appendChild(span);
        });
}