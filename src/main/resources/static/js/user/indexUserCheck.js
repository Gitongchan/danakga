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

(async function() {
    try {
        const res = await fetch('/api/user')
        if(res.status === 200){
            const data = await res.json();
            checkID.value = data.id;
            checkName.value = data.userid;
            cart.innerHTML = `
            <a href="javascript:void(0)" class="main-btn">
                                  <i class="lni lni-cart"></i>
                                  <span class="total-items">0</span>
                                </a>
                                <!-- Shopping Item -->
                                <div class="shopping-item">
                                  <div class="dropdown-cart-header">
                                    <span><!--아이템 갯수--></span>
                                    <a href="#">장바구니 보기</a>
                                  </div>
                                  <ul class="shopping-list">
                                    <!-- cart-item내용 들어감-->
                                  </ul>
                                  <div class="bottom">
                                    <div class="total">
                                      <span>합계</span>
                                      <span class="total-amount"><!--가격--></span>
                                    </div>
                                    <div class="button">
                                      <a href="#" class="btn animate">주문하기</a>
                                    </div>
                                  </div>
                                </div>
            `
            wish.innerHTML = `
                <a href="javascript:void(0)">
                    <i class="lni lni-heart"></i>
                    <span class="total-items">0<!--찜한 아이템 갯수--></span>
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
