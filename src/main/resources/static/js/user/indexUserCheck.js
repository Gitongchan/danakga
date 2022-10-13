//토근값 가져오기
const token = document.querySelector('meta[name="_csrf"]').content;
const header = document.querySelector('meta[name="_csrf_header"]').content;

//로그인 안되어있다면 로그인, 회원가입 > 로그인 되어있다면 내정보|로그아웃
const checkID = document.getElementById('checkUserId');
const checkCompany = document.getElementById('checkCompanyId');
const checkName = document.getElementById('checkName');

const cart = document.getElementById('cart-items');
const wish = document.getElementById('wishlist');
const loginInfo = document.getElementById('user-info');

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

(async function() {
    try {
        const res = await fetch('/api/user')
        if(res.status === 200){
            const data = await res.json();
            checkID.value = data.id;
            checkName.value = data.userid;

            //장바구니 아이 갯수 체크
            const cartRes = await fetch('/api/user/cart');
            const cartData = await cartRes.json();
            cart.innerHTML = `
            <a href="/mypage/cart" class="main-btn">
                                  <i class="lni lni-cart"></i>
                                  <span class="total-items">${cartData.length}</span>
                                </a>
            `

            //찜 갯수 체크
            const wishRes = await fetch('/api/user/wish/0');
            const wishData = await wishRes.json();
            wish.innerHTML = `
                <a href="/user/wishlist">
                    <i class="lni lni-heart"></i>
                    <span class="total-items">${wishData.length === 0 ? 0 : wishData[0].totalElement}</span>
                </a>
            `


            const mRes = await fetch('/api/manager');
                if(mRes.status === 200){
                    const mData = await mRes.json();
                    checkCompany.value = mData.companyId;
                    loginInfo.innerHTML = `
                        <div class="user">
                            <i class="lni lni-user"></i>                    
                          ${data.userid}님! 안녕하세요!
                        </div>
                        <ul class="user-login">
                          <li>
                            <a href="/manager/mypage">내정보</a>
                          </li>
                          <li>
                          <form method="POST" action="/api/user/logout">
                                <input type="hidden" name="_csrf" value="${token}">
                                <button>로그아웃</button>
                          </form>
                          </li>
                        </ul>
                    `
                }else{
                    loginInfo.innerHTML = `
                        <div class="user">
                            <i class="lni lni-user"></i>                    
                          ${data.userid}님! 안녕하세요!
                        </div>
                        <ul class="user-login">
                          <li>
                            <a href="/user/mypage">내정보</a>
                          </li>
                          <li>
                          <form method="POST" action="/api/user/logout">
                                <input type="hidden" name="_csrf" value="${token}">
                                <button>로그아웃</button>
                          </form>
                            
                          </li>
                        </ul>
                    `
                }
        }
    }
    catch (e) {
    }
})();
