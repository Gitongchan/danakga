package com.danakga.webservice.user.controller;

import com.danakga.webservice.user.dto.request.UserInfoDto;
import com.danakga.webservice.user.dto.response.ResDupliCheckDto;
import com.danakga.webservice.user.dto.response.ResUserIdDto;
import com.danakga.webservice.user.service.UserService;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class PermitAllController {

    private final UserService userService;

    //토큰발행
    @GetMapping(value = "/token")
    public CsrfToken getToken(CsrfToken token) {
        System.out.println("token = " + token);
        return token;
    }

    //회원가입
    @PostMapping("/signup")
    public ResResultDto join(@Valid @RequestBody UserInfoDto userInfoDto){
        System.out.println("userInfoDto = " + userInfoDto);
        Long result = userService.join(userInfoDto);
        return result == -1L ?
                new ResResultDto(result,"회원가입 실패.") : new ResResultDto(result,"회원가입 성공.");
    }

    //userid체크
    @GetMapping("/userid_check")
    public ResDupliCheckDto userIdCheck(@RequestParam("userid") String userid){
        return new ResDupliCheckDto(userService.userIdCheck(userid));
    }

    //email체크
    @GetMapping("/email_check")
    public ResDupliCheckDto emailCheck(@RequestParam("email") String email){
        return new ResDupliCheckDto(userService.emailCheck(email));
    }

    //id찾기
    @GetMapping("/userid_find")
    public ResUserIdDto findUserid(@RequestBody UserInfoDto userInfoDto){
        String result = userService.useridFind(userInfoDto);
        System.out.println(result);
        return result == null ?
                new ResUserIdDto(null,"아이디 찾기 실패.") : new ResUserIdDto(result,"아이디 찾기 성공.");
    }


    //pw찾기
    @Transactional
    @PostMapping("/userpw_find")
    public ResResultDto findUserPw(@RequestBody UserInfoDto userInfoDto){

        Long result = userService.passwordFind(userInfoDto);

        return new ResResultDto(result, "메일 발송 완료.");

    }

}
