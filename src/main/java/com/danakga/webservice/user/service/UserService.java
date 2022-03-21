package com.danakga.webservice.user.service;

import com.danakga.webservice.user.dto.UserJoinDto;

public interface UserService {
    Long join(UserJoinDto userJoinDto);
}
