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
    public String testindex(){
        return "pages/index";
    }

    @GetMapping("/register")
    public String testval(){
        return "pages/userRegister";
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

    @GetMapping("/manager/myboard")
    public String mypageManagerBoard(){
        return "pages/mypageUserBoard";
    }

    @GetMapping("/manager/mypage")
    public String mypageManager(){
        return "pages/mypageManager";
    }

    @GetMapping("/manager/companyinfo")
    public String mypageCompany(){
        return "pages/mypageManagerInfo";
    }

    @GetMapping("/manager/myproductlist")
    public String productList(){
        return "pages/myProductList";
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
