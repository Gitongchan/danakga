package com.danakga.webservice.product.repository;

import com.danakga.webservice.board.model.Board;
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
    @Query("Select p from Product p join p.productCompanyId c where p.productType like %:productType% " +
            "and p.productSubType like %:productSubType% " +
            "and p.productBrand like %:productBrand% " +
            "and p.productName like %:productName% " +
            "and p.productStock >= :productStock " +
            "and c.companyEnabled = true")
    Page<Product>
    searchProductList(
            @Param("productType") String productType, @Param("productSubType") String productSubType
            ,@Param("productBrand") String productBrand,
            @Param("productName") String productName, @Param("productStock") Integer productStock, Pageable pageable
    );

    //내가 등록한 상품 리스트 검색
    @Query("Select p from Product p where p.productName like %:productName% " +
            "and p.productType like %:productType% " +
            "and p.productSubType like %:productSubType% " +
            "and p.productBrand like %:productBrand% " +
            "and p.productStock >= :productStock " +
            "and p.productUploadDate between :startDate and :endDate " +
            "and p.productPrice between :startPrice and :endPrice " +
            "and p.productCompanyId = :company")
    Page<Product>
    searchMyProductList(
            @Param("productName") String productName, @Param("productStock") Integer productStock,
            @Param("productType") String productType,@Param("productSubType") String productSubType,
            @Param("productBrand") String productBrand,
            @Param("startDate")LocalDateTime startDate, @Param("endDate")LocalDateTime endDate,
            @Param("startPrice") Integer startPrice,@Param("endPrice") Integer endPrice,
            @Param("company")CompanyInfo companyInfo,
            Pageable pageable
    );


    @Query("select p from Product p join p.productCompanyId c " +
            "where p.productId = :productId and c.companyEnabled = true")
    Optional<Product> findByProductIdAndCompanyEnabled(Long productId);

    Optional<Product> findByProductIdAndProductCompanyId(Long productId,CompanyInfo companyInfo);

    //조회수 증가
    @Transactional
    @Modifying
    @Query("update Product p set p.productViewCount = p.productViewCount + 1 where p.productId =:productId")
    void updateProductView(@Param("productId") Long productId);

    //상품별 평점
    @Query("select Avg(r.reScore) from Review r where r.product = :product")
    Double selectProductRating(@Param("product") Product product);

    @Transactional
    @Modifying
    void deleteByProductIdAndProductCompanyId(Long productId, CompanyInfo companyInfo);

    @Transactional
    @Modifying
    @Query("update Product p set p.productStock = p.productStock - :ordersQuantity where p.productId = :productId")
    void updateProductStock(@Param("ordersQuantity") int ordersQuantity,@Param("productId") Long productId);


    /* 관리자 상품 목록 (진모) */
    
    /* 상품 전체 조회 */
    Page<Product> findAll(Pageable pageable);
    
    /* 상품 검색 */
    @Query(
            value = "Select p from Product p join p.productCompanyId c "
                    + "where (c.companyName Like %:content% "
                    + "or p.productBrand Like %:content% "
                    + "or p.productName Like %:content%) "
                    + "and c.companyEnabled = true"
    )
    Page<Product> adminProductSearch(@Param("content") String content,
                                     Pageable pageable);

    /* 상품명 검색 */
    @Query(
            value = "select p from Product p join p.productCompanyId c "
                    + "where (p.productName Like %:content%) "
                    + "and c.companyEnabled = true"
    )
    Page<Product> adminProductName(@Param("content") String content,
                                      Pageable pageable);

    /* 상품 브랜드명 검색 */
    @Query(
            value = "select p from Product p join p.productCompanyId c "
                    + "where (p.productBrand Like %:content%) "
                    + "and c.companyEnabled = true"
    )
    Page<Product> adminProductBrand(@Param("content") String content,
                                      Pageable pageable);
    
    /* 가게명 검색 */
    @Query(
            value = "select p from Product p join p.productCompanyId c "
                    + "where (c.companyName Like %:content%) "
                    + "and c.companyEnabled = true"
    )
    Page<Product> adminProductComName(@Param("content") String content,
                                      Pageable pageable);
}
