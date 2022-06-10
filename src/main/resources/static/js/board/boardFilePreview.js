// function getImageFiles(e) {
//     const uploadFiles = [];
//     const files = e.currentTarget.files;
//     const imagePreview = document.querySelector('.image-preview');
//     const docFrag = new DocumentFragment();
//
//     if ([...files].length >= 7) {
//         alert('이미지는 최대 6개 까지 업로드가 가능합니다.');
//         return;
//     }
//
//     // 파일 타입 검사
//     [...files].forEach(file => {
//         if (!file.type.match("image/.*")) {
//             alert('이미지 파일만 업로드가 가능합니다.');
//             return
//         }
//
//         // 파일 갯수 검사
//         if ([...files].length < 7) {
//             uploadFiles.push(file);
//             const reader = new FileReader();
//             reader.onload = (e) => {
//                 const preview = createElement(e, file);
//                 imagePreview.appendChild(preview);
//             };
//             reader.readAsDataURL(file);
//         }
//     });
// }
//
// function createElement(e, file) {
//     const li = document.createElement('li');
//     const img = document.createElement('img');
//     img.setAttribute('src', e.target.result);
//     img.setAttribute('data-file', file.name);
//     li.appendChild(img);
//
//     return li;
// }
//
// //네모 상자 클릭 이벤트
// const upload = document.querySelector('.upload');
// //실제 파일 인풋 폼
// const realUpload = document.querySelector('.real-upload');
//
// upload.addEventListener('click', () => {
//     realUpload.click();
//     console.log(realUpload);
// });
// realUpload.addEventListener('change', getImageFiles);


const handler = {
    init() {
        const fileInput = document.querySelector('#file-input');
        const preview = document.querySelector('#preview');
        fileInput.addEventListener('change', () => {
            console.dir(fileInput)
            const files = Array.from(fileInput.files)
            files.forEach(file => {
                preview.innerHTML += `
                        <p id="${file.lastModified}">
                            ${file.name}
                            <button data-index='${file.lastModified}' class='file-remove'>X</button>
                        </p>`;
            });
        });
    },

    removeFile: () => {
        document.addEventListener('click', (e) => {
            if(e.target.className !== 'file-remove') return;
            const removeTargetId = e.target.dataset.index;
            const removeTarget = document.getElementById(removeTargetId);
            const files = document.querySelector('#file-input').files;
            const dataTranster = new DataTransfer();

            // document.querySelector('#file-input').files =
            //             Array.from(files).filter(file => file.lastModified !== removeTarget);


            Array.from(files)
                .filter(file => file.lastModified != removeTargetId)
                .forEach(file => {
                    dataTranster.items.add(file);
                });

            document.querySelector('#file-input').files = dataTranster.files;

            removeTarget.remove();
        })
    }
}

handler.init()
handler.removeFile()