package com.danakga.webservice.mytool.repository;

import com.danakga.webservice.mytool.model.MyToolDetail;
import com.danakga.webservice.mytool.model.MyToolFolder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MyToolDetailRepository extends JpaRepository<MyToolDetail,Long> {

    Optional<MyToolDetail> findByIdAndMyToolFolder(Long Id,MyToolFolder myToolFolder);

    List<MyToolDetail> findByMyToolFolder(MyToolFolder myToolFolder);

}
