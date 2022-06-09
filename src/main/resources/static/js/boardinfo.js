const title = document.getElementById('post-title');
const content = document.getElementById('post-inner');
const userid = document.getElementById('userID');
const span = document.createElement('span');
function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}
const urlParams = getParameterByName('boardid');

fetch(`/api/board/post/${urlParams}`)
.then((res)=>res.json())
.then((data)=>
{

    for(let i =0; i< data.files.length; i++){
        let img = document.createElement('img');
        img.src= `/images/${data.files[i].file_name}`;
        console.log(data.files[i].file_path);
        content.appendChild(img);
    }

    content.innerHTML += data.bdContent;
    title.innerText = data.bdTitle;
    span.innerText= data.bdWriter;
    userid.appendChild(span);

});