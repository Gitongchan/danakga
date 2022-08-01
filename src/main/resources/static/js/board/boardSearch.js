function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}
const searchBtn = document.querySelector('#searchbtn');
const content = document.querySelector('#search');
const category = document.querySelector('.form-select');
const type = getParameterByName('type');

const getInfo = async () => {
    // 페이징 부분을 비워주는 곳
    pagenation.innerHTML= '';

    console.log(category.value);
    console.log(type.value);
    console.log(content.value);
    if(content.value.trim().length===0){
        alert('값을 입력해주세요!')
        return;
    }
    try{
        const res = await  fetch(`/api/board/list/search/${type}/${category.value}/${content.value}?page=0`);
        if(res.ok){
            const data = await res.json();
            tablelist.innerHTML = '';
            console.log(data);
            for(let i=0; i<data.lists.length; i++){
                const tr = document.createElement('tr');
                tr.innerHTML =
                    `<td>${data.lists[i].bd_id}</td>
                 <td><a href="/board/info?boardid=${data.lists[i].bd_id}?bdwriter=${data.lists[i].bd_writer}">${data.lists[i].bd_title}</a></td>
                 <td>${data.lists[i].bd_writer}</td>
                 <td>${data.lists[i].bd_created.split('.')[0]}</td>
                 <td>${data.lists[i].bd_views}</td>`

                tablelist.appendChild(tr);
            }
            //  페이징을 해주면 된다.
            console.log(searchPagination())
            searchPagination(data.lists[0].totalPage,data.lists[0].totalElement);
        }
    }catch (e) {

    }
}

searchBtn.addEventListener('click', getInfo);
