package com.danakga.webservice.board.service;

import com.danakga.webservice.board.dto.request.ReqBoardWriteDto;
import com.danakga.webservice.board.dto.response.ResBoardListDto;
import com.danakga.webservice.board.dto.response.ResBoardPostDto;
import com.danakga.webservice.board.model.Board;
import com.danakga.webservice.user.model.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardService {

    //게시판 목록
    List<ResBoardListDto> boardList(Pageable pageable);

    //게시글 조회
    ResBoardPostDto getpost(Long bd_id);

    //게시글 보기
    ResBoardListDto post();

    //게시글 작성
    Long write(ReqBoardWriteDto reqBoardWriteDto, UserInfo userInfo, List<MultipartFile> files);

    //게시글 수정
//    ResBoardUpdateDto edit(UserInfo userInfo, Board board);

}
