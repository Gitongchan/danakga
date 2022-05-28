package com.danakga.webservice.util.exception;

import com.danakga.webservice.util.responseDto.ResErrorDto;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class UtilExceptionHandler extends ResponseEntityExceptionHandler {

    public static int PAYLOAD_TOO_LARGE = 413;

    //request 업로드 용량 초과
    @ExceptionHandler(SizeLimitExceededException.class)
    public ResErrorDto sizeLimitExceededException() {
        String message = "업로드 용량을 초과 했습니다.(최대 30MB)";
        return new ResErrorDto(PAYLOAD_TOO_LARGE,message);
    }

    //개별 파일 용량 초과
    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ResErrorDto fileSizeLimitExceededException() {
        String message = "파일의 최대 크기를 초과 했습니다.(최대 10MB)";
        return new ResErrorDto(PAYLOAD_TOO_LARGE,message);
    }
}
