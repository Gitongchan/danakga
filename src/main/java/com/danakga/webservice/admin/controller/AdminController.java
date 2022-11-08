package com.danakga.webservice.admin.controller;

import com.danakga.webservice.admin.dto.response.ResManagerInfoDetailDto;
import com.danakga.webservice.admin.dto.response.ResManagerInfoDto;
import com.danakga.webservice.admin.dto.response.ResUserInfoDetailDto;
import com.danakga.webservice.admin.dto.response.ResUserInfoDto;
import com.danakga.webservice.admin.service.AdminService;
import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.model.UserRole;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping(value="/admin")
@RestController
public class AdminController {

    private final AdminService adminService;

    //일반 사용자 목록
    @GetMapping("/members/userList")
    public List<ResUserInfoDto> userList(@LoginUser UserInfo userInfo,
                       @RequestParam String userEnabled,
                       @RequestParam String searchRequirements , @RequestParam String searchWord,
                       @RequestParam String sortMethod, @RequestParam String sortBy,
                       Pageable pageable ,
                       @RequestParam int page){

        return adminService.findUserInfoList(userInfo,UserRole.ROLE_USER,userEnabled,searchRequirements,searchWord,sortMethod,sortBy,pageable ,page);
    }

    //사업자 목록
    @GetMapping("/members/managerList")
    public List<ResManagerInfoDto> managerList(@LoginUser UserInfo userInfo,
                                        @RequestParam String userEnabled, @RequestParam String companyEnabled,
                                        @RequestParam String searchRequirements , @RequestParam String searchWord,
                                        @RequestParam String sortMethod, @RequestParam String sortBy,
                                        Pageable pageable ,
                                        @RequestParam int page){

        return adminService.findManagerInfoList(userInfo,UserRole.ROLE_MANAGER,userEnabled,companyEnabled,searchRequirements,searchWord,sortMethod,sortBy,pageable ,page);
    }
    
    //일반 사용자 상세 정보
    @GetMapping("/members/user/{userId}")
    public ResUserInfoDetailDto userInfoDetail(@LoginUser UserInfo userInfo,
                                               @PathVariable String userId){
      return adminService.findUserInfoDetail(userInfo,userId);
    }

    //사업자 ,회사 상세 정보
    @GetMapping("/members/manager/{companyName}")
    public ResManagerInfoDetailDto managerInfoDetail(@LoginUser UserInfo userInfo,
                                                     @PathVariable String companyName){
        return adminService.findManagerInfoDetail(userInfo,companyName);
    }

    //회원 이용 정지
    @PutMapping("/members/user/{userId}")
    public ResResultDto stopUsing(@LoginUser UserInfo userInfo , @PathVariable String userId){

        Long result = adminService.stopUsing(userInfo,userId);
        return new ResResultDto(result,userId + " 회원을 이용 정지 시켰습니다" );
    }

    //사업자 이용 정지
    @PutMapping("/members/manager/{companyName}")
    public ResResultDto stopUsingManager(@LoginUser UserInfo userInfo , @PathVariable String companyName){
        Long result = adminService.stopUsingManager(userInfo,companyName);
        return new ResResultDto(result,companyName + " 상점을 정지 시켰습니다" );
    }

    //회원 삭제
    @DeleteMapping("/members/user/{userId}")
    public ResResultDto deleteMember(@LoginUser UserInfo userInfo , @PathVariable String userId){
        Long result = adminService.deleteMember(userInfo,userId);
        return new ResResultDto(result,userId + " 회원을 삭제했습니다." );
    }

    //회사 정보 삭제
    @DeleteMapping("/members/manager/{companyName}")
    public ResResultDto deleteManager(@LoginUser UserInfo userInfo,@PathVariable String companyName){
        Long result = adminService.deleteManager(userInfo,companyName);
        return new ResResultDto(result,companyName + " 상점을 삭제했습니다.");
    }
}
