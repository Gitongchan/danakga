package com.danakga.webservice.user.service.Impl;

import com.danakga.webservice.user.dto.request.UserAdapter;
import com.danakga.webservice.user.dto.request.UserInfoDto;
import com.danakga.webservice.user.dto.response.ResDupliCheckDto;
import com.danakga.webservice.user.dto.response.ResUserJoinDto;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.repository.UserRepository;
import com.danakga.webservice.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    @Autowired private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
        UserInfo userInfo = userRepository.findByUserid(userid)
                .orElseThrow(() -> new UsernameNotFoundException((userid)));

        return new UserAdapter(userInfo);
    }

    //회원가입
    @Override
    public ResUserJoinDto join(UserInfoDto userInfoDto) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String rawPassword = userInfoDto.getPassword();
        userInfoDto.setPassword(bCryptPasswordEncoder.encode(rawPassword));

        //임시로 권한 USER로 지정
        userInfoDto.setRole("ROLE_USER");

        final Long id = userRepository.save(
                UserInfo.builder()
                        .userid(userInfoDto.getUserid())
                        .password(userInfoDto.getPassword())
                        .name(userInfoDto.getName())
                        .phone(userInfoDto.getPhone())
                        .email(userInfoDto.getEmail())
                        .role(userInfoDto.getRole())
                        .build()
        ).getId();
        return new ResUserJoinDto(id);
    }

    //유저 아이디 중복 체크
    @Override
    public ResDupliCheckDto userIdCheck(String userid) {
        //.isPresent , Optional객체가 있으면 true null이면 false 반환
      if(userRepository.findByUserid(userid).isPresent()){
       return new ResDupliCheckDto(-1); //같은 userid있으면 -1반환
      }
      return new ResDupliCheckDto(1);
    }

    //이메일 중복 체크
    @Override
    public ResDupliCheckDto emailCheck(String email) {
        if(userRepository.findByEmail(email).isPresent()){
            System.out.println("같은이메일이 존재함");
            return new ResDupliCheckDto(-1);
        }
        System.out.println("같은 이메일이 없다");
        return new ResDupliCheckDto(1);
    }



}
