package com.danakga.webservice.board.repository;

import com.danakga.webservice.board.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
