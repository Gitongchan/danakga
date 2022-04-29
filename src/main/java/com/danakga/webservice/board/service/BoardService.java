package com.danakga.webservice.board.service;

import com.danakga.webservice.board.dto.request.ReqBoardWriteDto;
import com.danakga.webservice.board.dto.response.ResBoardDto;
import com.danakga.webservice.board.model.Board;
import com.danakga.webservice.user.model.UserInfo;

import java.util.List;

public interface BoardService {

    //게시판 목록
    List<Board> list();

    //게시글 작성
    ResBoardDto write(ReqBoardWriteDto reqBoardWriteDto, UserInfo userInfo);


    //게시글 수정
    ResBoardDto.ResBoardUpdateDto update(Board board);
}
