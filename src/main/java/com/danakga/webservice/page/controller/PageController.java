package com.danakga.webservice.page.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/")
    public String index(){
        return "testindex";
    }

    @GetMapping("/joinPage")
    public String join(){
        return "join";
    }

    @GetMapping("/index")
    public String testindex(){
        return "pages/index";
    }

    @GetMapping("/register")
    public String testval(){
        return "pages/userRegister";
    }

    @GetMapping("/test/mainfunc")
    public String testmain(){
        return "pages/mainfunction";
    }

    @GetMapping("/user/mypage")
    public String mypageUser(){
        return "pages/mypageUser";
    }

    @GetMapping("/manager/mypage")
    public String mypageAdmin(){
        return "pages/mypageManager";
    }


    @GetMapping("/board") //조회
    public String boardlist(){return "pages/boardlist";}

    @GetMapping("/board/post") //작성
    public String boardEdition(){return "pages/boardPost";}

    @GetMapping("/board/info") //게시글 상세조회
    public String boardInfo(){return "pages/boardInfo";}

    @GetMapping("/board/edit") //게시글 수정
    public String boardEdit(){return "pages/boardEdit";}

    @GetMapping("/user/edit")
    public String myPageEdit(){
        return "pages/mypageUserEdit";
    }

    @GetMapping("/user/signout")
    public String signOut(){
        return "pages/userSignout";
    }

    @GetMapping("/addproduct")
    public String addProduct(){
        return "pages/addProduct";
    }

}
