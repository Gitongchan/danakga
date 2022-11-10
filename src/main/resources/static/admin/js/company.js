// 회원관리 기능 넣는 곳

// 사용자 ,사업자 목록 조회(userEnabled) * true는 전체 조회 , false는 탈퇴자만 조회
// 사업자 , 검색조건(searchRequirements) : userName,userId,companyName,companyNum(회사 전화번호)

// searchWord = 검색어
// sortMethod = 오름차순(asc), 내림차순(desc)
// sortBy = name

menuId.value = '1';
const $table = document.querySelector('tbody');
const $selectSearchRequirements = document.querySelector('#selectSearchRequirements');
const $selectEnabled = document.querySelector('#selectEnabled');
const $selectSortBy = document.querySelector('#selectSortBy');
const $selectSortMethod = document.querySelector('#selectSortMethod');


// 버튼기능
const companyActions = {
    info: async (id) => {
        window.open(`user-info?id=${id}`,"유저 정보", "width=600px, height=500, scrollbars=no, resizable=no, toolbars=no, menubar=no")
    },
    stop: async (id) => {
        if(confirm("정지하시겠습니까?")){
            const res = await fetch(`/admin/members/user/${id}`,{
                method: "PUT",
                headers: {
                    'header': header,
                    'X-CSRF-Token': token,
                    "Content-Type": "application/json"
                },
            });

            if(!res.ok){
                alert("정지 실패!")
                return null;
            }

            const data = await res.json();
            alert(data.message);
            userList($selectEnabled.value, $selectSearchRequirements.value, $searchInput.value ? $searchInput.value : '%25', $selectSortMethod.value, $selectSortBy.value,0)
        }
    },
    reuse: (id) => alert('정지풀기', id),
    delete: async (id) => {
        if(confirm("삭제하시겠습니까?")){
            const res = await fetch(`/admin/members/user/${id}`,{
                method: "DELETE",
                headers: {
                    'header': header,
                    'X-CSRF-Token': token,
                    "Content-Type": "application/json"
                },
            });

            if(!res.ok){
                alert("삭제 실패!")
                return null;
            }

            const data = await res.json();
            alert(data.message);
            userList($selectEnabled.value, $selectSearchRequirements.value, $searchInput.value ? $searchInput.value : '%25', $selectSortMethod.value, $selectSortBy.value,0)
        }
    },
}


// 일반 회원
async function manager(companyEnabled, userEnabled, searchRequirements, searchWord, sortMethod, sortBy, page){
    const res = await fetch(`/admin/members/managerList?companyEnabled=${companyEnabled}&userEnabled=${userEnabled}&searchRequirements=${searchRequirements}&searchWord=${searchWord}&sortMethod=${sortMethod}&sortBy=${sortBy}&page=${page}`);

    if(!res.ok){
        alert("조회에 실패하였습니다!")
        return 0;
    }

    $table.innerHTML = '';
    const data = await res.json();

    for(const item of data){
        if(item.companyEnabled){
            $table.innerHTML+= `
                      <tr>
                        <td><i class="fab fa-angular fa-lg text-danger me-3"></i> <strong>${item.userid}</strong></td>
                        <td>${item.email}</td>
                        <td>
                            ${item.name}
                        </td>
                        <td>${item.phone}</td>
                        <td><span class="badge bg-label-primary me-1">사용회원</span></td>
                        <td>
                                <button class="btn user-info" data-action="info" data-id="${item.userid}"><i class="bx bx-user me-1"></i> 상세정보</button>
                        </td>
                        <td>
                                <button class="btn user-stop" data-action="stop" data-id="${item.userid}"><i class="bx bx-stop me-1"></i> 정지</button>
                        </td>
                        <td>
                                
                        </td>
                      </tr>
        `
        }else{
            $table.innerHTML+= `
                      <tr>
                        <td><i class="fab fa-angular fa-lg text-danger me-3"></i> <strong>${item.userid}</strong></td>
                        <td>${item.email}</td>
                        <td>
                            ${item.name}
                        </td>
                        <td>${item.phone}</td>
                        <td><span class="badge bg-label-danger me-1">정지회원</span></td>
                        <td>
                                <button class="btn user-info" data-action="info" data-id="${item.userid}"><i class="bx bx-user me-1"></i> 상세정보</button>
                        </td>
                        <td>
                                <button class="btn user-re-use" data-action="reuse" data-id="${item.userid}"><i class="bx bx-recycle me-1"></i> 해제</button>
                        </td>
                        <td>
                                <button class="btn user-delete" data-action="delete" data-id="${item.userid}"><i class="bx bx-trash me-1"></i> 삭제</button>
                        </td>
                      </tr>
        `
        }
    }

    document.querySelector('tbody').addEventListener('click', e => {
        const action = e.target.dataset.action
        const id = e.target.dataset.id;
        if (action) {
            companyActions[action](id);
        }
    });

    console.log(data);
}

function Alert(event){
    console.log(event.target);
}

// 탈퇴여부
$selectEnabled.addEventListener('change', () => {
    manager($selectEnabled.value,true, $selectSearchRequirements.value, $searchInput.value ? $searchInput.value : '%25', $selectSortMethod.value, $selectSortBy.value,0)
})

// 정렬조건
$selectSortBy.addEventListener('change', () => {
    manager($selectEnabled.value, true, $selectSearchRequirements.value, $searchInput.value ? $searchInput.value : '%25', $selectSortMethod.value, $selectSortBy.value,0)
})

// 오름차순 내림차순 정렬
$selectSortMethod.addEventListener('change', () =>{
    manager($selectEnabled.value, true, $selectSearchRequirements.value, $searchInput.value ? $searchInput.value : '%25', $selectSortMethod.value, $selectSortBy.value,0)

})


//초기 렌더링
manager(true,true, '%25', '%25', 'desc', 'companyName', 0);