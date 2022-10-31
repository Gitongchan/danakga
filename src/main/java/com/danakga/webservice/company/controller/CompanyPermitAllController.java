package com.danakga.webservice.company.controller;

import com.danakga.webservice.company.dto.request.CompanyUserInfoDto;
import com.danakga.webservice.company.dto.response.ResProductByCompanyDto;
import com.danakga.webservice.company.service.CompanyService;
import com.danakga.webservice.user.dto.response.ResDupliCheckDto;
import com.danakga.webservice.user.service.UserService;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/company")
public class CompanyPermitAllController {
    private final CompanyService companyService;
    private final UserService userService;

    //업체명 중복 체크
    @GetMapping("/name_check")
    public ResDupliCheckDto companyNameCheck(@RequestParam("companyName") String companyName){
        return new ResDupliCheckDto(companyService.companyNameCheck(companyName));
    }
    //사업자 회원가입
    @PostMapping("/signup")
    public ResResultDto companyRegister(@RequestBody CompanyUserInfoDto companyUserInfoDto){

        Integer idCheckResult = userService.userIdCheck(companyUserInfoDto.getUserid());
        Integer emailCheckResult = userService.emailCheck(companyUserInfoDto.getEmail());

        //중복 id,email 검증
        if(idCheckResult.equals(-1)||emailCheckResult.equals(-1)) {
            return new ResResultDto(-1L,"회원가입 실패, 아이디,이메일을 다시 확인하세요.");
        }else{
            Long result = companyService.companyRegister(companyUserInfoDto);
            return result == -1L ?
                    new ResResultDto(result,"사업자 등록 실패.") : new ResResultDto(result,"사업자 등록 성공.");
        }
    }

    //상점 품목 조회 - 일반사용자 측에서
    @GetMapping("/inquire/{companyName}")
    public ResProductByCompanyDto productByCompany(@PathVariable("companyName") String companyName,
                                                   @RequestParam String sortBy,@RequestParam String sortMethod,
                                                   @RequestParam String productName,@RequestParam int productStock,
                                                   Pageable pageable,
                                                   int page
    ){
        return companyService.productByCompanyDto(companyName,sortBy,sortMethod,productName,productStock,pageable,page);
    }
}
