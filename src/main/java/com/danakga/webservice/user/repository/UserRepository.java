package com.danakga.webservice.user.repository;

import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.model.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserInfo,Long> {
    Optional<UserInfo> findByUserid(String userid);

    Optional<UserInfo> findByEmail(String email);

    Optional<UserInfo> findById(Long id);

    Optional<UserInfo> findByIdAndRole(Long id, UserRole role);

    Optional<UserInfo> findByUseridAndRole(String userid, UserRole role);

    Optional<UserInfo> findByEmailAndPhone(String email, String phone);

    Optional<UserInfo> findByUseridAndEmailAndPhone(String userid, String email, String phone);

    @Modifying
    @Query("update UserInfo u set u.password = :password where u.id = :id")
    void updateUserInfoPassword(String password,Long id);

    
    //어드민 - 일반 유저 목록 조회
    @Query(value="select u from UserInfo u where u.name like %:userName% " +
            "and u.email like %:userEmail% " +
            "and u.phone like %:userPhone% " +
            "and u.userid like %:userId% " +
            "and u.role = :userRole " +
            "and u.userEnabled <= :userEnabled")
    Page<UserInfo> findAllUserInfo(UserRole userRole,
                                   String userName,String userEmail,String userPhone,
                                   String userId,boolean userEnabled,Pageable pageable);



}
