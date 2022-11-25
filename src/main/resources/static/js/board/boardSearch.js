const searchBtn = document.querySelector('#searchbtn');
const content = document.querySelector('#search');
const category = document.querySelector('.form-select');
const type = getParameterByName('type');

const getInfo = (page = 0) => {
    return async () => {
        if(content.value.trim().length===0){
            alert('값을 입력해주세요!')
            return;
        }
        try{
            const res = await  fetch(`/api/board/list/search/${type}/${category.value}/${content.value}?page=${page}`);
            if(!res.ok){
                alert("오류발생!");
                return
            }

            const { lists } = await res.json();
            tablelist.innerHTML = '';

            for(let item in lists){
                const tr = document.createElement('tr');
                tr.innerHTML =
                    `<td>${lists[item].bd_id}</td>
                 <td><a href="/board/info?boardid=${lists[item].bd_id}?bdwriter=${lists[item].bd_writer}">${lists[item].bd_title}</a></td>
                 <td>${lists[item].bd_writer}</td>
                 <td>${lists[item].bd_created.split('.')[0]}</td>
                 <td>${lists[item].bd_views}</td>`

                tablelist.appendChild(tr);
            }

            renderPagination(lists[0].totalPage,lists[0].totalElement, getInfo());
        }catch (e) {

        }
    }
}

searchBtn.addEventListener('click', getInfo());
