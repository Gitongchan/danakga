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

    @GetMapping("/board/qa") //문의 게시판 조회
    public String qaBoardList() {
        return "pages/qa-boardlist";
    }

    @GetMapping("/board/post") //작성
    public String boardEdition() {
        return "pages/boardPost";
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


    // ------------------유저부분------------------

    @GetMapping("/user/mypage")
    public String mypageUser() {
        return "pages/user/mypageUser";
    }

    @GetMapping("/user/myboard")
    public String mypageUserBoard() {
        return "pages/user/mypageUserBoard";
    }

    @GetMapping("/user/wishlist")
    public String myPageWishList() {
        return "pages/user/wish-list";
    }

    @GetMapping("/user/orderlist")
    public String myPageOrderList() {
        return "pages/user/order-list";
    }

    @GetMapping("/product/myTools")
    public String mytools() {
        return "pages/user/myTools";
    }

    @GetMapping("/mypage/myFolder")
    public String myFolder() {
        return "pages/user/myFolder";
    }

    @GetMapping("/mypage/cart")
    public String myPageCart() {
        return "pages/user/cart";
    }


    @GetMapping("/userchangecompany")
    public String userchangecompany() {
        return "pages/user/userchangecompany";
    }


    @GetMapping("/user/edit")
    public String myPageEdit() {
        return "pages/user/mypageUserEdit";
    }

    @GetMapping("/user/signout")
    public String signOut() {
        return "pages/user/userSignout";
    }

    @GetMapping("/user/myreview")
    public String myReview() {
        return "pages/user/my-reviewList";
    }


    // ------------------사업자 부분------------------
    @GetMapping("/manager/wishlist")
    public String managerWishList() {
        return "pages/manager/managerWish-list";
    }

    @GetMapping("/manager/myOrderlist")
    public String managerMyOrderList() {
        return "pages/manager/managerOrder-list";
    }

    @GetMapping("/manager/myboard")
    public String mypageManagerBoard() {
        return "pages/manager/mypageManagerBoard";
    }

    @GetMapping("/manager/mypage")
    public String mypageManager() {
        return "pages/manager/mypageManager";
    }

    @GetMapping("/manager/cart")
    public String managerCart() {
        return "pages/manager/manager-cart";
    }

    @GetMapping("/manager/companyinfo")
    public String mypageCompany() {
        return "pages/manager/mypageManagerInfo";
    }

    @GetMapping("/manager/myproductlist")
    public String productList() {
        return "pages/manager/myProductList";
    }

    @GetMapping("/manager/orderList")
    public String managerOrderList() {
        return "pages/manager/managerOrder-list";
    }

    @GetMapping("/addproduct")
    public String addProduct() {
        return "pages/manager/addProduct";
    }

    @GetMapping("/product/edit")
    public String productEdit() {
        return "pagesmanager/edit-product";
    }

    @GetMapping("/CompanySignout")
    public String CompanySignout() {
        return "pages/manager/CompanySignout";
    }


    // ------------------어드민------------------

    @GetMapping("/admin/index")
    public String adminIndex() {
        return "admin/index";
    }


    //잡
    @GetMapping("/product/grid")
    public String productgrid() {
        return "pages/product-grids";
    }
}
