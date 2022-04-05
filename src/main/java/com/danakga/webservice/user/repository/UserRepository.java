package com.danakga.webservice.user.repository;

import com.danakga.webservice.user.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserInfo,Integer> {
    Optional<UserInfo> findByUserid(String userid);

    Optional<UserInfo> findByEmail(String email);

    Optional<UserInfo> findById(Long id);
}
