package com.danakga.webservice.admin.service.Impl;


import com.danakga.webservice.admin.dto.response.ResManagerInfoDetailDto;
import com.danakga.webservice.admin.dto.response.ResManagerInfoDto;
import com.danakga.webservice.admin.dto.response.ResUserInfoDetailDto;
import com.danakga.webservice.admin.dto.response.ResUserInfoDto;
import com.danakga.webservice.admin.service.AdminService;
import com.danakga.webservice.company.model.CompanyInfo;
import com.danakga.webservice.company.repository.CompanyRepository;
import com.danakga.webservice.exception.CustomException;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.model.UserRole;
import com.danakga.webservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    
    //일반 사용자 목록
    @Override
    public List<ResUserInfoDto> findUserInfoList(UserInfo userInfo, UserRole userRole,String userEnabled ,String searchRequirements , String searchWord,
                                                 String sortMethod, String sortBy,Pageable pageable ,int page) {
        userRepository.findByIdAndRole(userInfo.getId(), UserRole.ROLE_ADMIN).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("어드민 사용자를 찾을 수 없습니다.")
        );

        String userEmail = "", userName = "", userPhone = "", userId = "";

        switch (searchRequirements) {
            case "userEmail":
                userEmail = searchWord;
                break;
            case "userName":
                userName = searchWord;
                break;
            case "userPhone":
                userPhone = searchWord;
                break;
            case "userId":
                userId = searchWord;
                break;
        }


        if(sortMethod.equals("desc")){
            pageable = PageRequest.of(page, 50, Sort.by(sortBy).descending());
        }
        else if(sortMethod.equals("asc")){
            pageable = PageRequest.of(page, 50, Sort.by(sortBy).ascending());
        }

        Page<UserInfo> userInfoPage = userRepository.findAllUserInfo(userRole,userName,userEmail,userPhone,userId,Boolean.parseBoolean(userEnabled),pageable);

        List<UserInfo> userInfoList = userInfoPage.getContent();

        List<ResUserInfoDto> userInfoListDto = new ArrayList<>();

        userInfoList.forEach(entity->{
                ResUserInfoDto listDto = new ResUserInfoDto();
                listDto.setUserid(entity.getUserid());
                listDto.setEmail(entity.getEmail());
                listDto.setName(entity.getName());
                listDto.setPhone(entity.getPhone());
                listDto.setUserEnabled(entity.isUserEnabled());
                listDto.setUserDeletedDate(entity.getUserDeletedDate());
                userInfoListDto.add(listDto);
        });

        return userInfoListDto;
    }

    //사업자 목록 조회
    @Override
    public List<ResManagerInfoDto> findManagerInfoList(UserInfo userInfo, UserRole userRole, String userEnabled,String companyEnabled, String searchRequirements, String searchWord, String sortMethod, String sortBy, Pageable pageable, int page) {
        userRepository.findByIdAndRole(userInfo.getId(), UserRole.ROLE_ADMIN).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("어드민 사용자를 찾을 수 없습니다.")
        );


        String userName = "", companyName = "", companyNum = "", userId = "";

        switch (searchRequirements) {
            case "userName":
                userName = searchWord;
                break;
            case "userId":
                userId = searchWord;
                break;
            case "companyName":
                companyName = searchWord;
                break;
            case "companyNum":
                companyNum = searchWord;
                break;
        }

        if(sortMethod.equals("desc")){
            pageable = PageRequest.of(page, 50, Sort.by(sortBy).descending());
        }
        else if(sortMethod.equals("asc")){
            pageable = PageRequest.of(page, 50, Sort.by(sortBy).ascending());
        }

        Page<CompanyInfo> companyInfoPage = companyRepository.findAllManagerInfo(userRole,userName,userId,Boolean.parseBoolean(userEnabled),Boolean.parseBoolean(companyEnabled),companyName,companyNum,pageable);

        List<CompanyInfo> companyInfoList = companyInfoPage.getContent();

        List<ResManagerInfoDto> companyInfoListDto = new ArrayList<>();

        companyInfoList.forEach(entity->{
            ResManagerInfoDto listDto = new ResManagerInfoDto();
            listDto.setUserid(entity.getUserInfo().getUserid());
            listDto.setEmail(entity.getUserInfo().getEmail());
            listDto.setName(entity.getUserInfo().getName());
            listDto.setPhone(entity.getUserInfo().getPhone());
            listDto.setUserEnabled(entity.getUserInfo().isUserEnabled());
            listDto.setUserDeletedDate(entity.getUserInfo().getUserDeletedDate());
            listDto.setCompanyId(entity.getCompanyId());
            listDto.setCompanyName(entity.getCompanyName());
            listDto.setCompanyNum(entity.getCompanyNum());
            listDto.setCompanyDeletedDate(entity.getCompanyDeletedDate());
            companyInfoListDto.add(listDto);
        });
        return companyInfoListDto;
    }

    //일반 사용자 상세 조회
    @Override
    public ResUserInfoDetailDto findUserInfoDetail(UserInfo userInfo, String userId) {
        userRepository.findByIdAndRole(userInfo.getId(), UserRole.ROLE_ADMIN).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("어드민 사용자를 찾을 수 없습니다.")
        );
        UserInfo checkUserInfo = userRepository.findByUseridAndRole(userId,UserRole.ROLE_USER).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("사용자를 찾을 수 없습니다.")
        );
        return new ResUserInfoDetailDto(checkUserInfo);
    }

    //사업자 상세 조회
    @Override
    public ResManagerInfoDetailDto findManagerInfoDetail(UserInfo userInfo, String companyName) {
        userRepository.findByIdAndRole(userInfo.getId(), UserRole.ROLE_ADMIN).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("어드민 사용자를 찾을 수 없습니다.")
        );
        CompanyInfo checkCompanyInfo = companyRepository.findByCompanyName(companyName).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("사업자 정보를 찾을 수 없습니다.")
        );


        return new ResManagerInfoDetailDto(checkCompanyInfo);
    }

}
