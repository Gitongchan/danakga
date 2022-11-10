// 회원관리 기능 넣는 곳

// 사용자 ,사업자 목록 조회(userEnabled) * true는 전체 조회 , false는 탈퇴자만 조회
// 사용자 , 검색조건(searchRequirements) : userEmail, userName, userId, userPhone 총 4가지만 가능

// searchWord = 검색어
// sortMethod = 오름차순(asc), 내림차순(desc)
// sortBy = name

/*
*  email: "pp@naver.com"
*  name: "유병진"
*  phone: "01091232333"
*  userDeletedDate: null
*  userEnabled: true
*  userid: "user123"
* */

menuId.value = '0';
const $table = document.querySelector('tbody');
const $selectSearchRequirements = document.querySelector('#selectSearchRequirements');
const $selectEnabled = document.querySelector('#selectEnabled');
const $selectSortBy = document.querySelector('#selectSortBy');
const $selectSortMethod = document.querySelector('#selectSortMethod');

/*
* email
* name
* pwd
* phone
* role
* userAdrNum
* userDeletedDate
* userDetailAdr
* userLotAdr
* userStreetAdr
* userid
* */

// 버튼기능
const userActions = {
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
                return
            }

            const data = await res.json();
            alert(data.message);
            userList($selectEnabled.value, $selectSearchRequirements.value, $searchInput.value ? $searchInput.value : '%25', $selectSortMethod.value, $selectSortBy.value,0)
        }
    },
    reuse: async (id) => {
        if(confirm("계정을 복구하시겠습니까?")){
            const res = await fetch(`/admin/members/user/restore/${id}`,{
                method: "PUT",
                headers: {
                    'header': header,
                    'X-CSRF-Token': token,
                    "Content-Type": "application/json"
                },
            });

            if(!res.ok){
                alert("복구 실패!")
                return;
            }

            const data = await res.json();
            alert(data.message);
            userList($selectEnabled.value, $selectSearchRequirements.value, $searchInput.value ? $searchInput.value : '%25', $selectSortMethod.value, $selectSortBy.value,0)
        }
    },
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
                return;
            }

            const data = await res.json();
            alert(data.message);
            userList($selectEnabled.value, $selectSearchRequirements.value, $searchInput.value ? $searchInput.value : '%25', $selectSortMethod.value, $selectSortBy.value,0)
        }
    },
}


// 일반 회원
async function userList(userEnabled, searchRequirements, searchWord, sortMethod, sortBy, page){
    const res = await fetch(`/admin/members/userList?userEnabled=${userEnabled}&searchRequirements=${searchRequirements}&searchWord=${searchWord}&sortMethod=${sortMethod}&sortBy=${sortBy}&page=${page}`)

    if(!res.ok){
        alert("조회에 실패하였습니다!")
        return 0;
    }

    $table.innerHTML = '';
    const data = await res.json();

    for(const item of data){
        if(item.userEnabled){
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
            userActions[action](id);
        }
    });

    console.log(data);
}

function Alert(event){
    console.log(event.target);
}

// 탈퇴여부
$selectEnabled.addEventListener('change', () => {
    userList($selectEnabled.value, $selectSearchRequirements.value, $searchInput.value ? $searchInput.value : '%25', $selectSortMethod.value, $selectSortBy.value,0)
})

// 정렬조건
$selectSortBy.addEventListener('change', () => {
    userList($selectEnabled.value, $selectSearchRequirements.value, $searchInput.value ? $searchInput.value : '%25', $selectSortMethod.value, $selectSortBy.value,0)
})

// 오름차순 내림차순 정렬
$selectSortMethod.addEventListener('change', () =>{
    userList($selectEnabled.value, $selectSearchRequirements.value, $searchInput.value ? $searchInput.value : '%25', $selectSortMethod.value, $selectSortBy.value,0)

})




//초기 렌더링
userList(true, '%25', '%25', 'desc', 'name', 0);