package com.danakga.webservice.board.service;

import com.danakga.webservice.board.dto.request.ReqFileUploadDto;
import com.danakga.webservice.board.dto.response.ResBoardDto;
import com.danakga.webservice.board.model.Board;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    //파일 업로드 후 저장
    ResBoardDto.ResFileUploadDto saveFile(ReqFileUploadDto reqFileUploadDto, MultipartFile files, Board board);
}
