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
import java.time.LocalDateTime;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {
    //대표 이미지 변경을 위해서 사용
    @Modifying
    @Query("UPDATE Product p set p.productPhoto = :productMainPhoto where p.productId = :productId")
    void updateProductMainPhoto(@Param("productMainPhoto") String productPhoto,@Param("productId") Long productId);

    //상품 리스트 검색
    @Query("Select p from Product p where p.productType like :productType " +
            "and p.productSubType like :productSubType " +
            "and p.productBrand like :productBrand " +
            "and p.productName like :productName " +
            "and p.productStock >= :productStock")
    Page<Product>
    searchProductList(
            @Param("productType") String productType, @Param("productSubType") String productSubType
            ,@Param("productBrand") String productBrand,
            @Param("productName") String productName, @Param("productStock") Integer productStock, Pageable pageable
    );

    //내가 등록한 상품 리스트 검색
    @Query("Select p from Product p where p.productName like :productName " +
            "and p.productStock >= :productStock " +
            "and p.productUploadDate between :startDate and :endDate " +
            "and p.productCompanyId = :company")
    Page<Product>
    searchMyProductList(
            @Param("productName") String productName, @Param("productStock") Integer productStock,
            @Param("startDate")LocalDateTime startDate, @Param("endDate")LocalDateTime endDate,
            @Param("company")CompanyInfo companyInfo,
            Pageable pageable
    );


    Optional<Product> findByProductId(Long productId);

    Optional<Product> findByProductIdAndProductCompanyId(Long productId,CompanyInfo companyInfo);

    //조회수 증가
    @Transactional
    @Modifying
    @Query("update Product p set p.productViewCount = p.productViewCount + 1 where p.productId =:productId")
    void updateProductView(@Param("productId") Long productId);


    @Transactional
    @Modifying
    void deleteByProductIdAndProductCompanyId(Long productId, CompanyInfo companyInfo);

    @Transactional
    @Modifying
    @Query("update Product p set p.productStock = p.productStock - :ordersQuantity where p.productId = :productId")
    void updateProductStock(@Param("ordersQuantity") int ordersQuantity,@Param("productId") Long productId);

}
