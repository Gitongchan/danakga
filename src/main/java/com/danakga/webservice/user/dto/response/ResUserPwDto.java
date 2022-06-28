package com.danakga.webservice.user.dto.response;

public class ResUserPwDto {
    private String password;
    private String message;

    public ResUserPwDto(String password, String message){
        this.password = password;
        this.message = message;
    }
}
