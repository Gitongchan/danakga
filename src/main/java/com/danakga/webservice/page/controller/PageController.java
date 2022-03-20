package com.danakga.webservice.page.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/index")
    public String index(){
        return "index";
    }
    @GetMapping("/join")
    public String join(){
        return "join";
    }
    @GetMapping("/login")
    public String login(){
        return "login";
    }
}
