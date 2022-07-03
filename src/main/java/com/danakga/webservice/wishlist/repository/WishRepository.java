package com.danakga.webservice.wishlist.repository;

import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.wishlist.model.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishRepository extends JpaRepository<Wish,Integer> {
    Optional<Wish> findByUserInfoAndProduct_id (UserInfo userInfo, Long productId);
    //좋아요 삭제
    Optional<Wish> findById(Integer integer);
}
