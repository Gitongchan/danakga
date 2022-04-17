package com.danakga.webservice.company.service.Impl;

import com.danakga.webservice.company.dto.request.CompanyUserInfoDto;
import com.danakga.webservice.company.service.CompanyService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private final UserRepository userRepository;
    //사업자 탈퇴

    @Override
    public Long companyDeleted(UserInfo userInfo, CompanyUserInfoDto companyUserInfoDto) {
        if(userRepository.findById(userInfo.getId()).isPresent()&& userInfo.getRole().equals("ROLE_MANAGER")){

            companyUserInfoDto.setCompanyEnabled(false);
            companyUserInfoDto.setRole("ROLE_USER");

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
                            //사업자 등록으로 받은 정보만 user_info로 업데이트
                            .role(companyUserInfoDto.getRole())
                            .companyId(companyUserInfoDto.getCompanyId())
                            .companyName(companyUserInfoDto.getCompanyName())
                            .companyNum(companyUserInfoDto.getCompanyNum())
                            .companyAdrNum(companyUserInfoDto.getCompanyAdrNum())
                            .companyDefNum(companyUserInfoDto.getCompanyDefNum())
                            .companyDetailAdr(companyUserInfoDto.getCompanyDetailAdr())
                            .companyBanknum(companyUserInfoDto.getCompanyBanknum())
                            .companyEnabled(companyUserInfoDto.isCompanyEnabled())
                            .companyDeltedDate(companyUserInfoDto.getCompanyDeletedDate())
                            .build()
            );
        }
        return null;
    }

}
