package com.danakga.webservice.user.service.Impl;

import com.danakga.webservice.company.repository.CompanyRepository;
import com.danakga.webservice.user.dto.request.UserAdapter;
import com.danakga.webservice.user.dto.request.UserInfoDto;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.repository.UserRepository;
import com.danakga.webservice.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

        //임시로 권한 USER로 지정
        userInfoDto.setRole("ROLE_USER");
        userInfoDto.setUserEnabled(true);

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
                        .role(userInfoDto.getRole())
                        .userAdrNum(userInfoDto.getUserAdrNum())
                        .userDefAdr(userInfoDto.getUserDefAdr())
                        .userDetailAdr(userInfoDto.getUserDetailAdr())
                        .userEnabled(userInfoDto.isUserEnabled())
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
    @Override
    public Long update(UserInfo userInfo, UserInfoDto userInfoDto) {
        //로그인 사용자 검증 이후 동작함
        if (userRepository.findById(userInfo.getId()).isPresent()) {

            userInfoDto.setRole("ROLE_USER");//임시로 권한 USER로 지정

            userRepository.save(
                    UserInfo.builder()
                            .id(userInfo.getId()) //로그인 유저 키값을 받아옴
                            .userid(userInfo.getUserid()) //그대로 유지
                            .password(userInfoDto.getPassword())
                            .name(userInfoDto.getName())
                            .phone(userInfoDto.getPhone())
                            .email(userInfoDto.getEmail())
                            .role(userInfoDto.getRole())
                            .userAdrNum(userInfoDto.getUserAdrNum())
                            .userDefAdr(userInfoDto.getUserDefAdr())
                            .userDetailAdr(userInfoDto.getUserDetailAdr())
                            .userEnabled(userInfo.isUserEnabled())
                            .build()
            );
            return userInfo.getId();
        }
        return -1L;
    }

    

    //회원 탈퇴
    @Override
    public Long userDeleted(UserInfo userInfo, UserInfoDto userInfoDto) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        System.out.println("userInfo.getUserid() = " + userInfo.getPassword());
        System.out.println("userInfoDto = " + userInfoDto.getPassword());

        if (userRepository.findById(userInfo.getId()).isPresent()
                && bCryptPasswordEncoder.matches(userInfoDto.getPassword(),userInfo.getPassword())) {
            userInfoDto.setUserEnabled(false);

            userRepository.save(
                    UserInfo.builder()
                            .id(userInfo.getId()) //로그인 유저 키값을 받아옴
                            .userid(userInfo.getUserid()) //그대로 유지
                            .password(userInfo.getPassword())
                            .name(userInfo.getName())
                            .phone(userInfo.getPhone())
                            .email(userInfo.getEmail())
                            .role(userInfo.getRole())
                            .userAdrNum(userInfo.getUserAdrNum())
                            .userDefAdr(userInfo.getUserDefAdr())
                            .userDetailAdr(userInfo.getUserDetailAdr())
                            .userEnabled(userInfoDto.isUserEnabled())
                            .build()
            );
            return userInfo.getId();
        }
        return -1L;
    }


}
