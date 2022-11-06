package com.danakga.webservice.admin.controller;

import com.danakga.webservice.admin.dto.response.ResManagerInfoDto;
import com.danakga.webservice.admin.dto.response.ResUserInfoDto;
import com.danakga.webservice.admin.service.AdminService;
import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.model.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping(value="/admin")
@RestController
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/members/userList")
    public List<ResUserInfoDto> userList(@LoginUser UserInfo userInfo,
                       @RequestParam String userEnabled,
                       @RequestParam String searchRequirements , @RequestParam String searchWord,
                       @RequestParam String sortMethod, @RequestParam String sortBy,
                       Pageable pageable ,
                       @RequestParam int page){

        return adminService.findUserInfoList(userInfo,UserRole.ROLE_USER,userEnabled,searchRequirements,searchWord,sortMethod,sortBy,pageable ,page);
    }

    @GetMapping("/members/managerList")
    public List<ResManagerInfoDto> managerList(@LoginUser UserInfo userInfo,
                                        @RequestParam String userEnabled,
                                        @RequestParam String searchRequirements , @RequestParam String searchWord,
                                        @RequestParam String sortMethod, @RequestParam String sortBy,
                                        Pageable pageable ,
                                        @RequestParam int page){

        return adminService.findManagerInfoList(userInfo,UserRole.ROLE_MANAGER,userEnabled,searchRequirements,searchWord,sortMethod,sortBy,pageable ,page);
    }
}
