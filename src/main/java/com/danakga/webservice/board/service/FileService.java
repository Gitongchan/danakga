package com.danakga.webservice.board.service;

import com.danakga.webservice.board.dto.request.ReqFileUploadDto;
import com.danakga.webservice.board.dto.response.ResFileUploadDto;
import com.danakga.webservice.board.model.Board;
import com.danakga.webservice.board.repository.FileRepository;

public interface FileService {

    ResFileUploadDto savefile(ReqFileUploadDto reqFileUploadDto, Board board);
}
