package com.danakga.webservice.company.service.Impl;

import com.danakga.webservice.company.dto.request.CompanyInfoDto;
import com.danakga.webservice.company.dto.request.CompanyUserInfoDto;
import com.danakga.webservice.company.model.CompanyInfo;
import com.danakga.webservice.company.repository.CompanyRepository;
import com.danakga.webservice.company.service.CompanyService;
import com.danakga.webservice.user.dto.request.UserInfoDto;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.repository.UserRepository;
import com.danakga.webservice.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanyServiceImpl implements CompanyService {
    @Autowired private final CompanyRepository companyRepository;
    @Autowired private final UserRepository userRepository;
    @Autowired private final UserService userService;

    //업체명 중복 체크
    @Override
    public Integer companyNameCheck(String companyName) {
        if (companyRepository.findByCompanyName(companyName).isPresent()) {
            return -1; //같은 이메일 존재할 때
        }
        return 1; // 같은 이메일 없을 때
    }

    //사업자 회원 등록
    @Override
    public Long companyRegister(CompanyUserInfoDto companyUserInfoDto) {


        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String rawPassword = companyUserInfoDto.getPassword();
        companyUserInfoDto.setPassword(bCryptPasswordEncoder.encode(rawPassword));

        //가입시 자동 설정
        companyUserInfoDto.setRole("ROLE_MANAGER");
        companyUserInfoDto.setCompanyEnabled(true);
        companyUserInfoDto.setUserEnabled(true);

        //중복 id,email 검증
        Integer idCheckResult = userService.userIdCheck(companyUserInfoDto.getUserid());
        Integer emailCheckResult = userService.emailCheck(companyUserInfoDto.getEmail());
        if(idCheckResult.equals(-1)||emailCheckResult.equals(-1)) {
            return -1L;
        }
        else{
            System.out.println("실행됨");
            UserInfo singUpUserInfo =
                    userRepository.save(
                            UserInfo.builder()
                                    .userid(companyUserInfoDto.getUserid())
                                    .password(companyUserInfoDto.getPassword())
                                    .name(companyUserInfoDto.getName())
                                    .phone(companyUserInfoDto.getPhone())
                                    .email(companyUserInfoDto.getEmail())
                                    .role(companyUserInfoDto.getRole())
                                    .userAdrNum(companyUserInfoDto.getUserAdrNum())
                                    .userDefAdr(companyUserInfoDto.getUserDefAdr())
                                    .userDetailAdr(companyUserInfoDto.getUserDetailAdr())
                                    .userEnabled(companyUserInfoDto.isUserEnabled())
                                    .build()
                    );

            companyRepository.save(
                    CompanyInfo.builder()
                            .companyId(companyUserInfoDto.getCompanyId())
                            .userInfo(singUpUserInfo)
                            .companyName(companyUserInfoDto.getCompanyName())
                            .companyNum(companyUserInfoDto.getCompanyNum())
                            .companyAdrNum(companyUserInfoDto.getCompanyAdrNum())
                            .companyDefNum(companyUserInfoDto.getCompanyDefNum())
                            .companyDetailAdr(companyUserInfoDto.getCompanyDetailAdr())
                            .companyBanknum(companyUserInfoDto.getCompanyBanknum())
                            .companyEnabled(companyUserInfoDto.isCompanyEnabled())
                            .build()
            );
            return singUpUserInfo.getId();
        }
    }

    //사업자 정보 수정
    @Override
    public Long companyUpdate(UserInfo userInfo, CompanyInfoDto companyInfoDto) {
        CompanyInfo updateCompanyInfo = companyRepository.findByUserInfo(userInfo).orElseGet(
                ()->CompanyInfo.builder().build()
        );
        companyRepository.save(
                CompanyInfo.builder()
                        .companyId(updateCompanyInfo.getCompanyId())
                        .userInfo(userInfo)
                        .companyName(companyInfoDto.getCompanyName())
                        .companyNum(companyInfoDto.getCompanyNum())
                        .companyAdrNum(companyInfoDto.getCompanyAdrNum())
                        .companyDefNum(companyInfoDto.getCompanyDefNum())
                        .companyDetailAdr(companyInfoDto.getCompanyDetailAdr())
                        .companyBanknum(companyInfoDto.getCompanyBanknum())
                        .companyEnabled(updateCompanyInfo.isCompanyEnabled())
                        .build()
        );
        return updateCompanyInfo.getCompanyId();
    }

    //사업자탈퇴
    @Override
    public Long companyDeleted(UserInfo userInfo, CompanyUserInfoDto companyUserInfoDto) {

        if(userRepository.findById(userInfo.getId()).isPresent()&& userInfo.getRole().equals("ROLE_MANAGER")) {

            companyUserInfoDto.setCompanyEnabled(false);
            companyUserInfoDto.setRole("ROLE_USER");

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            List<GrantedAuthority> updatedAuthorities = new ArrayList<>(auth.getAuthorities());
            updatedAuthorities.clear();
            updatedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

            Authentication newAuth = new UsernamePasswordAuthenticationToken(
                    auth.getPrincipal(), auth.getCredentials(),updatedAuthorities
            );
            SecurityContextHolder.getContext().setAuthentication(newAuth);

            System.out.println("newAuth.getAuthorities() = " + newAuth.getAuthorities());

            System.out.println(userInfo.getRole()+" -- 현재 권한 ");
            System.out.println(userInfo.getAuthorities() + "현재권한 getAuthor");

            userRepository.save(
                    UserInfo.builder()
                            .id(userInfo.getId()) //로그인 유저 키값을 받아옴
                            //유저의 정보는 그대로 유지
                            .userid(userInfo.getUserid())
                            .password(userInfo.getPassword())
                            .name(userInfo.getName())
                            .phone(userInfo.getPhone())
                            .email(userInfo.getEmail())
                            .userAdrNum(userInfo.getUserAdrNum())
                            .userDefAdr(userInfo.getUserDefAdr())
                            .userDetailAdr(userInfo.getUserDetailAdr())
                            .userEnabled(userInfo.isUserEnabled())
                            //권한만 변경
                            .role(companyUserInfoDto.getRole())
                            .build()
            );

            CompanyInfo deleteCompanyInfo = companyRepository.findByUserInfo(userInfo).orElseGet(
                    ()->CompanyInfo.builder().build()
            );
            companyRepository.save(
                    CompanyInfo.builder()
                            .companyId(deleteCompanyInfo.getCompanyId())
                            .userInfo(userInfo)
                            .companyName(deleteCompanyInfo.getCompanyName())
                            .companyNum(deleteCompanyInfo.getCompanyNum())
                            .companyAdrNum(deleteCompanyInfo.getCompanyAdrNum())
                            .companyDefNum(deleteCompanyInfo.getCompanyDefNum())
                            .companyDetailAdr(deleteCompanyInfo.getCompanyDetailAdr())
                            .companyBanknum(deleteCompanyInfo.getCompanyBanknum())
                            .companyEnabled(companyUserInfoDto.isCompanyEnabled())
                            .companyDeltedDate(LocalDateTime.now())
                            .build()

            );
            return userInfo.getId();
        }
        return -1L;
    }
}
