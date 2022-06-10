package com.danakga.webservice.board.repository;

import com.danakga.webservice.board.model.Board_Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Board_Comment, Long> {
}
