package com.danakga.webservice.user.controller;

import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.user.dto.request.UserInfoDto;
import com.danakga.webservice.user.dto.response.ResUserInfoDto;
import com.danakga.webservice.util.responseDto.ResResultDto;
import com.danakga.webservice.user.model.UserInfo;
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

    //회원정보 조회
    @GetMapping("")
    public ResUserInfoDto check(@LoginUser UserInfo userInfo){
        return new ResUserInfoDto(userInfo);
    }

    //회원정보 수정
    @PutMapping("")
    public ResResultDto update(@LoginUser UserInfo userInfo, @Valid @RequestBody UserInfoDto userInfoDto){
        System.out.println("userInfo = " + userInfo.getName());
        System.out.println("userInfoDto = " + userInfoDto.getName());
        Long result = userService.update(userInfo,userInfoDto);
        return result == -1L ?
                new ResResultDto(result,"회원정보 변경 실패.") : new ResResultDto(result,"회원정보 변경 성공.");
    }

    
    
    /**              마이페이지 기능               **/



    //회원 탈퇴
    @PutMapping("/user_deleted")
    public ResResultDto userDeleted(@LoginUser UserInfo userInfo, @RequestBody UserInfoDto userInfoDto){
        Long result = userService.userDeleted(userInfo,userInfoDto);
        return result == -1L ?
                new ResResultDto(result,"회원 탈퇴 실패") : new ResResultDto(result,"회원 탈퇴 성공");
    }


}
