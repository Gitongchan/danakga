package com.danakga.webservice.user.controller;

import com.danakga.webservice.user.dto.UserJoinDto;
import com.danakga.webservice.user.repository.UserRepository;
import com.danakga.webservice.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController{
    @Autowired private final UserService userService;

    @PostMapping("")
    public String join(UserJoinDto userJoinDto){
        System.out.println(userJoinDto);

        userService.join(userJoinDto);

        return "회원가입성공";
    }

}
