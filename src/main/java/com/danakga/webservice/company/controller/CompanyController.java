package com.danakga.webservice.company.controller;

import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.company.dto.request.CompanyInfoDto;
import com.danakga.webservice.company.dto.request.CompanyUserInfoDto;
import com.danakga.webservice.company.service.CompanyService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/manager")
public class CompanyController {

    @Autowired private final CompanyService companyService;

    //사업자 탈퇴
    @PutMapping("/deleted")
    public ResResultDto companyDeleted(@LoginUser UserInfo userInfo, @RequestBody CompanyUserInfoDto companyUserInfoDto){

        Long result = companyService.companyDeleted(userInfo,companyUserInfoDto);
        return result == -1L ?
                new ResResultDto(result,"사업자 탈퇴 실패.") : new ResResultDto(result,"사업자 탈퇴 성공.");
    }
    
    //사업자 정보 수정
    @PutMapping("/update")
    public ResResultDto companyUpdate(@LoginUser UserInfo userInfo,@RequestBody CompanyInfoDto companyInfoDto){
        Long result = companyService.companyUpdate(userInfo,companyInfoDto);
        return result == -1L ?
                new ResResultDto(result,"회사정보 수정 실패.") : new ResResultDto(result,"회사정보 수정 성공.");
    }


}
