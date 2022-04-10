package com.danakga.webservice.board.repository;

import com.danakga.webservice.board.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
