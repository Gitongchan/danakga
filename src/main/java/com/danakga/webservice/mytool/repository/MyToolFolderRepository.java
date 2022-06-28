package com.danakga.webservice.mytool.repository;

import com.danakga.webservice.mytool.model.MyToolFolder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MyToolFolderRepository extends JpaRepository<MyToolFolder,Integer> {
    Optional<MyToolFolder> findById(Long Id);
}
