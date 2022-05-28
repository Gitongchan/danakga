package com.danakga.webservice.board.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class CustomException{

    //게시글을 찾지 못한 경우
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public static class ResourceNotFoundException extends RuntimeException {
     public ResourceNotFoundException(String message) {
         super(message);
     }
    }
}
