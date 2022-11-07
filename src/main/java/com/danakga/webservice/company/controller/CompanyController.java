package com.danakga.webservice.company.controller;

import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.company.dto.request.PasswordDto;
import com.danakga.webservice.company.dto.response.ResCompanyInfoDto;
import com.danakga.webservice.company.dto.request.CompanyInfoDto;
import com.danakga.webservice.company.service.CompanyService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/manager")
public class CompanyController {

    private final CompanyService companyService;

    //사업자 탈퇴
    @PutMapping("/deleted")
    public ResResultDto companyDeleted(@LoginUser UserInfo userInfo,@RequestBody PasswordDto password){

        Long result = companyService.companyDeleted(userInfo,password.getPassword());
        if(result == -2L){
            return new ResResultDto(result,"사업자 탈퇴 실패, 비밀번호 확인 오류");
        }
        else if(result == -1L){
            return new ResResultDto(result,"사업자 탈퇴 실패.");
        }
        else{
            return new ResResultDto(result,"사업자 탈퇴 성공.");
        }
    }
    
    //사업자 정보 수정
    @PutMapping("/update")
    public ResResultDto companyUpdate(@LoginUser UserInfo userInfo,@RequestBody CompanyInfoDto companyInfoDto){
        Long result = companyService.companyUpdate(userInfo,companyInfoDto);
        //회사명 중복 체크
        if(companyService.companyNameCheck(companyInfoDto.getCompanyName()).equals(-1)){
            return new ResResultDto(-2L,"사업자 등록 실패, 이미 사용되고있는 회사명입니다");
        }
        return result == -1L ?
                new ResResultDto(result,"회사정보 수정 실패.") : new ResResultDto(result,"회사정보 수정 성공.");
    }
    
    //사업자 회사 정보 조회
    @GetMapping("")
    public ResCompanyInfoDto companyInfoCheck(@LoginUser UserInfo userInfo){
        return new ResCompanyInfoDto(companyService.companyInfoCheck(userInfo));
    }






}
