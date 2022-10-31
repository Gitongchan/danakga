package com.danakga.webservice.company.service;

import com.danakga.webservice.company.dto.request.CompanyInfoDto;
import com.danakga.webservice.company.dto.request.CompanyUserInfoDto;
import com.danakga.webservice.company.dto.response.ResProductByCompanyDto;
import com.danakga.webservice.company.model.CompanyInfo;
import com.danakga.webservice.user.model.UserInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public interface CompanyService {
    //사업자탈퇴
    Long companyDeleted(UserInfo userInfo,String password);

    //업체명 체크
    Integer companyNameCheck(String companyName);

    //사업자 회원 등록
    Long companyRegister(CompanyUserInfoDto companyUserInfoDto);

    //사업자 회사 정보 수정
    Long companyUpdate(UserInfo userInfo, CompanyInfoDto companyInfoDto);

    //사업자 회사 정보 조회
    CompanyInfo companyInfoCheck(UserInfo userInfo);

    ResProductByCompanyDto productByCompanyDto(String companyName,
                                               String sortBy, String sortMethod,
                                               String productName,int productStock,
                                               Pageable pageable,
                                               int page);

}
