package com.danakga.webservice.company.controller;

import com.danakga.webservice.company.dto.request.CompanyInfoDto;
import com.danakga.webservice.company.dto.request.CompanyUserInfoDto;
import com.danakga.webservice.company.service.CompanyService;
import com.danakga.webservice.user.dto.request.UserInfoDto;
import com.danakga.webservice.user.dto.response.ResDupliCheckDto;
import com.danakga.webservice.user.service.UserService;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/company")
public class CompanyPermitAllController {
    @Autowired private final CompanyService companyService;
    @Autowired private final UserService userService;

    //업체명 중복 체크
    @GetMapping("/name_check")
    public ResDupliCheckDto companyNameCheck(@RequestParam("companyName") String companyName){
        return new ResDupliCheckDto(companyService.companyNameCheck(companyName));
    }
    //사업자 회원가입
    @PostMapping("/signup")
    public ResResultDto companyRegister(@RequestBody CompanyUserInfoDto companyUserInfoDto){
        Long result = companyService.companyRegister(companyUserInfoDto);
        //중복 id,email 검증
        Integer idCheckResult = userService.userIdCheck(companyUserInfoDto.getUserid());
        Integer emailCheckResult = userService.emailCheck(companyUserInfoDto.getEmail());

        if(idCheckResult.equals(-1)||emailCheckResult.equals(-1)) {
            return new ResResultDto(result,"회원가입 실패, 아이디,이메일을 다시 확인하세요.");
        }
        return result == -1L ?
                new ResResultDto(result,"사업자 등록 실패.") : new ResResultDto(result,"사업자 등록 성공.");
    }
}
