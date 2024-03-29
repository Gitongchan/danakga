const title = document.getElementById('post-title');
const content = document.getElementById('post-inner');
const content_img = document.getElementById('post-img');
const userid = document.getElementById('userID');
const span = document.createElement('span');
const delFiles = {
    deletedFiles  : []
};

const urlParams = getParameterByName('boardid').split('?')[0];
const urlWriter = getParameterByName('bdwriter');


fetch(`/api/board/post/${urlParams}`)
    .then((res)=>res.json())
    .then((data)=> {
        console.log(data.post[0]);
        console.log(data.post[0].bd_content);
        for(let i =0; i< data.post[0].files.length; i++){
            const img = document.createElement('img');
            const divImg = document.createElement('div');
            const deleteButton = document.createElement('button');
            divImg.classList.add(`current-img`);
            divImg.id= `${data.post[0].files[i].file_name}`
            deleteButton.classList.add('img-btn-delete','btn-danger');
            deleteButton.innerText = 'X';
            img.classList.add(`img-item`);
            img.src= data.post[0].files[i].file_path;

            deleteButton.addEventListener('click',(e)=>{
                console.log(e);
                delFiles.deletedFiles.push({"value":e.target.offsetParent.id});
                e.target.parentElement.remove();
            })

            divImg.appendChild(img);
            divImg.appendChild(deleteButton);
            content_img.appendChild(divImg);
        }


        editor.setData(data.post[0].bd_content);
        title.value = data.post[0].bd_title;
        span.innerText= data.post[0].bd_writer;
        userid.appendChild(span);

    });