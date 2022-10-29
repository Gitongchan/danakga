package com.danakga.webservice.company.repository;

import com.danakga.webservice.company.model.CompanyInfo;
import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.user.model.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<CompanyInfo,Long> {

    Optional<CompanyInfo> findByCompanyName(String companyName);

    Optional<CompanyInfo> findByUserInfo(UserInfo userInfo);

    Optional<CompanyInfo> findByUserInfoAndCompanyEnabled(UserInfo userInfo,boolean enabled);

    Optional<CompanyInfo> findByCompanyId(Long companyId);

    //상품 리스트 검색
    @Query("Select p from Product p join p.productCompanyId c " +
            "where c.companyName = :companyName " +
            "and p.productName like %:productName% " +
            "and p.productStock >= :productStock")
    Page<Product>
    searchProductByCompanyList(
            @Param("companyName") String companyName,
            @Param("productName") String productName, @Param("productStock") Integer productStock, Pageable pageable
    );
}
