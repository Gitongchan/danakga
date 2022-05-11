package com.danakga.webservice.user.service;

import com.danakga.webservice.user.dto.request.UserInfoDto;
import com.danakga.webservice.user.dto.response.ResUserInfoDto;
import com.danakga.webservice.user.model.UserInfo;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    //회원가입
    Long join(UserInfoDto userInfoDto);

    //회원정보 수정
    Long update(UserInfo userInfo , UserInfoDto userInfoDto);

    //아이디 중복 체크
    Integer userIdCheck(String userid);

    //이메일 중복 체크
    Integer emailCheck(String email);
    
    //회원 탈퇴
    Long userDeleted(UserInfo userInfo,UserInfoDto userInfoDto);

    UserInfo userInfoCheck(UserInfo userInfo);


}
