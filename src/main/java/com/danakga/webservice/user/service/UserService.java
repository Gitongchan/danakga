package com.danakga.webservice.user.service;

import com.danakga.webservice.user.dto.request.UserJoinDto;
import com.danakga.webservice.user.dto.response.ResUserJoinDto;
import org.springframework.validation.Errors;

import java.util.Map;

public interface UserService {
    //회원가입
    ResUserJoinDto join(UserJoinDto userJoinDto);

}
