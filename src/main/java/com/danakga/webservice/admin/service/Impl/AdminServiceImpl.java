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

import javax.transaction.Transactional;
import java.time.LocalDateTime;
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
            listDto.setCompanyEnabled(entity.isCompanyEnabled());
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

    //회원 정지 - 로그인 불가
    @Transactional
    @Override
    public Long stopUsing(UserInfo userInfo, String userId) {
        userRepository.findByIdAndRole(userInfo.getId(), UserRole.ROLE_ADMIN).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("어드민 사용자를 찾을 수 없습니다.")
        );
        UserInfo checkUserInfo = userRepository.findByUserid(userId).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("삭제할 사용자를 찾을 수 없습니다.")
        );
        
        //정지할 사용자가 사업자면 사업자 이용 중지 같이해줌
        if(checkUserInfo.getRole().equals(UserRole.ROLE_MANAGER)){
            //일반 사용자로 권한 변경
            userRepository.updateUserRole(UserRole.ROLE_USER,checkUserInfo.getId());

            CompanyInfo deleteCompanyInfo = companyRepository.findByUserInfo(checkUserInfo).orElseThrow(
                    ()-> new CustomException.ResourceNotFoundException("사업자 정보를 찾을 수 없습니다.")
            );

            companyRepository.updateCompanyEnabled(false,LocalDateTime.now(),deleteCompanyInfo.getCompanyId());
        }

        userRepository.save(
                UserInfo.builder()
                        .id(checkUserInfo.getId()) //로그인 유저 키값을 받아옴
                        .userid(checkUserInfo.getUserid()) //그대로 유지
                        .password(checkUserInfo.getPassword())
                        .name(checkUserInfo.getName())
                        .phone(checkUserInfo.getPhone())
                        .email(checkUserInfo.getEmail())
                        .role(checkUserInfo.getRole())
                        .userAdrNum(checkUserInfo.getUserAdrNum())
                        .userStreetAdr(checkUserInfo.getUserStreetAdr())
                        .userLotAdr(checkUserInfo.getUserLotAdr())
                        .userDetailAdr(checkUserInfo.getUserDetailAdr())
                        .userDeletedDate(LocalDateTime.now()) //현재시간
                        .userEnabled(false)//사용자 이용 중지
                        .build()
        );
        return userInfo.getId();
    }

    //사업자 정지
    @Transactional
    @Override
    public Long stopUsingManager(UserInfo userInfo, String companyName) {
        userRepository.findByIdAndRole(userInfo.getId(), UserRole.ROLE_ADMIN).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("어드민 사용자를 찾을 수 없습니다.")
        );
        CompanyInfo checkCompanyInfo = companyRepository.findByCompanyName(companyName).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("사업자를 찾을 수 없습니다.")
        );
        //일반 사용자로 권한 변경
        userRepository.updateUserRole(UserRole.ROLE_USER,checkCompanyInfo.getUserInfo().getId());

        CompanyInfo deleteCompanyInfo = companyRepository.findByUserInfo(checkCompanyInfo.getUserInfo()).orElseThrow(
                ()-> new CustomException.ResourceNotFoundException("사업자 정보를 찾을 수 없습니다.")
        );

        companyRepository.updateCompanyEnabled(false,LocalDateTime.now(),deleteCompanyInfo.getCompanyId());

        return checkCompanyInfo.getCompanyId();
    }

    //회원 정보 삭제
    @Transactional
    @Override
    public Long deleteMember(UserInfo userInfo, String userId) {
        userRepository.findByIdAndRole(userInfo.getId(), UserRole.ROLE_ADMIN).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("어드민 사용자를 찾을 수 없습니다.")
        );
        
        //정지되거나 탈퇴된 사용자만 삭제 가능
        UserInfo deleteUserInfo = userRepository.findByUseridAndUserEnabled(userId,false).orElseThrow(
                ()-> new CustomException.ResourceNotFoundException("삭제 할 사용자를 찾을 수 없거나,정지 또는 탈퇴한 사용자가 아닙니다.")
        );

        userRepository.deleteById(deleteUserInfo.getId());

        return 1L;
    }

    //사업자 정보 삭제 - 회사 정보 삭제
    @Transactional
    @Override
    public Long deleteManager(UserInfo userInfo, String companyName) {
        userRepository.findByIdAndRole(userInfo.getId(), UserRole.ROLE_ADMIN).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("어드민 사용자를 찾을 수 없습니다.")
        );

        CompanyInfo deleteCompanyInfo = companyRepository.findByCompanyNameAndCompanyEnabled(companyName,false).orElseThrow(
                ()-> new CustomException.ResourceNotFoundException("삭제 할 상점을 찾을 수 없거나,정지 또는 탈퇴한 상점이 아닙니다.")
        );

        companyRepository.deleteById(deleteCompanyInfo.getCompanyId());

        return 1L;
    }

    @Override
    public Long restoreUser(UserInfo userInfo, String userId) {
        userRepository.findByIdAndRole(userInfo.getId(), UserRole.ROLE_ADMIN).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("어드민 사용자를 찾을 수 없습니다.")
        );
        UserInfo restoreUserInfo = userRepository.findByUseridAndUserEnabled(userId,false).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("탈퇴하거나 정지된 사용자를 찾을 수 없습니다.")
        );
        userRepository.save(
                UserInfo.builder()
                        .id(restoreUserInfo.getId())
                        .userid(restoreUserInfo.getUserid())
                        .password(restoreUserInfo.getPassword())
                        .name(restoreUserInfo.getName())
                        .phone(restoreUserInfo.getPhone())
                        .email(restoreUserInfo.getEmail())
                        .role(UserRole.ROLE_USER)
                        .userAdrNum(restoreUserInfo.getUserAdrNum())
                        .userStreetAdr(restoreUserInfo.getUserStreetAdr())
                        .userLotAdr(restoreUserInfo.getUserLotAdr())
                        .userDetailAdr(restoreUserInfo.getUserDetailAdr())
                        .userEnabled(true)
                        .userDeletedDate(null)
                        .build()
        );


        return restoreUserInfo.getId();
    }

    @Override
    public Long restoreManager(UserInfo userInfo, String companyName) {
        userRepository.findByIdAndRole(userInfo.getId(), UserRole.ROLE_ADMIN).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("어드민 사용자를 찾을 수 없습니다.")
        );

        CompanyInfo restoreCompanyInfo = companyRepository.findByCompanyNameAndCompanyEnabled(companyName,false).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("탈퇴하거나 정지된 사업자 정보를 찾을 수 없습니다.")
        );

        userRepository.save(
                UserInfo.builder()
                        .id(restoreCompanyInfo.getUserInfo().getId())
                        .userid(restoreCompanyInfo.getUserInfo().getUserid())
                        .password(restoreCompanyInfo.getUserInfo().getPassword())
                        .name(restoreCompanyInfo.getUserInfo().getName())
                        .phone(restoreCompanyInfo.getUserInfo().getPhone())
                        .email(restoreCompanyInfo.getUserInfo().getEmail())
                        .role(UserRole.ROLE_MANAGER)
                        .userAdrNum(restoreCompanyInfo.getUserInfo().getUserAdrNum())
                        .userStreetAdr(restoreCompanyInfo.getUserInfo().getUserStreetAdr())
                        .userLotAdr(restoreCompanyInfo.getUserInfo().getUserLotAdr())
                        .userDetailAdr(restoreCompanyInfo.getUserInfo().getUserDetailAdr())
                        .userEnabled(true)
                        .userDeletedDate(null)
                        .build()
        );
        companyRepository.save(
                CompanyInfo.builder()
                        .companyId(restoreCompanyInfo.getCompanyId())
                        .userInfo(restoreCompanyInfo.getUserInfo())
                        .companyName(restoreCompanyInfo.getCompanyName())
                        .companyNum(restoreCompanyInfo.getCompanyNum())
                        .companyAdrNum(restoreCompanyInfo.getCompanyAdrNum())
                        .companyLotAdr(restoreCompanyInfo.getCompanyLotAdr())
                        .companyStreetAdr(restoreCompanyInfo.getCompanyStreetAdr())
                        .companyDetailAdr(restoreCompanyInfo.getCompanyDetailAdr())
                        .companyBankName(restoreCompanyInfo.getCompanyBankName())
                        .companyBanknum(restoreCompanyInfo.getCompanyBanknum())
                        .companyEnabled(true)
                        .companyDeletedDate(null)
                        .build()
        );
        return restoreCompanyInfo.getCompanyId();
    }

}
