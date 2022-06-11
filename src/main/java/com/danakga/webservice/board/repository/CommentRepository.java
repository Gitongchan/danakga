package com.danakga.webservice.board.repository;

import com.danakga.webservice.board.model.Board;
import com.danakga.webservice.board.model.Board_Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Board_Comment, Long> {

    Page<Board_Comment> findAllByBoardAndCmDeleted(Board board, String Deleted, Pageable pageable);
}
