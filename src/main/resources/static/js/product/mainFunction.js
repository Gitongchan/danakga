// 카테고리 더보기 버튼 부분
const moreBtn = document.querySelectorAll('.btn_item_more');

for(const btn of moreBtn){
    btn.addEventListener('click', (event) => {
        const checkClass = event.target.parentNode.parentElement.classList;
        if(checkClass.contains('open')){
            checkClass.remove('open');
        }else{
            checkClass.add('open');
        }
    })
}