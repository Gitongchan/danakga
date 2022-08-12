package com.danakga.webservice.cart.repository;

import com.danakga.webservice.cart.model.Cart;
import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.user.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {
//    List<Cart> findByIdcAndCartIdAndProductIdAndUserInfoAndCartAmountAndPrice(); // 몽땅 조회
    Optional<Cart> findByCartId(Long cartId);// 아이디만 있나 조회
    Optional<Cart> findByCartIdAndUserInfo(UserInfo userInfo, Long cartId);// 아이디만 있나 조회

//
    Optional<Cart> findByUserInfoAndProductId(UserInfo userInfo , Product productId);

    Optional<Cart> findById(Long aLong);

    Optional<Cart>  findByProductId(Long productId);

    Optional<Cart> findByUserInfo(UserInfo userInfo);
//
//    void delete(Long cartId);
}
