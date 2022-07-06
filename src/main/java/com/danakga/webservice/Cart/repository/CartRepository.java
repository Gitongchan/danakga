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

    Optional<Cart> findById(Integer integer);

}
