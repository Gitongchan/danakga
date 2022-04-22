package com.danakga.webservice.board.service;

import com.danakga.webservice.board.dto.request.ReqBoardWriteDto;
import com.danakga.webservice.board.dto.request.ReqFileUploadDto;
import com.danakga.webservice.board.dto.response.ResBoardUpdateDto;
import com.danakga.webservice.board.dto.response.ResBoardWriteDto;
import com.danakga.webservice.board.model.Board;
import com.danakga.webservice.user.model.UserInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardService {

    //게시판 목록
    List<Board> list();

    //게시글 작성
    ResBoardWriteDto write(ReqBoardWriteDto reqBoardWriteDto, ReqFileUploadDto reqFileUploadDto, MultipartFile files, UserInfo userInfo);

    //게시글 수정
    ResBoardUpdateDto update(Board board);
}
