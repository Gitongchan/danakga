package com.danakga.webservice.admin.service;

import com.danakga.webservice.admin.dto.response.ResManagerInfoDetailDto;
import com.danakga.webservice.admin.dto.response.ResManagerInfoDto;
import com.danakga.webservice.admin.dto.response.ResUserInfoDetailDto;
import com.danakga.webservice.admin.dto.response.ResUserInfoDto;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.model.UserRole;
import com.danakga.webservice.util.responseDto.ResResultDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminService {
    List<ResUserInfoDto> findUserInfoList(UserInfo userInfo, UserRole userRole,String userEnabled , String searchRequirements , String searchWord,
                                          String sortMethod, String sortBy, Pageable pageable  , int page);

    List<ResManagerInfoDto> findManagerInfoList(UserInfo userInfo, UserRole userRole, String userEnabled,String companyEnabled , String searchRequirements , String searchWord,
                                             String sortMethod, String sortBy,Pageable pageable , int page);

    ResUserInfoDetailDto findUserInfoDetail(UserInfo userInfo,String userId);

    ResManagerInfoDetailDto findManagerInfoDetail(UserInfo userInfo, String userId);

    //사용자 정지
    Long stopUsing(UserInfo userInfo,String userId);

    //사업자 정지
    Long stopUsingManager(UserInfo userInfo,String companyName);

    //사용자 삭제
    Long deleteMember(UserInfo userInfo,String userId);

    //사업자 정보 삭제
    Long deleteManager(UserInfo userInfo,String companyName);

    //사용자 복구 - 정지 탈퇴 상태에서만 가능
    Long restoreUser(UserInfo userInfo,String userId);

    //사업자 복구 - 정지,탈퇴상태에서만 가능
    Long restoreManager(UserInfo userInfo,String companyName);


    
}
