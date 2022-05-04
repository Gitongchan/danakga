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

    @GetMapping("/page/manager/test")
    public String managerTest() {return "managerTest"; }


}
