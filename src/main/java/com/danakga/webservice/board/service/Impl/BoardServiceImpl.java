package com.danakga.webservice.board.service.Impl;

import com.danakga.webservice.board.dto.request.ReqBoardWriteDto;
import com.danakga.webservice.board.dto.response.ResBoardDto;
import com.danakga.webservice.board.model.Board;
import com.danakga.webservice.board.repository.BoardRepository;
import com.danakga.webservice.board.service.BoardService;
import com.danakga.webservice.user.model.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    @Autowired private final BoardRepository boardRepository;

    //게시판 목록
    @Override
    public List<Board> list() {
        return boardRepository.findAll();
    }

    //jpa는 id값만 확인하기 때문에 외래키로 설정한 값에 그대로 넣어주면 DB 테이블에 id값 들어옴!
    //.userInfo(userInfo)안해주면 외래키로 설정된 컬럼에 null값이 박힘
    @Override
    public ResBoardDto write(ReqBoardWriteDto reqBoardWriteDto, UserInfo userInfo) {
        final Long bd_id = boardRepository.save(
                Board.builder()
                        .bd_id(reqBoardWriteDto.getBd_id())
                        .bd_type(reqBoardWriteDto.getBd_type())
                        .bd_views(reqBoardWriteDto.getBd_views())
                        .bd_writer(userInfo.getUserid())
                        .bd_title(reqBoardWriteDto.getBd_title())
                        .bd_content(reqBoardWriteDto.getBd_content())
                        .bd_deleted(reqBoardWriteDto.getBd_deleted())
                        .userInfo(userInfo)
                        .build()
        ).getBd_id();
        return new ResBoardDto(bd_id);
    }

    @Override
    public ResBoardDto.ResBoardUpdateDto update(Board board) {
        return null;
    }

//    @Override
//    public ResBoardUpdateDto update(Board board) {
//        return null;
//    }
}
