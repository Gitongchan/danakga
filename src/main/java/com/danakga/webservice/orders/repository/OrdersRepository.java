package com.danakga.webservice.orders.repository;

import com.danakga.webservice.company.model.CompanyInfo;
import com.danakga.webservice.orders.model.Orders;
import com.danakga.webservice.user.model.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders,Long>{

    Optional<Orders> findByOrdersId(Long ordersId);

    Optional<Orders> findByUserInfo(UserInfo userinfo);

    //주문내역 조회
    @Query(
            value = "select o from Orders o join o.product p " +
                    "where o.userInfo = :userInfo " +
                    "and o.ordersDate between :startDate and :endDate"
    )
    Page<Orders> ordersList(@Param("userInfo") UserInfo userInfo, Pageable pageable,
                            @Param("startDate")LocalDateTime startDate,
                            @Param("endDate")LocalDateTime endDate);
    
    //판매내역 조회
    @Query(
            value = "select o from Orders o join o.product p join p.productCompanyId c " +
                    "where c.companyId = :companyId " +
                    "and o.ordersDate between :startDate and :endDate"
    )
    Page<Orders> salesList(@Param("companyId")Long companyInfo, Pageable pageable,
                           @Param("startDate")LocalDateTime startDate,
                           @Param("endDate")LocalDateTime endDate);

    //운송장번호 업데이트
    @Modifying
    @Query(
            value = "update Orders o set o.ordersTrackingNum = :ordersTrackingNum " +
                    "where o.ordersId = (select o.ordersId from Orders o join o.product p join p.productCompanyId c where c.companyId = :companyId and o.ordersId = :ordersId)"

    )
    void updateSalesTrackingNum(@Param("ordersTrackingNum") String ordersTrackingNum,@Param("companyId") Long companyInfo,
                                 @Param("ordersId") Long ordersId);

    //주문 완료일자 업데이트
    @Modifying
    @Query(
            value = "update Orders o set o.ordersFinishedDate = :ordersFinishedDate " +
                    "where o.ordersId = (select o.ordersId from Orders o join o.product p join p.productCompanyId c where c.companyId = :companyId and o.ordersId = :ordersId)"

    )
    void updateSalesFinishedDate(@Param("ordersFinishedDate") LocalDateTime ordersFinishedDate,@Param("companyId") Long companyInfo,
                                  @Param("ordersId") Long ordersId);

    //주문 상태 업데이트
    @Modifying
    @Query(
            value = "update Orders o set o.ordersStatus = :ordersStatus " +
                    "where o.ordersId = (select o.ordersId from Orders o join o.product p join p.productCompanyId c where c.companyId = :companyId and o.ordersId = :ordersId)"
    )
    void updateSalesStatus(@Param("ordersStatus") String ordersStatus,@Param("companyId") Long companyInfo,
                            @Param("ordersId") Long ordersId);

    //일반 사용자(구매자)의 주문 상태 업데이트
    @Modifying
    @Query(
            value = "update Orders o set o.ordersStatus = :ordersStatus " +
                    "where o.ordersId = (select o.ordersId from Orders o join o.product p where o.userInfo = :userInfo and o.ordersId = :ordersId)"
    )
    void updateOrdersStatus(@Param("userInfo") UserInfo userInfo,@Param("ordersStatus") String ordersStatus,@Param("ordersId") Long ordersId);
}
