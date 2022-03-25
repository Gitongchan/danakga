package com.danakga.webservice.user.service.Impl;

import com.danakga.webservice.user.dto.request.UserJoinDto;
import com.danakga.webservice.user.dto.response.ResDupliCheckDto;
import com.danakga.webservice.user.dto.response.ResUserJoinDto;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.repository.UserRepository;
import com.danakga.webservice.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {


    @Autowired private final UserRepository userRepository;
    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public ResUserJoinDto join(UserJoinDto userJoinDto) {
        String rawPassword = userJoinDto.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);

        //임시로 권한 USER로 지정
        userJoinDto.setRole("ROLE_USER");

        final Long id = userRepository.save(
                UserInfo.builder()
                        .userid(userJoinDto.getUserid())
                        .password(encPassword)
                        .username(userJoinDto.getUsername())
                        .phone(userJoinDto.getPhone())
                        .email(userJoinDto.getEmail())
                        .role(userJoinDto.getRole())
                        .build()
        ).getId();
        return new ResUserJoinDto(id);
    }

    @Override
    public ResDupliCheckDto userIdCheck(String userid) {
        //.isPresent , Optional객체가 있으면 true null이면 false 반환
      if(userRepository.findByUserid(userid).isPresent()){
       return new ResDupliCheckDto(-1); //같은 userid있으면 -1반환
      }
      return new ResDupliCheckDto(1);
    }

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
