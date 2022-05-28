package com.danakga.webservice.board.service;

import com.danakga.webservice.board.dto.request.ReqBoardDto;
import com.danakga.webservice.board.dto.response.ResBoardListDto;
import com.danakga.webservice.board.dto.response.ResBoardPostDto;
import com.danakga.webservice.user.model.UserInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface BoardService {

    //게시판 목록
    List<ResBoardListDto> boardList(Pageable pageable, String board_type, int page);

    //게시글 조회
    ResBoardPostDto getPost(Long bd_id, HttpServletRequest request, HttpServletResponse response);

    //게시글 작성
    Long postWrite(ReqBoardDto reqBoardWriteDto, UserInfo userInfo, List<MultipartFile> files);

    //게시글 수정
    Long postEdit(Long id, UserInfo userInfo, ReqBoardDto reqBoardDto, List<MultipartFile> files);

    Long postDelete(Long id, UserInfo userInfo);

}
