package com.danakga.webservice.board.service;

import com.danakga.webservice.board.model.Board;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FilesService {

    //파일 저장
    Long saveFileUpload(List<MultipartFile> files, Board board);
}
