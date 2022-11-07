const title = document.getElementById('post-title');
const content = document.getElementById('post-inner');
const content_img = document.getElementById('post-img');
const userid = document.getElementById('userID');
const span = document.createElement('span');

const qnId = getParameterByName('qnId').split('?')[0];
const userId = getParameterByName('userid');

let editor;

ClassicEditor.create(document.querySelector('#editor'), {
    toolbar: ['bold', 'italic', 'link', 'undo', 'redo', 'numberedList', 'bulletedList']
})
    .then(newEditor => {
        editor = newEditor;
    })
    .catch(error => {
        console.error(error);
    });

// Assuming there is a <button id="submit">Submit</button> in your application.
document.querySelector('#submit').addEventListener('click', (e) => {
    e.preventDefault();
    //텍스트 에디터에서 값 가져옴
    const editorData = editor.getData();
    const qnaTitle = document.getElementById('post-title').value;

    if(qnaTitle.trim().length <= 0 ){
        alert("제목을 입력해주세요.")
        return
    }

    if(editorData.trim().length <= 0){
        alert("내용을 입력해주세요!")
        return
    }

    const boardData = {
        qnaType: document.getElementById('type').value,
        qnaTitle: qnaTitle,
        qnaContent: editorData
    }



    fetch(`/api/user/qna/site_edit/${qnId}`, {
        method: "PUT",
        headers: {
            'header': header,
            "Content-Type": "application/json",
            'X-CSRF-Token': token
        },
        body: JSON.stringify(boardData)
    })
        .then(res => {
            if (res.status === 200 || res.status === 201) { // 성공을 알리는 HTTP 상태 코드면
                alert("수정완료");
            } else { // 실패를 알리는 HTTP 상태 코드면
                console.error(res.statusText);
                console.error(res);
            }
        })
        .then(data => {
            location.replace(`/qnaboard/info?qnId=${qnId}&userid=${userId}`);
        })
        .catch(error => console.log(error))
    // ...
});

// 수정할 게시판의 정보를 먼저 뿌려주는 곳
fetch(`/api/qna/post/${qnId}`)
    .then((res)=>res.json())
    .then((data)=> {
        console.log(data);
        editor.setData(data.qnaList[0].qn_content);
        title.value = data.qnaList[0].qn_title;
        span.innerText= data.qnaList[0].qn_userid;
        userid.appendChild(span);

    });