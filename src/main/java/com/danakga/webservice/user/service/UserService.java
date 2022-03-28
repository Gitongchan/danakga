package com.danakga.webservice.user.service;

import com.danakga.webservice.user.dto.request.UserJoinDto;
import com.danakga.webservice.user.dto.response.ResDupliCheckDto;
import com.danakga.webservice.user.dto.response.ResUserJoinDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    //회원가입
    ResUserJoinDto join(UserJoinDto userJoinDto);

    //아이디 중복 체크
    ResDupliCheckDto userIdCheck(String userid);
    //이메일 중복 체크
    ResDupliCheckDto emailCheck(String email);
}
