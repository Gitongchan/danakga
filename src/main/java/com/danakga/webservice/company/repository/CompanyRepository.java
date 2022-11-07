package com.danakga.webservice.company.repository;

import com.danakga.webservice.company.model.CompanyInfo;
import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.model.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<CompanyInfo,Long> {

    Optional<CompanyInfo> findByCompanyName(String companyName);

    Optional<CompanyInfo> findByCompanyNameAndCompanyEnabled(String companyName,boolean enabled);

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

    //어드민 - 사업자 유저 목록 조회
    @Query(value="select c from CompanyInfo c join c.userInfo u " +
            "where u.name like %:userName% " +
            "and u.userid like %:userId% " +
            "and u.role = :userRole " +
            "and u.userEnabled <= :userEnabled " +
            "and c.companyEnabled <= :companyEnabled " +
            "and c.companyName like %:companyName% " +
            "and c.companyNum like %:companyNum%")
    Page<CompanyInfo> findAllManagerInfo(UserRole userRole,
                                      String userName,String userId ,boolean userEnabled,boolean companyEnabled,
                                      String companyName,String companyNum,Pageable pageable);


}
