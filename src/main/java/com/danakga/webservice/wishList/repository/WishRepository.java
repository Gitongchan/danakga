package com.danakga.webservice.wishList.repository;

import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.wishList.model.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishRepository extends JpaRepository<Wish,Long> {
    Optional<Wish> findByUserInfoAndProductId (UserInfo userInfo, Product productId);

    Page<Wish> findByUserInfo(UserInfo userInfo,Pageable pageable);

    Optional<Wish> findByWishIdAndUserInfo(Long wishId,UserInfo userInfo);

    Long countByProductId(Product product);



}
