package com.danakga.webservice.board.repository;

import com.danakga.webservice.board.model.Board_Files;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<Board_Files, Long> {
}
