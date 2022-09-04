package com.danakga.webservice.page.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/")
    public String index(){
        return "pages/index";
    }

    @GetMapping("/index")
    public String index2(){
        return "pages/index";
    }

    @GetMapping("/register")
    public String register(){
        return "pages/userRegister";
    }

    @GetMapping("/findId")
    public String findId(){
        return "pages/find-id";
    }

    @GetMapping("/findPw")
    public String findPW(){
        return "pages/find-pw";
    }

    @GetMapping("/id_pw_search")
    public String id_pw_Search(){
        return "pages/search-id-pw";
    }

    @GetMapping("/id_search")
    public String id_Search(){
        return "pages/search_id";
    }

    @GetMapping("/pw_search")
    public String pw_Search(){
        return "pages/search_pw";
    }

    @GetMapping("/product/main")
    public String mainFunction(){
        return "pages/mainfunction";
    }

    @GetMapping("/user/mypage")
    public String mypageUser(){
        return "pages/mypageUser";
    }

    @GetMapping("/user/myboard")
    public String mypageUserBoard(){
        return "pages/mypageUserBoard";
    }

    @GetMapping("/user/wishlist")
    public String myPageWishList(){
        return "pages/wish-list";
    }

    @GetMapping("/user/orderlist")
    public String myPageOrderList() { return "pages/order-list"; }
    @GetMapping("/manager/myboard")
    public String mypageManagerBoard(){
        return "pages/mypageManagerBoard";
    }

    @GetMapping("/manager/mypage")
    public String mypageManager(){return "pages/mypageManager";}
    @GetMapping("/product/myTools")
    public String mytools(){return "pages/myTools";}

    @GetMapping("/mypage/myFolder")
    public String myFolder(){return "pages/myFolder";}

    @GetMapping("/mypage/cart")
    public String myPageCart(){return "pages/cart";}

    @GetMapping("/manager/companyinfo")
    public String mypageCompany(){
        return "pages/mypageManagerInfo";
    }

    @GetMapping("/manager/myproductlist")
    public String productList(){
        return "pages/myProductList";
    }

    @GetMapping("/manager/orderList")
    public String managerOrderList(){
        return "pages/managerOrder-list";
    }
    @GetMapping("/product/info")
    public String productInfo(){
        return "pages/product-details";
    }

    @GetMapping("/product/edit")
    public String productEdit(){
        return "pages/edit-product";
    }

    @GetMapping("/board/basic") //자유  게시판 조회
    public String basicBoardList(){return "pages/basic-boardlist";}

    @GetMapping("/board/qa") //문의 게시판 조회
    public String qaBoardList(){return "pages/qa-boardlist";}

    @GetMapping("/board/post") //작성
    public String boardEdition(){return "pages/boardPost";}

    @GetMapping("/board/info") //게시글 상세조회
    public String boardInfo(){return "pages/boardInfo";}

    @GetMapping("/board/edit") //게시글 수정
    public String boardEdit(){return "pages/boardEdit";}

    @GetMapping("/board/faq") //자주묻는질문
    public String boardFAQ(){return "pages/faq";}

    @GetMapping("/user/edit")
    public String myPageEdit(){
        return "pages/mypageUserEdit";
    }

    @GetMapping("/user/signout")
    public String signOut(){
        return "pages/userSignout";
    }

    @GetMapping("/CompanySignout")
    public String CompanySignout(){return "pages/CompanySignout";}

    @GetMapping("/user/myreview")
    public String myReview() { return "pages/my-reviewList";}
    @GetMapping("/addproduct")
    public String addProduct(){
        return "pages/addProduct";
    }

    @GetMapping("/product/grid")
    public String productgrid(){
        return "pages/product-grids";
    }
    @GetMapping("/userchangecompany")
    public  String userchangecompany(){return "pages/userchangecompany";}
}
