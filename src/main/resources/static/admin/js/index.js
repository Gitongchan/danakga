//토근값 가져오기
const token = document.querySelector('meta[name="_csrf"]').content;
const header = document.querySelector('meta[name="_csrf_header"]').content;

const menuId = document.querySelector('#menuId');
const $searchInput = document.querySelector('#search-input');

// nav에 공통검색기능
function searchText(e){
    const txt = $searchInput.value;
    const code = e.code;

    if(code === 'Enter'){
        if(menuId.value === '0'){
            userList($selectEnabled.value, $selectSearchRequirements.value, $searchInput.value ? $searchInput.value : '%25', $selectSortMethod.value, $selectSortBy.value,0)
        }else if( menuId.value === '1'){
            console.log('실행함');
            manager($selectEnabled.value, $selectSearchRequirements.value, $searchInput.value ? $searchInput.value : '%25', $selectSortMethod.value, $selectSortBy.value,0)
        }

        $searchInput.value = '';
    }
}
// 공통적인 페이징 부분