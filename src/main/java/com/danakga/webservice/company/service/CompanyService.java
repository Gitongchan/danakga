package com.danakga.webservice.company.service;

import com.danakga.webservice.company.dto.request.CompanyUserInfoDto;
import com.danakga.webservice.user.dto.request.UserInfoDto;
import com.danakga.webservice.user.model.UserInfo;

public interface CompanyService {
    //회원탈퇴
    Long companyDeleted(UserInfo userInfo, CompanyUserInfoDto companyUserInfoDto);
}
