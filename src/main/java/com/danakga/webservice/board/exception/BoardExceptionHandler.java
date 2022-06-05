package com.danakga.webservice.board.exception;

import com.danakga.webservice.util.responseDto.ResErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class BoardExceptionHandler extends ResponseEntityExceptionHandler {

    public static int NOT_FOUND = 404;

    @ExceptionHandler(CustomException.ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(CustomException.ResourceNotFoundException exception) {
        ResErrorDto response = new ResErrorDto(NOT_FOUND, exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
