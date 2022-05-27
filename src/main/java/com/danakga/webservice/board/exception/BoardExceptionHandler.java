package com.danakga.webservice.board.exception;

import com.danakga.webservice.board.dto.response.ResErrorDto;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
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

    //게시글이 존재 하지 않는 경우
    //이 경우는 properties에 설정한 값이 없기 때문에 해당 메소드 쪽에서 if문으로 조건 걸어서 exception처리 가능
    @ExceptionHandler(CustomException.ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(CustomException.ResourceNotFoundException exception) {
        ResErrorDto response = new ResErrorDto(NOT_FOUND, exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    /* properties에 설정한 파일 업로드 제한 값은 서버에 들어오자마자 먼저 걸러지기 때문에
       컨트롤러 실행 전에 모두 처리되어 콘솔에 오류를 띄워줘서 해당 exception class를 직접 @ExceptionHandler로 처리하여
       json으로 결과 값 반환(@RestControllerAdvice)
    */
    
    //request 용량 초과
    @ExceptionHandler(SizeLimitExceededException.class)
    public ResErrorDto sizeLimitExceededException(SizeLimitExceededException exception) {
        String message = "업로드 용량을 초과 했습니다.(최대 30MB)";
        return new ResErrorDto(PAYLOAD_TOO_LARGE,message);
    }

    //개별 파일 용량 초과
    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ResErrorDto fileSizeLimitExceededException(FileSizeLimitExceededException exception) {
        String message = "파일의 최대 크기를 초과 했습니다.(최대 10MB)";
        return new ResErrorDto(PAYLOAD_TOO_LARGE,message);
    }
}
