package com.danakga.webservice.board.repository;

import com.danakga.webservice.board.dto.response.ResBoardListDto;
import com.danakga.webservice.board.model.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Optional<Board> findById(Long id);

    Page<Board> findAll(Pageable pageable);
}
