package com.danakga.webservice.user.controller;

import com.danakga.webservice.user.dto.request.UserJoinDto;
import com.danakga.webservice.user.dto.response.ResUserJoinDto;
import com.danakga.webservice.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController{
    @Autowired private final UserService userService;

    //회원가입
    @PostMapping("")
    public ResUserJoinDto join(@Valid UserJoinDto userJoinDto){
        return userService.join(userJoinDto);
    }
}