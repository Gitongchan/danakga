package com.danakga.webservice.user.controller;

import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.company.dto.request.CompanyInfoDto;
import com.danakga.webservice.company.service.CompanyService;
import com.danakga.webservice.user.dto.request.UserInfoDto;
import com.danakga.webservice.user.dto.response.ResUserInfoDto;
import com.danakga.webservice.util.responseDto.ResResultDto;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController{
    @Autowired private final UserService userService;
    @Autowired private final CompanyService companyService;

    /**              마이페이지 기능               **/
    //회원정보 조회
    @GetMapping("")
    public ResUserInfoDto check(@LoginUser UserInfo userInfo){
        return new ResUserInfoDto(userService.userInfoCheck(userInfo));
    }

    //회원정보 수정
    @PutMapping("")
    public ResResultDto update(@LoginUser UserInfo userInfo, @Valid @RequestBody UserInfoDto userInfoDto){

        System.out.println("userInfo = " + userInfo.getName());
        System.out.println("userInfoDto = " + userInfoDto.getName());
        Long result = userService.update(userInfo,userInfoDto);
        return result == -1L ?
                new ResResultDto(result,"회원정보 변경 실패.") : new ResResultDto(result,"회원정보 변경 성공.");
    }

    //회원 탈퇴
    @PutMapping("/user_deleted")
    public ResResultDto userDeleted(@LoginUser UserInfo userInfo, @RequestBody UserInfoDto userInfoDto){
        Long result = userService.userDeleted(userInfo,userInfoDto);

        if(result.equals(-1L)){
            return new ResResultDto(result,"회원 탈퇴 실패");
        }
        else if(result.equals(-2L)){
            return new ResResultDto(result,"일반회원만 탈퇴할 수 있습니다");
        }
        else return new ResResultDto(result,"회원 탈퇴 성공");

    }

    //사업자 회원 가입
    @PostMapping("/company_register")
    public ResResultDto companyRegister(@LoginUser UserInfo userInfo ,
                                        UserInfoDto userInfoDto, @RequestBody CompanyInfoDto companyInfoDto){

        //회사명 중복 체크
        if(companyService.companyNameCheck(companyInfoDto.getCompanyName()).equals(-1)){
            return new ResResultDto(-3L,"사업자 등록 실패, 이미 사용되고있는 회사명입니다");
        }

        Long result = userService.companyRegister(userInfo,userInfoDto,companyInfoDto);

        if(result == -1L) {
            return new ResResultDto(result,"사업자 등록 실패, 유저 정보를 받아올 수 없습니다.");
        }
        else if(result == -2L) {
            return new ResResultDto(result,"사업자 등록 실패, 이미 사업자로 등록되었습니다.");
        }
        else{
            return new ResResultDto(result,"사업자 등록 성공.");
        }

    }


}
