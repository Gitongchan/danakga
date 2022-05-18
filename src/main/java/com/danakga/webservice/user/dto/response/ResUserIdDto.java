package com.danakga.webservice.user.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResUserIdDto {
    private String userid;
    private String message;

    public ResUserIdDto(String userid , String message) {
        this.userid = userid;
        this.message = message;
    }
}
