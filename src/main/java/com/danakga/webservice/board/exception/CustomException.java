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
    
    //maxRequestSize 값이 초과 한 경우
    @ResponseStatus(value = HttpStatus.PAYLOAD_TOO_LARGE)
    public static class SizeLimitExceededException extends RuntimeException {
        public SizeLimitExceededException(String message) {
            super(message);
        }
    }
    //maxFileSize 값이 초과 한 경우
    @ResponseStatus(value = HttpStatus.PAYLOAD_TOO_LARGE)
    public static class FileSizeLimitExceededException extends RuntimeException {
        public FileSizeLimitExceededException(String message) {
            super(message);
        }
    }


}
