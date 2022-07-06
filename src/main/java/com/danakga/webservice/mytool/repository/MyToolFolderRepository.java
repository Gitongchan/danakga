package com.danakga.webservice.mytool.repository;

import com.danakga.webservice.mytool.model.MyToolFolder;
import com.danakga.webservice.user.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MyToolFolderRepository extends JpaRepository<MyToolFolder,Long> {

    Optional<MyToolFolder> findByIdAndUserInfo(Long Id, UserInfo userInfo);

    List<MyToolFolder> findByUserInfo(UserInfo userInfo);

    Optional<MyToolFolder> findById(Long Id);
}
