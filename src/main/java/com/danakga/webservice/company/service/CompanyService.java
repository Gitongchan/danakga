package com.danakga.webservice.company.service;

import com.danakga.webservice.company.dto.request.CompanyInfoDto;
import com.danakga.webservice.company.dto.request.CompanyUserInfoDto;
import com.danakga.webservice.user.dto.request.UserInfoDto;
import com.danakga.webservice.user.model.UserInfo;

public interface CompanyService {
    //사업자탈퇴
    Long companyDeleted(UserInfo userInfo, CompanyUserInfoDto companyUserInfoDto);

    //업체명 체크
    Integer companyNameCheck(String companyName);

    //사업자 회원 등록
    Long companyRegister(CompanyUserInfoDto companyUserInfoDto);
}
