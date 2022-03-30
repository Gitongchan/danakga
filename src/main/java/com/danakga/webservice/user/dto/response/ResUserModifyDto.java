package com.danakga.webservice.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResUserModifyDto {
    private Long id;
    private String message;

    public ResUserModifyDto(Long id) {
        this.id = id;
        this.message = "회원정보 수정 완료";
    }
}
