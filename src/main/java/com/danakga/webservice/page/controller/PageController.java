package com.danakga.webservice.page.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/")
    public String index(){
        return "testindex";
    }

    @GetMapping("/test/index")
    public String testindex(){
        return "pages/index";
    }

    @GetMapping("/register")
    public String testval(){
        return "pages/userRegister";
    }

    @GetMapping("/companyval")
    public String companyval(){
        return "pages/companyRegister";
    }


    @GetMapping("/test/mainfunc")
    public String testmain(){
        return "pages/mainfunction";
    }

    @GetMapping("/test/mypageNav")
    public String testnav(){
        return "pages/mypageNAV";
    }
    @GetMapping("/mypage")
    public String mypage(){
        return "pages/mypage";
    }

}
