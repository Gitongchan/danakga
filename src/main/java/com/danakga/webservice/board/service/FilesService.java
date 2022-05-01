package com.danakga.webservice.board.service;

import com.danakga.webservice.board.dto.request.ReqFileUploadDto;
import com.danakga.webservice.board.dto.response.ResFileUploadDto;
import com.danakga.webservice.board.model.Board;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FilesService {

    ResFileUploadDto saveFileUpload(ReqFileUploadDto reqFileUploadDto, List<MultipartFile> files, Board board);
}
