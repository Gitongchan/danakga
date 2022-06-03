package com.danakga.webservice.user.service;

import com.danakga.webservice.company.dto.request.CompanyInfoDto;
import com.danakga.webservice.user.dto.request.UpdateUserInfoDto;
import com.danakga.webservice.user.dto.request.UserInfoDto;
import com.danakga.webservice.user.dto.response.ResUserInfoDto;
import com.danakga.webservice.user.model.UserInfo;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    //회원가입
    Long join(UserInfoDto userInfoDto);

    //회원정보 수정
    Long update(UserInfo userInfo , UpdateUserInfoDto updateUserInfoDto);

    //아이디 중복 체크
    Integer userIdCheck(String userid);

    //이메일 중복 체크
    Integer emailCheck(String email);
    
    //회원 탈퇴
    Long userDeleted(UserInfo userInfo,UserInfoDto userInfoDto);

    //회원 정보 조회
    UserInfo userInfoCheck(UserInfo userInfo);

    //사업자 등록 (회원으로 등록된 사용자의 사업자 등록)
    Long companyRegister(UserInfo userInfo,UserInfoDto userInfoDto, CompanyInfoDto companyInfoDto);

    //사업자 복구
    Long companyRestore(UserInfo userInfo,UserInfoDto userInfoDto,CompanyInfoDto companyInfoDto);

    //아이디 찾기
    String useridFind(UserInfoDto userInfoDto);

}
