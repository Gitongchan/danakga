package com.danakga.webservice.company.service.Impl;

import com.danakga.webservice.company.dto.request.CompanyInfoDto;
import com.danakga.webservice.company.dto.request.CompanyUserInfoDto;
import com.danakga.webservice.company.model.CompanyInfo;
import com.danakga.webservice.company.repository.CompanyRepository;
import com.danakga.webservice.company.service.CompanyService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.model.UserRole;
import com.danakga.webservice.user.repository.UserRepository;
import com.danakga.webservice.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanyServiceImpl implements CompanyService {
    @Autowired private final CompanyRepository companyRepository;
    @Autowired private final UserRepository userRepository;

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

            System.out.println("실행됨");
            UserInfo singUpUserInfo =
                    userRepository.save(
                            UserInfo.builder()
                                    .userid(companyUserInfoDto.getUserid())
                                    .password(companyUserInfoDto.getPassword())
                                    .name(companyUserInfoDto.getName())
                                    .phone(companyUserInfoDto.getPhone())
                                    .email(companyUserInfoDto.getEmail())
                                    .role(UserRole.ROLE_MANAGER)//가입시 자동 설정
                                    .userAdrNum(companyUserInfoDto.getUserAdrNum())
                                    .userStreetAdr(companyUserInfoDto.getUserStreetAdr())
                                    .userLotAdr(companyUserInfoDto.getUserLotAdr())
                                    .userDetailAdr(companyUserInfoDto.getUserDetailAdr())
                                    .userEnabled(true)//가입시 자동 설정
                                    .build()
                    );

            companyRepository.save(
                    CompanyInfo.builder()
                            .companyId(companyUserInfoDto.getCompanyId())
                            .userInfo(singUpUserInfo)
                            .companyName(companyUserInfoDto.getCompanyName())
                            .companyNum(companyUserInfoDto.getCompanyNum())
                            .companyAdrNum(companyUserInfoDto.getCompanyAdrNum())
                            .companyLotAdr(companyUserInfoDto.getCompanyLotAdr())
                            .companyStreetAdr(companyUserInfoDto.getCompanyStreetAdr())
                            .companyDetailAdr(companyUserInfoDto.getCompanyDetailAdr())
                            .companyBanknum(companyUserInfoDto.getCompanyBanknum())
                            .companyEnabled(true)
                            .build()
            );
            return singUpUserInfo.getId();

    }

    //사업자 정보 수정
    @Override
    public Long companyUpdate(UserInfo userInfo, CompanyInfoDto companyInfoDto) {
        if(companyRepository.findByUserInfo(userInfo).isEmpty()){
            return -1L;
        }
            CompanyInfo updateCompanyInfo = companyRepository.findByUserInfo(userInfo).orElseGet(
                    ()->CompanyInfo.builder().build()
            );
            companyRepository.save(
                    CompanyInfo.builder()
                            .companyId(updateCompanyInfo.getCompanyId())
                            .userInfo(updateCompanyInfo.getUserInfo())
                            .companyName(companyInfoDto.getCompanyName())
                            .companyNum(companyInfoDto.getCompanyNum())
                            .companyAdrNum(companyInfoDto.getCompanyAdrNum())
                            .companyLotAdr(companyInfoDto.getCompanyLotAdr())
                            .companyStreetAdr(companyInfoDto.getCompanyStreetAdr())
                            .companyDetailAdr(companyInfoDto.getCompanyDetailAdr())
                            .companyBanknum(companyInfoDto.getCompanyBanknum())
                            .companyEnabled(updateCompanyInfo.isCompanyEnabled())
                            .build()
            );
            return updateCompanyInfo.getCompanyId();
    }

    //사업자 회사 정보 조회
    @Override
    public CompanyInfo companyInfoCheck(UserInfo userInfo) {
        if(companyRepository.findByUserInfo(userInfo).isPresent()){
            return companyRepository.findByUserInfo(userInfo).get();
        }
        return null;
    }

    //사업자탈퇴
    @Override
    public Long companyDeleted(UserInfo userInfo,String password) {

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        if(userRepository.findById(userInfo.getId()).isEmpty()){
            return -1L;
        }
        UserInfo comUserInfo = userRepository.findById(userInfo.getId()).get();

        if(!comUserInfo.getRole().equals(UserRole.ROLE_MANAGER)){
            return -1L; //사업자가 아니면
        }

        if(!bCryptPasswordEncoder.matches(password,comUserInfo.getPassword())) {
            return -2L; //비밀번호 확인 실패시
        }

            userRepository.save(
                    UserInfo.builder()
                            .id(comUserInfo.getId()) //로그인 유저 키값을 받아옴
                            //유저의 정보는 그대로 유지
                            .userid(comUserInfo.getUserid())
                            .password(comUserInfo.getPassword())
                            .name(comUserInfo.getName())
                            .phone(comUserInfo.getPhone())
                            .email(comUserInfo.getEmail())
                            .userAdrNum(comUserInfo.getUserAdrNum())
                            .userLotAdr(comUserInfo.getUserLotAdr())
                            .userStreetAdr(comUserInfo.getUserStreetAdr())
                            .userDetailAdr(comUserInfo.getUserDetailAdr())
                            .userEnabled(comUserInfo.isUserEnabled())
                            .role(UserRole.ROLE_USER)
                            .build()
            );

            CompanyInfo deleteCompanyInfo = companyRepository.findByUserInfo(comUserInfo).orElseGet(
                    ()->CompanyInfo.builder().build()
            );
            companyRepository.save(
                    CompanyInfo.builder()
                            .companyId(deleteCompanyInfo.getCompanyId())
                            .userInfo(comUserInfo)
                            .companyName(deleteCompanyInfo.getCompanyName())
                            .companyNum(deleteCompanyInfo.getCompanyNum())
                            .companyAdrNum(deleteCompanyInfo.getCompanyAdrNum())
                            .companyLotAdr(deleteCompanyInfo.getCompanyLotAdr())
                            .companyStreetAdr(deleteCompanyInfo.getCompanyStreetAdr())
                            .companyDetailAdr(deleteCompanyInfo.getCompanyDetailAdr())
                            .companyBanknum(deleteCompanyInfo.getCompanyBanknum())
                            .companyEnabled(false)
                            .companyDeltedDate(LocalDateTime.now())
                            .build()

            );
            return comUserInfo.getId();
    }
}
