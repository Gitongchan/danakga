package com.danakga.webservice.product.repository;

import com.danakga.webservice.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    //대표 이미지 변경을 위해서 사용
    @Modifying
    @Query("UPDATE Product p set p.productPhoto = :productMainPhoto where p.productId = :productId")
    void updateProductMainPhoto(@Param("productMainPhoto") String productPhoto,@Param("productId") Long productId);
}
