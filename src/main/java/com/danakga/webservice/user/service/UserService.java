package com.danakga.webservice.user.service;

import com.danakga.webservice.board.dto.response.ResBoardListDto;
import com.danakga.webservice.company.dto.request.CompanyInfoDto;
import com.danakga.webservice.user.dto.request.UpdateUserInfoDto;
import com.danakga.webservice.user.dto.request.UserInfoDto;
import com.danakga.webservice.user.model.UserInfo;
import org.springframework.data.domain.Pageable;
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
    Long userDeleted(UserInfo userInfo,String password);

    //회원 정보 조회
    UserInfo userInfoCheck(UserInfo userInfo);

    //사업자 등록 (회원으로 등록된 사용자의 사업자 등록)
    Long companyRegister(UserInfo userInfo,UserInfoDto userInfoDto, CompanyInfoDto companyInfoDto);

    //사업자 복구
    Long companyRestore(UserInfo userInfo,String password);

    //아이디 찾기
    String useridFind(UserInfoDto userInfoDto);
    //페스워드 찾기
    String passwordFind(UserInfoDto userInfoDto);

    ResBoardListDto myPostList(UserInfo userInfo, String boardType, Pageable pageable, int page);

    ResBoardListDto myCommentList(UserInfo userInfo, String boardType, Pageable pageable, int page);

}
