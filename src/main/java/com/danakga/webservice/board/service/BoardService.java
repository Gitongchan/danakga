package com.danakga.webservice.board.service;

import com.danakga.webservice.board.dto.request.ReqBoardDto;
import com.danakga.webservice.board.dto.request.ReqDeletedFileDto;
import com.danakga.webservice.board.dto.response.ResBoardListDto;
import com.danakga.webservice.board.dto.response.ResBoardPostDto;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface BoardService {

    //게시판 목록
    ResBoardListDto boardList(Pageable pageable, String boardType, int page);

    //게시글 조회
    ResBoardPostDto getPost(Long bd_id, HttpServletRequest request, HttpServletResponse response);

    //게시글 작성
    ResResultDto postWrite(ReqBoardDto reqBoardWriteDto, UserInfo userInfo, List<MultipartFile> files);

    //게시글 수정
    ResResultDto postEdit(Long bd_id, UserInfo userInfo, ReqBoardDto reqBoardDto, ReqDeletedFileDto reqDeletedFileDto, List<MultipartFile> files);

    //게시글 삭제 여부 변경
    ResResultDto postDelete(Long bd_id, UserInfo userInfo);

    //게시판 검색
    ResBoardListDto boardSearch(Pageable pageable, String category, String content, String boardType, int page);

}
