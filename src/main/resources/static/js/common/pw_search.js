const pwBtn = document.querySelector('#search_pw');
pwBtn.addEventListener('click', async () => {
    const userid = document.querySelector('#reg-id').value;
    const email = document.querySelector('#reg-email').value;
    const phone = document.querySelector('#reg-phone').value;
    if(userid.length <= 0 || email.length <= 0 || phone.length <= 0){
        alert('정보를 올바르게 입력해주세요!');
        return
    }
    try{
        alert("정보를 찾고있습니다!");
        pwBtn.disabled = true;
        const res = await fetch(`/api/userpw_find`,{
            method: 'POST',
            headers: {
                'header': header,
                'X-Requested-With': 'XMLHttpRequest',
                "Content-Type": "application/json",
                'X-CSRF-Token': token
            },
            body: JSON.stringify({userid,email,phone})
        });
        if(res.ok){
            const data = await res.json();

            alert("해당 이메일로 임시번호를 발송하였습니다!");
            location.replace('/findPw');
        }else{
            pwBtn.disabled = false;
            alert("일치하는 정보가 없습니다!");
        }
    }catch (e) {
        console.log('에러남',e);
    }


})