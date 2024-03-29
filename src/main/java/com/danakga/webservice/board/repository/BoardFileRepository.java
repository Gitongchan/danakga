package com.danakga.webservice.board.repository;

import com.danakga.webservice.board.model.Board;
import com.danakga.webservice.board.model.Board_Files;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardFileRepository extends JpaRepository<Board_Files, Long> {

    //게시글 조회 시 bd_id 값 찾기
    List<Board_Files> findByBoard(Board board);


    //파일 삭제
    void deleteByBoardAndFileSaveName(Board board, String fileSaveName);
}
