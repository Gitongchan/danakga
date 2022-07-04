package com.danakga.webservice.Cart.repository;

import com.danakga.webservice.Cart.model.Cart;
import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.product.model.ProductFiles;
import com.danakga.webservice.user.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Integer> {

    @Query("select * from Cart")
    List<Cart> selectAllCart(@Param("cart_id") Long cart_id, @Param("pd_id") Product product_id, @Param("u_id") UserInfo userInfo, @Param("fie_id") String fieId, @Param("cart_amount") Integer cart_amount);
}
