package com.danakga.webservice.wishlist.repository;

import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.wishlist.model.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishRepository extends JpaRepository<Wish,Long> {
    Optional<Wish> findByUserInfoAndProductId (UserInfo userInfo, Product productId);

}
