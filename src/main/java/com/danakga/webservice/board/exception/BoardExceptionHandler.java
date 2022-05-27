package com.danakga.webservice.board.exception;

import com.danakga.webservice.board.dto.response.ResErrorDto;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class BoardExceptionHandler extends ResponseEntityExceptionHandler {

    public static int NOT_FOUND = 404;
    public static int PAYLOAD_TOO_LARGE = 413;

    @ExceptionHandler(CustomException.ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(CustomException.ResourceNotFoundException exception) {
        ResErrorDto response = new ResErrorDto(NOT_FOUND, exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SizeLimitExceededException.class)
    public ResponseEntity<?> sizeLimitExceededException(CustomException.SizeLimitExceededException exception) {
        ResErrorDto response = new ResErrorDto(PAYLOAD_TOO_LARGE, exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.PAYLOAD_TOO_LARGE);
    }

    @ExceptionHandler(CustomException.FileSizeLimitExceededException.class)
    public ResponseEntity<?> fileSizeLimitExceededException(CustomException.FileSizeLimitExceededException exception) {
        ResErrorDto response = new ResErrorDto(PAYLOAD_TOO_LARGE, exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.PAYLOAD_TOO_LARGE);
    }
}
