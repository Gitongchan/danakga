package com.danakga.webservice.user.service;

import com.danakga.webservice.user.dto.UserJoinDto;
import org.springframework.validation.Errors;

import java.util.HashMap;

public interface UserService {
    //회원가입
    Long join(UserJoinDto userJoinDto);
    
    //유효성검사
    HashMap<String,String> validateHandling(Errors errors);
}
