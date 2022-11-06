package com.danakga.webservice.admin.service;

import com.danakga.webservice.admin.dto.response.ResManagerInfoDetailDto;
import com.danakga.webservice.admin.dto.response.ResManagerInfoDto;
import com.danakga.webservice.admin.dto.response.ResUserInfoDetailDto;
import com.danakga.webservice.admin.dto.response.ResUserInfoDto;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.model.UserRole;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminService {
    List<ResUserInfoDto> findUserInfoList(UserInfo userInfo, UserRole userRole,String userEnabled , String searchRequirements , String searchWord,
                                          String sortMethod, String sortBy, Pageable pageable  , int page);

    List<ResManagerInfoDto> findManagerInfoList(UserInfo userInfo, UserRole userRole, String userEnabled , String searchRequirements , String searchWord,
                                             String sortMethod, String sortBy,Pageable pageable , int page);

    ResUserInfoDetailDto findUserInfoDetail(UserInfo userInfo,String userId);

    ResManagerInfoDetailDto findManagerInfoDetail(UserInfo userInfo, String userId);
}
