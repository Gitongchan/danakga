package com.danakga.webservice.orders.repository;

import com.danakga.webservice.company.model.CompanyInfo;
import com.danakga.webservice.orders.model.Orders;
import com.danakga.webservice.user.model.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders,Integer>{

    Optional<Orders> findByUserInfo(UserInfo userinfo);

    @Query(
            value = "select o from Orders o join o.product p " +
                    "where o.userInfo = :userInfo " +
                    "and o.ordersDate between :startDate and :endDate"
    )
    Page<Orders> ordersList(@Param("userInfo") UserInfo userInfo, Pageable pageable,
                            @Param("startDate")LocalDateTime startDate,
                            @Param("endDate")LocalDateTime endDate);

    /*
    SELECT * FROM orders o,
(SELECT * FROM product p,company_info c
 WHERE p.pd_com_id = c.com_id
 AND com_id = 1) a
WHERE a.pd_id = o.orders_product;

  SELECT *,(SELECT distinct p1.pd_com_id FROM product p1,company_info c
 WHERE p1.pd_com_id = c.com_id
 AND com_id = 1) AS pdid from product p
JOIN orders o ON p.pd_id = o.orders_product;
     */

    @Query(
            value = "select o from Orders o join o.product p join p.productCompanyId c " +
                    "where c.companyId = :companyId " +
                    "and o.ordersDate between :startDate and :endDate"
    )
    Page<Orders> salesList(@Param("companyId")Long companyInfo, Pageable pageable,
                           @Param("startDate")LocalDateTime startDate,
                           @Param("endDate")LocalDateTime endDate);
}
