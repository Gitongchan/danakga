package com.danakga.webservice.user.service;

import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.user.dto.request.UserInfoDto;
import com.danakga.webservice.user.dto.response.ResDupliCheckDto;
import com.danakga.webservice.user.dto.response.ResUserResultDto;
import com.danakga.webservice.user.model.UserInfo;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    //회원가입
    Long join(UserInfoDto userInfoDto);
    //회원정보 수정
    Long update(@LoginUser UserInfo userInfo , UserInfoDto userInfoDto);
    //아이디 중복 체크
    Integer userIdCheck(String userid);
    //이메일 중복 체크
    Integer emailCheck(String email);
}
