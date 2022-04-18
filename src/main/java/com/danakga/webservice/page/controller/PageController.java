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
    @GetMapping("/loginPage")
    public String login(){
        return "login";
    }
    @GetMapping("/test/index")
    public String testindex(){
        return "pages/index";
    }

    @GetMapping("/test/val")
    public String testval(){
        return "pages/register";
    }

    @GetMapping("/test/mainfunc")
    public String testmain(){
        return "pages/mainfunction";
    }

    @GetMapping("/test/login")
    public String testlogin(){
        return "pages/login";
    }


}
