package com.danakga.webservice.mytool.repository;

import com.danakga.webservice.mytool.model.MyToolDetail;
import com.danakga.webservice.mytool.model.MyToolFolder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyToolDetailRepository extends JpaRepository<MyToolDetail,Integer> {
}
