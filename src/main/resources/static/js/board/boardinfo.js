
const title = document.getElementById('post-title');
const content = document.getElementById('post-inner');
const content_img = document.getElementById('post-img');
const userid = document.getElementById('userID');
const span = document.createElement('span');
const btnWrap = document.createElement('div');
const editBtn = document.createElement('button');
const deleteBtn = document.createElement('button');

editBtn.innerText = "수정하기";
deleteBtn.innerText = "삭제하기";
btnWrap.appendChild(editBtn);
btnWrap.appendChild(deleteBtn);

const boardID = getParameterByName('boardid').split('?')[0];
const urlWriter = getParameterByName('bdwriter');
const boardType = getParameterByName('bdType');

(async function() {
    try {
        const res = await fetch('/api/user')
        const data = await res.json();

        if(res.status === 200){
            if(data.userid === urlWriter){
                fetch(`/api/board/post/${boardID}`)
                    .then((res)=>res.json())
                    .then((data)=>
                {
                    for(let i =0; i< data.post[0].files.length; i++){
                            const img = document.createElement('img');
                            const divImg = document.createElement('div');
                            const deleteButton = document.createElement('button');
                            divImg.classList.add(`current-img`);
                            img.classList.add(`img-item`);
                            img.src= data[0].files[i].file_path;
                            divImg.appendChild(img);
                            content_img.appendChild(divImg);
                        }

                        userid.after(btnWrap);
                        content.innerHTML += data.post[0].bd_content;
                        title.innerText = data.post[0].bd_title;
                        span.innerText= data.post[0].bd_writer;
                        userid.appendChild(span);
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
    location.replace(`/board/edit?boardid=${boardID}&bdwriter=${urlWriter}`)
})

deleteBtn.addEventListener('click',()=>{
    // api에 요청을 보낼 때 header에 _csrf토큰값을 가져와서 넘김
    const header = document.querySelector('meta[name="_csrf_header"]').content;
    const token = document.querySelector('meta[name="_csrf"]').content;

    if(confirm('게시글을 삭제하시겠습니까?')) {
        fetch(`/api/user/post/delete/${boardID}`,{
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
                if(boardType == 'free'){
                    location.replace('/board/basic');
                }else{
                    location.replace('/board/qa');
                }
            })
    }
})

const check = () => {
    fetch(`/api/board/post/${boardID}`)
        .then((res)=>res.json())
        .then((data)=>
        {
            for(let i =0; i< data.post[0].files.length; i++){
                const img = document.createElement('img');
                const divImg = document.createElement('div');
                const deleteButton = document.createElement('button');
                divImg.classList.add(`current-img`);
                img.classList.add(`img-item`);
                img.src= data.post[0].files[i].file_path;
                divImg.appendChild(img);
                content_img.appendChild(divImg);
            }
            content.innerHTML += data.post[0].bd_content;
            title.innerText = data.post[0].bd_title;
            span.innerText= data.post[0].bd_writer;
            userid.appendChild(span);
        });
}