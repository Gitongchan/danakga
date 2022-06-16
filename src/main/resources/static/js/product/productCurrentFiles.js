const content_Thumimg = document.getElementById('thumb-preview');
const content_img = document.getElementById('current-image-preview');

const userid = document.getElementById('userID');
const span = document.createElement('span');
//썸네일 관련 태그들
const thumImg = document.createElement('img');
const thumDivImg = document.createElement('div');
const thumDeleteButton = document.createElement('button');

const delThumbFile = {
    deletedThumFile  : []
};

const delFiles = {
    deletedFileList  : []
};

fetch(`/api/product/item/${urlID}`)
    .then((res)=>res.json())
    .then((data)=>
    {
        //썸네일
        thumDivImg.classList.add(`current-img`);
        thumDivImg.id= `${data.productPhoto.substr(21)}`
        thumDeleteButton.classList.add('img-btn-delete','btn-danger');
        thumDeleteButton.innerText = 'X';
        thumImg.classList.add(`img-item`);
        thumImg.src= data.productPhoto;

        thumDeleteButton.addEventListener('click',(e)=>{
            console.log(e);
            delThumbFile.deletedThumFile.push({"value":e.target.offsetParent.id});
            e.target.parentElement.remove();
        })

        thumDivImg.appendChild(thumImg);
        thumDivImg.appendChild(thumDeleteButton);
        content_Thumimg.appendChild(thumDivImg);

        //다른 파일들
        for(let i =0; i< data.files.length; i++){
            const img = document.createElement('img');
            const divImg = document.createElement('div');
            const deleteButton = document.createElement('button');
            divImg.classList.add(`current-img`);
            divImg.id= `${data.files[i].file_name}`
            deleteButton.classList.add('img-btn-delete','btn-danger');
            deleteButton.innerText = 'X';
            img.classList.add(`img-item`);
            img.src= data.files[i].file_path;

            deleteButton.addEventListener('click',(e)=>{
                delFiles.deletedFileList.push({"value":e.target.offsetParent.id});
                e.target.parentElement.remove();
            })

            divImg.appendChild(img);
            divImg.appendChild(deleteButton);
            content_img.appendChild(divImg);
        }

    });
