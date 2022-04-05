package com.danakga.webservice.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResUserResultDto {
    private Long id;
    private String message;

    public ResUserResultDto(Long id , String message) {
        this.id = id;
        this.message = message;
    }
}
