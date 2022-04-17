package com.danakga.webservice.company.controller;

import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.company.dto.request.CompanyUserInfoDto;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/manager")
public class CompanyController {
    @PutMapping
    public ResResultDto companyDeleted(@LoginUser UserInfo userInfo, CompanyUserInfoDto companyUserInfoDto){
        return null;
    }
}
