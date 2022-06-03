package com.danakga.webservice.user.service.Impl;

import com.danakga.webservice.company.dto.request.CompanyInfoDto;
import com.danakga.webservice.company.model.CompanyInfo;
import com.danakga.webservice.company.repository.CompanyRepository;
import com.danakga.webservice.user.dto.request.UpdateUserInfoDto;
import com.danakga.webservice.user.dto.request.UserAdapter;
import com.danakga.webservice.user.dto.request.UserInfoDto;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.model.UserRole;
import com.danakga.webservice.user.repository.UserRepository;
import com.danakga.webservice.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {


    @Autowired private final UserRepository userRepository;
    @Autowired private final CompanyRepository companyRepository;


    @Override
    public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
        UserInfo userInfo = userRepository.findByUserid(userid)
                .orElseThrow(() -> new UsernameNotFoundException((userid)));

        return new UserAdapter(userInfo);
    }

    //회원가입
    @Override
    public Long join(UserInfoDto userInfoDto) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String rawPassword = userInfoDto.getPassword();
        userInfoDto.setPassword(bCryptPasswordEncoder.encode(rawPassword));

        //중복 id,email 검증
        Integer idCheckResult = userIdCheck(userInfoDto.getUserid());
        Integer emailCheckResult = emailCheck(userInfoDto.getEmail());

        if(idCheckResult.equals(-1)||emailCheckResult.equals(-1)) {
            return -1L;
        }
        return userRepository.save(
                UserInfo.builder()
                        .userid(userInfoDto.getUserid())
                        .password(userInfoDto.getPassword())
                        .name(userInfoDto.getName())
                        .phone(userInfoDto.getPhone())
                        .email(userInfoDto.getEmail())
                        .role(UserRole.ROLE_USER)//임시로 권한 USER로 지정
                        .userAdrNum(userInfoDto.getUserAdrNum())
                        .userLotAdr(userInfoDto.getUserLotAdr())
                        .userStreetAdr(userInfoDto.getUserStreetAdr())
                        .userDetailAdr(userInfoDto.getUserDetailAdr())
                        .userEnabled(true)
                        .build()
        ).getId();
    }

    //유저 아이디 중복 체크
    @Override
    public Integer userIdCheck(String userid) {
        //.isPresent , Optional객체가 있으면 true null이면 false 반환
        if (userRepository.findByUserid(userid).isPresent()) {
            return -1; //같은 userid있으면 -1반환
        }
        return 1;
    }

    //이메일 중복 체크
    @Override
    public Integer emailCheck(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            return -1; //같은 이메일 존재할 때
        }
        return 1; // 같은 이메일 없을 때
    }

    //회원 정보 수정
    @Transactional
    @Override
    public Long update(UserInfo userInfo, UpdateUserInfoDto updateUserInfoDto) {
        //로그인 사용자 검증 이후 동작함
        if(userRepository.findById(userInfo.getId()).isEmpty()) {
            return -1L;
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        UserInfo modifyUser = userRepository.findById(userInfo.getId()).get();

        String rawCheckPassword = updateUserInfoDto.getCheckPassword();

        if(bCryptPasswordEncoder.matches(rawCheckPassword,modifyUser.getPassword())){

            String rawPassword = updateUserInfoDto.getPassword();
            updateUserInfoDto.setPassword(bCryptPasswordEncoder.encode(rawPassword));

            userRepository.save(
                    UserInfo.builder()
                            .id(modifyUser.getId()) //로그인 유저 키값을 받아옴
                            .userid(modifyUser.getUserid()) //그대로 유지
                            .password(updateUserInfoDto.getPassword())
                            .name(updateUserInfoDto.getName())
                            .phone(updateUserInfoDto.getPhone())
                            .email(modifyUser.getEmail())
                            .role(modifyUser.getRole())
                            .userAdrNum(updateUserInfoDto.getUserAdrNum())
                            .userLotAdr(updateUserInfoDto.getUserLotAdr())
                            .userStreetAdr(updateUserInfoDto.getUserStreetAdr())
                            .userDetailAdr(updateUserInfoDto.getUserDetailAdr())
                            .userEnabled(modifyUser.isUserEnabled())
                            .build()
            );
            return userInfo.getId();
        }
        return -2L;
        }

    //회원 탈퇴
    @Override
    @Transactional
    public Long userDeleted(UserInfo userInfo, UserInfoDto userInfoDto) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        userInfoDto.setUserEnabled(false);//사용자 이용 중지

        if(!userInfo.getRole().equals(UserRole.ROLE_USER)) return -2L;

        if (userRepository.findById(userInfo.getId()).isEmpty()) {
            return -1L;
        }

        UserInfo DeleteUser = userRepository.findById(userInfo.getId()).get();

        if(!bCryptPasswordEncoder.matches(userInfoDto.getPassword(),DeleteUser.getPassword())){
           return -1L;
        }
            userRepository.save(
                    UserInfo.builder()
                            .id(DeleteUser.getId()) //로그인 유저 키값을 받아옴
                            .userid(DeleteUser.getUserid()) //그대로 유지
                            .password(DeleteUser.getPassword())
                            .name(DeleteUser.getName())
                            .phone(DeleteUser.getPhone())
                            .email(DeleteUser.getEmail())
                            .role(DeleteUser.getRole())
                            .userAdrNum(DeleteUser.getUserAdrNum())
                            .userStreetAdr(DeleteUser.getUserStreetAdr())
                            .userLotAdr(DeleteUser.getUserLotAdr())
                            .userDetailAdr(DeleteUser.getUserDetailAdr())
                            .userDeletedDate(LocalDateTime.now()) //현재시간
                            .userEnabled(userInfoDto.isUserEnabled())
                            .build()
            );
            return userInfo.getId();
        }

    //회원 정보 조회
    @Override
    public UserInfo userInfoCheck(UserInfo userInfo) {

        if(userRepository.findById(userInfo.getId()).isPresent()) {
            return  userRepository.findById(userInfo.getId()).get();
        }
        return null;
    }

    //사업자 등록
    //일반회원으로 등록된 사용자의 사업자 등록
    @Override
    public Long companyRegister(UserInfo userInfo, UserInfoDto userInfoDto, CompanyInfoDto companyInfoDto) {

        if(companyRepository.findByUserInfo(userInfo).isPresent()){
            return -1L;//가입된 유저 정보 있으면 -1L
        }

        if(userRepository.findById(userInfo.getId()).isPresent()){

            UserInfo registerUserInfo = userRepository.findById(userInfo.getId()).get();


            userRepository.save(
                    UserInfo.builder()
                            .id(registerUserInfo.getId())
                            .userid(registerUserInfo.getUserid())
                            .password(registerUserInfo.getPassword())
                            .name(registerUserInfo.getName())
                            .phone(registerUserInfo.getPhone())
                            .email(registerUserInfo.getEmail())
                            .role(UserRole.ROLE_MANAGER)
                            .userAdrNum(registerUserInfo.getUserAdrNum())
                            .userStreetAdr(registerUserInfo.getUserStreetAdr())
                            .userLotAdr(registerUserInfo.getUserLotAdr())
                            .userDetailAdr(registerUserInfo.getUserDetailAdr())
                            .userEnabled(registerUserInfo.isUserEnabled())
                            .build()
            );
            companyRepository.save(
                    CompanyInfo.builder()
                            .companyId(companyInfoDto.getCompanyId())
                            .userInfo(registerUserInfo)
                            .companyName(companyInfoDto.getCompanyName())
                            .companyNum(companyInfoDto.getCompanyNum())
                            .companyAdrNum(companyInfoDto.getCompanyAdrNum())
                            .companyLotAdr(companyInfoDto.getCompanyLotAdr())
                            .companyStreetAdr(companyInfoDto.getCompanyStreetAdr())
                            .companyDetailAdr(companyInfoDto.getCompanyDetailAdr())
                            .companyBanknum(companyInfoDto.getCompanyBanknum())
                            .companyEnabled(true)
                            .build()
            );            return registerUserInfo.getId();

        }
        return -1L;
    }

    //탈퇴한 회원 복구
    @Override
    public Long companyRestore(UserInfo userInfo, UserInfoDto userInfoDto, CompanyInfoDto companyInfoDto) {


        if (userRepository.findById(userInfo.getId()).isEmpty()) {
            return -1L; //사용자 정보 없음
        }

        UserInfo restoreUserInfo = userRepository.findById(userInfo.getId()).get();

        if (companyRepository.findByUserInfo(restoreUserInfo).isEmpty()) {
            return -1L; //로그인된 사용자가 사업자 등록이 안되어있음
        }

        CompanyInfo restoreComUserInfo = companyRepository.findByUserInfo(restoreUserInfo).get();


        if (!restoreComUserInfo.isCompanyEnabled()) {
            userRepository.save(
                    UserInfo.builder()
                            .id(restoreUserInfo.getId())
                            .userid(restoreUserInfo.getUserid())
                            .password(restoreUserInfo.getPassword())
                            .name(restoreUserInfo.getName())
                            .phone(restoreUserInfo.getPhone())
                            .email(restoreUserInfo.getEmail())
                            .role(UserRole.ROLE_MANAGER)
                            .userAdrNum(restoreUserInfo.getUserAdrNum())
                            .userStreetAdr(restoreUserInfo.getUserStreetAdr())
                            .userLotAdr(restoreUserInfo.getUserLotAdr())
                            .userDetailAdr(restoreUserInfo.getUserDetailAdr())
                            .userEnabled(restoreUserInfo.isUserEnabled())
                            .build()
            );
            companyRepository.save(
                    CompanyInfo.builder()
                            .companyId(restoreComUserInfo.getCompanyId())
                            .userInfo(restoreUserInfo)
                            .companyName(restoreComUserInfo.getCompanyName())
                            .companyNum(restoreComUserInfo.getCompanyNum())
                            .companyAdrNum(restoreComUserInfo.getCompanyAdrNum())
                            .companyLotAdr(restoreComUserInfo.getCompanyLotAdr())
                            .companyStreetAdr(restoreComUserInfo.getCompanyStreetAdr())
                            .companyDetailAdr(restoreComUserInfo.getCompanyDetailAdr())
                            .companyBanknum(restoreComUserInfo.getCompanyBanknum())
                            .companyEnabled(true)
                            .companyDeltedDate(null)
                            .build()
            );
            return restoreUserInfo.getId();

        }
        return -1L; //이미 사업자 이용중임
    }

    @Override
    public String useridFind(UserInfoDto userInfoDto) {
        if(userRepository.findByEmailAndPhone(userInfoDto.getEmail(),userInfoDto.getPhone()).isPresent()){
            UserInfo findUserInfo = userRepository.findByEmailAndPhone(userInfoDto.getEmail(),userInfoDto.getPhone()).get();
            return findUserInfo.getUserid();
        }
        return null;
    }
}
