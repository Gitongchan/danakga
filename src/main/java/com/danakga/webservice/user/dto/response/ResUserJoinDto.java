package com.danakga.webservice.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResUserJoinDto {
    private Long id;
    private String message;

    public ResUserJoinDto(Long id) {
        this.id = id;
        this.message = "회원가입 성공";
    }
}
