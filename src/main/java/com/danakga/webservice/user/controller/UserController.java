package com.danakga.webservice.user.controller;

import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.user.dto.request.UserInfoDto;
import com.danakga.webservice.user.dto.response.ResDupliCheckDto;
import com.danakga.webservice.user.dto.response.ResUserJoinDto;
import com.danakga.webservice.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

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
    public ResUserJoinDto join(@Valid UserInfoDto userInfoDto){
        return userService.join(userInfoDto);
    }

    //회원정보 조회
    @GetMapping("")
    public UserInfoDto check(@LoginUser UserInfoDto userInfoDto){
        return userInfoDto;
    }


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
