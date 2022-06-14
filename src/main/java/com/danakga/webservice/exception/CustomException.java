package com.danakga.webservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class CustomException{

    //resource가 없는 경우
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public static class ResourceNotFoundException extends RuntimeException {
     public ResourceNotFoundException(String message) {
         super(message);
     }
    }
}
