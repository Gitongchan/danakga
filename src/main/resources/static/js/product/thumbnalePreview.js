function getImageFile(e) {
    const uploadFiles = [];
    const files = e.currentTarget.files;
    const imagePreview = document.querySelector('#thumb-preview');
    const docFrag = new DocumentFragment();


    console.log(imagePreview);
    console.log(e);
    console.log(files);
    while(imagePreview.hasChildNodes()){
        imagePreview.removeChild(imagePreview.firstChild);
    }

    if ([...files].length >= 7) {
        alert('이미지는 최대 6개 까지 업로드가 가능합니다.');
        return;
    }

    // 파일 타입 검사
    [...files].forEach(file => {
        if (!file.type.match("image/.*")) {
            alert('이미지 파일만 업로드가 가능합니다.');
            return
        }

        // 파일 갯수 검사
        if ([...files].length < 7) {
            uploadFiles.push(file);
            const reader = new FileReader();
            reader.onload = (e) => {
                const preview = createElement(e, file);
                imagePreview.appendChild(preview);
            };
            reader.readAsDataURL(file);
        }
    });
}

function createElement(e, file) {
    const li = document.createElement('li');
    const img = document.createElement('img');
    const button = document.createElement('button');
    const divImg = document.createElement('div');

    divImg.classList.add("current-img");
    divImg.id = file.lastModified;
    button.classList.add('img-btn-delete','btn-danger');
    button.id = 'file-remove';
    button.innerText = 'X';
    button.setAttribute('data-index', file.lastModified);

    img.setAttribute('src', e.target.result);
    img.setAttribute('data-file', file.name);
    img.classList.add('img-item');

    button.addEventListener('click',(e)=>{
        console.log(e);
        if(e.target.id !== 'file-remove') return;
        const removeTargetId = e.target.dataset.index;
        const removeTarget = document.getElementById(removeTargetId);
        const files = document.querySelector('#thumb-file').files;
        const dataTranster = new DataTransfer();

        Array.from(files)
            .filter(file => file.lastModified != removeTargetId)
            .forEach(file => {
                dataTranster.items.add(file);
            });

        document.querySelector('#thumb-file').files = dataTranster.files;

        removeTarget.remove();
    })

    divImg.appendChild(img);
    divImg.appendChild(button);

    return divImg;
}


const thandler = {
    init() {
        const fileInput = document.querySelector('#thumb-file');
        fileInput.addEventListener('change', getImageFile)
    }
}

thandler.init()