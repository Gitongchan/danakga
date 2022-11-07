package com.danakga.webservice.page.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    // ------------------공통부분------------------
    @GetMapping("/")
    public String index() {
        return "pages/index";
    }

    @GetMapping("/index")
    public String index2() {
        return "pages/index";
    }

    @GetMapping("/register")
    public String register() {
        return "pages/userRegister";
    }

    @GetMapping("/findId")
    public String findId() {
        return "pages/find-id";
    }

    @GetMapping("/findPw")
    public String findPW() {
        return "pages/find-pw";
    }

    @GetMapping("/id_pw_search")
    public String id_pw_Search() {
        return "pages/search-id-pw";
    }

    @GetMapping("/id_search")
    public String id_Search() {
        return "pages/search_id";
    }

    @GetMapping("/pw_search")
    public String pw_Search() {
        return "pages/search_pw";
    }

    @GetMapping("/product/main")
    public String mainFunction() {
        return "pages/mainfunction";
    }

    @GetMapping("/board/basic") //자유  게시판 조회
    public String basicBoardList() {
        return "pages/basic-boardlist";
    }

    @GetMapping("/board/data") //정보 게시판 조회
    public String dataBoardList() {
        return "pages/data-boardlist";
    }

    @GetMapping("/board/qa") //문의 게시판 조회
    public String qaBoardList() {
        return "pages/qa-boardlist";
    }

    @GetMapping("/board/post") //작성
    public String boardEdition() {
        return "pages/boardPost";
    }

    @GetMapping("/board/qnaPost") //작성
    public String qnaBoardPost() {
        return "pages/qnaBoardPost";
    }

    @GetMapping("/board/info") //게시글 상세조회
    public String boardInfo() {
        return "pages/boardInfo";
    }

    @GetMapping("/board/edit") //게시글 수정
    public String boardEdit() {
        return "pages/boardEdit";
    }

    @GetMapping("/board/faq") //자주묻는질문
    public String boardFAQ() {
        return "pages/faq";
    }

    @GetMapping("/product/info")
    public String productInfo() {
        return "pages/product-details";
    }

    @GetMapping("/product/myTools")
    public String mytools() {
        return "pages/myTools";
    }

    @GetMapping("/mypage/myFolder")
    public String myFolder() {
        return "pages/myFolder";
    }

    @GetMapping("/manager/myFolder")
    public String mypageManagerFolder() {
        return "pages/myFolder";
    }

    // ------------------유저부분------------------

    @GetMapping("/user/mypage")
    public String mypageUser() {
        return "pages/user/mypageUser";
    }

    @GetMapping("/user/orderlist")
    public String myPageOrderList() {
        return "pages/user/mypageUserOrderList";
    }

    @GetMapping("/user/wishlist")
    public String myPageWishList() {
        return "pages/user/mypageUserWishList";
    }

    @GetMapping("/mypage/cart")
    public String myPageCart() {
        return "pages/user/mypageUserCart";
    }


    @GetMapping("/user/myreview")
    public String myReview() {
        return "pages/user/mypageUserReviewList";
    }

    @GetMapping("/user/myboard")
    public String mypageUserBoard() {
        return "pages/user/mypageUserBoard";
    }

    @GetMapping("/user/changeCompany")
    public String myPageUserChangeCompany() {
        return "pages/user/mypageUserChangeCompany";
    }


    @GetMapping("/user/edit")
    public String myPageEdit() {
        return "pages/user/mypageUserEdit";
    }

    @GetMapping("/user/signout")
    public String signOut() {
        return "pages/user/mypageUserSignOut";
    }



    // ------------------사업자 부분(유저기능 사용)------------------
    @GetMapping("/manager/mypage")
    public String mypageManager() {
        return "pages/manager/mypageManager";
    }

    @GetMapping("/manager/orderList")
    public String managerOrderList() {
        return "pages/manager/mypageManagerOrderList";
    }

    @GetMapping("/manager/wishlist")
    public String managerWishList() {
        return "pages/manager/mypageManagerWishList";
    }

    @GetMapping("/manager/cart")
    public String managerCart() {
        return "pages/manager/mypageManagerCart";
    }

    @GetMapping("/manager/myreview")
    public String managerReview() {
        return "pages/manager/mypageManagerReviewList";
    }

    @GetMapping("/manager/myboard")
    public String mypageManagerBoard() {
        return "pages/manager/mypageManagerBoard";
    }




    // --------------------상점 부분---------------------
    @GetMapping("/shop")
    public String shop() {
        return "pages/manager/mypageShopInfo";
    }

    @GetMapping("/shop/addProduct")
    public String shopAddProduct() {
        return "pages/manager/mypageShopAddProduct";
    }

    @GetMapping("/shop/productList")
    public String shopProductList() {
        return "pages/manager/mypageShopProductList";
    }

    @GetMapping("/shop/orderList")
    public String shopOrderList() {
        return "pages/manager/mypageShopOrderList";
    }

    @GetMapping("/shop/statistics")
    public String shopStatistics() {
        return "pages/manager/mypageShopStatistics";
    }

    @GetMapping("/shop/signOut")
    public String shopSignOut() {
        return "pages/manager/mypageShopSignOut";
    }


    @GetMapping("/shop/product/edit")
    public String shopEditProduct() {
        return "pages/manager/mypageShopEditProduct";
    }

    @GetMapping("/product/grid")
    public String productGrid() {
        return "pages/product-grids";
    }

    // ------------------어드민------------------

    @GetMapping("/admin/index")
    public String adminIndex() {
        return "admin/index";
    }


    //잡


    //---접근 오류 페이지 ---
    @GetMapping("/accessDenied")
    public String accessDenied(){
        return "errors/403";
    }
}
