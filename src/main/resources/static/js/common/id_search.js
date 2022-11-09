document.querySelector('#search_id').addEventListener('click', async () => {
    const email = document.querySelector('#search-email').value;
    const phone = document.querySelector('#search-phone').value;
    if(email.length <= 0 || phone.length <= 0){
        alert('이메일 또는 전화번호를 제대로 입력해주세요!');
        return
    }
    const res = await fetch(`/api/userid_find`,{
        method:'POST',
        headers: {
                'header': header,
                'X-Requested-With': 'XMLHttpRequest',
                "Content-Type": "application/json",
                'X-CSRF-Token': token
        },
        body: JSON.stringify({email,phone})
    });
    if(res.ok){
        const data = await res.json();
        console.log(data);
        if(data.id === ''){
            alert('해당하는 정보로 존재하는 아이디가 없습니다!');
        }else{
            alert('계정 정보 찾는 중...');
            location.replace(`/findId?userId=${data.userid}`);
        }
    }
})