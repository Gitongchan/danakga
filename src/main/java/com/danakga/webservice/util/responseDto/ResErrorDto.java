package com.danakga.webservice.util.responseDto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ResErrorDto {
    private int statusCode;
    private String message;

    public ResErrorDto(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
