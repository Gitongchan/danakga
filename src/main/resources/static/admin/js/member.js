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

// 일반 회원
async function userList(userEnabled, searchRequirements, searchWord, sortMethod, sortBy, page){
    const res = await fetch(`/admin/members/userList?userEnabled=${userEnabled}&searchRequirements=${searchRequirements}&searchWord=${searchWord}&sortMethod=${sortMethod}&sortBy=${sortBy}&page=${page}`)

    if(!res.ok){
        alert("조회에 실패하였습니다!")
        return 0;
    }

    const data = await res.json();

    console.log(data);
}


//초기 렌더링
userList(true, '%25', '%25', 'desc', 'name', 0);