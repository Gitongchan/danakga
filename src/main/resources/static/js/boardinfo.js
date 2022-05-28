const title = document.getElementById('post-title');
const content = document.getElementById('post-inner');
const userid = document.getElementById('userID');
const span = document.createElement('span');
fetch(`/api/board/post/3`)
.then((res)=>res.json())
.then((data)=>
{
    title.innerText = data.bdTitle;
    content.innerHTML = data.bdContent;
    span.innerText= data.bdWriter;
    userid.appendChild(span);

});