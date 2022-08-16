package com.danakga.webservice.cart.repository;

import com.danakga.webservice.cart.model.Cart;
import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.user.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {
    Optional<Cart> findByCartId(Long cartId);

    Optional<Cart> findByCartIdAndUserInfo(Long cartId, UserInfo userInfo);

    Optional<Cart> findByUserInfoAndProductId(UserInfo userInfo , Product productId);


    Optional<Cart>  findByProductId(Product productId);

    Optional<Cart> findByUserInfo(UserInfo userInfo);

    @Modifying
    void deleteAllByUserInfo(UserInfo userInfo);

}
