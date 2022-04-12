package com.danakga.webservice.board.service.Impl;

import com.danakga.webservice.board.dto.request.ReqBoardWriteDto;
import com.danakga.webservice.board.dto.response.ResBoardUpdateDto;
import com.danakga.webservice.board.dto.response.ResBoardWriteDto;
import com.danakga.webservice.board.model.Board;
import com.danakga.webservice.board.repository.BoardRepository;
import com.danakga.webservice.board.service.BoardService;
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

    @Override
    public ResBoardWriteDto write(ReqBoardWriteDto reqBoardWriteDto) {
        final Long id = boardRepository.save(
                Board.builder()
                        .bd_id(reqBoardWriteDto.getBd_id())
                        .bd_type(reqBoardWriteDto.getBd_type())
                        .bd_views(reqBoardWriteDto.getBd_views())
                        .bd_writer(reqBoardWriteDto.getBd_writer())
                        .bd_title(reqBoardWriteDto.getBd_title())
                        .bd_content(reqBoardWriteDto.getBd_content())
                        .bd_filepath(reqBoardWriteDto.getBd_filepath())
                        .bd_deleted(reqBoardWriteDto.getBd_deleted())
                        .build()
        ).getBd_id();
        return new ResBoardWriteDto(id);
    }

    @Override
    public ResBoardUpdateDto update(Board board) {
        return null;
    }
}
