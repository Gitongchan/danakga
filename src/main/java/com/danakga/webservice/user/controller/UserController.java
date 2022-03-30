package com.danakga.webservice.user.controller;

import com.danakga.webservice.user.dto.request.UserJoinDto;
import com.danakga.webservice.user.dto.response.ResDupliCheckDto;
import com.danakga.webservice.user.dto.response.ResUserJoinDto;
import com.danakga.webservice.user.dto.response.ResUserModifyDto;
import com.danakga.webservice.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController{
    @Autowired private final UserService userService;

    @GetMapping(value = "/token")
    public CsrfToken getToken(CsrfToken token) {
        System.out.println("실행됨");
        System.out.println("token = " + token);
        return token;
    }

    //회원가입
    @PostMapping("")
    public ResUserJoinDto join(@Valid UserJoinDto userJoinDto){
        return userService.join(userJoinDto);
    }

    //회원정보 수정


    //userid체크
    @PostMapping("/userid_check")
    public ResDupliCheckDto userIdCheck(@RequestParam("userid") String userid){
        return userService.userIdCheck(userid);
    }

    //email체크
    @PostMapping("/email_check")
    public ResDupliCheckDto emailCheck(@RequestParam("email") String email){
        return userService.emailCheck(email);
    }
}
