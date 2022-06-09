package com.danakga.webservice.product.repository;

import com.danakga.webservice.company.model.CompanyInfo;
import com.danakga.webservice.product.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    //대표 이미지 변경을 위해서 사용
    @Modifying
    @Query("UPDATE Product p set p.productPhoto = :productMainPhoto where p.productId = :productId")
    void updateProductMainPhoto(@Param("productMainPhoto") String productPhoto,@Param("productId") Long productId);

    //상품 리스트 검색
    Page<Product>
    findByProductTypeLikeAndProductSubTypeLikeAndProductBrandLikeAndProductNameLikeAndProductStockGreaterThanEqual(
            String productType, String productSubType, String productBrand,
            String productName, Integer productStock, Pageable pageable
    );

    Optional<Product> findByProductId(Long productId);

    Optional<Product> findByProductIdAndProductCompanyId(Long productId,CompanyInfo companyInfo);

    //조회수 증가
    @Transactional
    @Modifying
    @Query("update Product p set p.productViewCount = p.productViewCount + 1 where p.productId =:productId")
    void updateProductView(@Param("productId") Long productId);

    //대표이미지 null로 변경
    @Transactional
    @Modifying
    @Query("update Product p set p.productPhoto = 0 where p.productId = :productId")
    void deleteProductPhoto(@Param("productId") Long productId);

    @Transactional
    @Modifying
    void deleteByProductIdAndProductCompanyId(Long productId, CompanyInfo companyInfo);

    @Transactional
    @Modifying
    @Query("update Product p set p.productStock = p.productStock - :ordersQuantity where p.productId = :productId")
    void updateProductStock(@Param("ordersQuantity") int ordersQuantity,@Param("productId") Long productId);
}
