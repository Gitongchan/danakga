package com.danakga.webservice.cart.repository;

import com.danakga.webservice.cart.model.Cart;
import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.user.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {
    Optional<Cart> findByCartId(Long cartId);// 아이디만 있나 조회

    Optional<Cart> findByCartIdAndUserInfo(Long cartId, UserInfo userInfo);// 아이디만 있나 조회

    Optional<Cart> findByUserInfoAndProductId(UserInfo userInfo , Product productId);


    Optional<Cart>  findByProductId(Product productId);

    Optional<Cart> findByUserInfo(UserInfo userInfo);
//
//    void delete(Long cartId);
}
