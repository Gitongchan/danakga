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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired private final CompanyRepository companyRepository;
    @Autowired private final UserRepository userRepository;
    @Autowired private final UserService userService;

    //사업자탈퇴a
    @Override
    public Long companyDeleted(UserInfo userInfo, CompanyInfoDto companyInfoDto) {
        return null;
    }

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

        companyUserInfoDto.setRole("ROLE_MANAGER");
        companyUserInfoDto.setCompanyEnabled(true);

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

}
