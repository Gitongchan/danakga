package com.danakga.webservice.orders.repository;

import com.danakga.webservice.orders.dto.response.ResRevenueDto;
import com.danakga.webservice.orders.model.Orders;
import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.user.model.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders,Long>{

    Optional<Orders> findByOrdersId(Long ordersId);

    Optional<Orders> findByUserInfo(UserInfo userinfo);

    Optional<Orders> findByOrdersIdAndUserInfo(Long ordersId,UserInfo userInfo);

    /* 후기 작성을 위한 주문 번호, 상품 아이디 조회  -진모- */
    Optional<Orders> findByOrdersIdAndProduct(Long ordersId, Product product);


    @Query(
            value = "select o from Orders o join o.product p join p.productCompanyId c " +
                    "where c.companyId = :companyId and o.ordersId = :ordersId"
    )
    Optional<Orders> findByOrdersIdAndCompany(@Param("ordersId")Long ordersId,@Param("companyId")Long companyId);

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
                    "and o.ordersDate between :startDate and :endDate " +
                    "and o.ordersStatus like %:ordersStatus% " +
                    "and o.product.productType like %:productType% " +
                    "and o.product.productSubType like %:productSubType% " +
                    "and o.product.productName like %:productName% " +
                    "and o.product.productBrand like %:productBrand% " +
                    "and o.userInfo.name like %:userName% " +
                    "and o.userInfo.phone like %:userPhone% " +
                    "and o.userInfo.userid like %:userId% " +
                    "and concat(o.ordersId,'') like %:ordersId%"
    )
    Page<Orders> salesList(@Param("companyId")Long companyInfo, Pageable pageable,
                           @Param("startDate")LocalDateTime startDate,@Param("endDate")LocalDateTime endDate,
                           @Param("ordersStatus")String ordersStatus,
                           @Param("productType")String productType,@Param("productSubType") String productSubType,
                           @Param("productName")String productName,@Param("productBrand") String productBrand,
                           @Param("userName") String userName, @Param("userPhone")String userPhone,@Param("userId") String userId,
                           @Param("ordersId") String ordersId
                           );

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


    //일별 판매 수익금 조회
    @Query(
            value = "select new com.danakga.webservice.orders.dto.response.ResRevenueDto(" +
                    "function('date_format',o.ordersFinishedDate,'%Y-%m-%d'),sum(o.ordersPrice * o.ordersQuantity)) " +
                    "from Orders o join o.product p join p.productCompanyId c " +
                    "where c.companyId = :companyId " +
                    "and function('date_format',o.ordersFinishedDate,'%Y-%m-%d') BETWEEN :startDate AND :endDate " +
                    "group by function('date_format',o.ordersFinishedDate,'%Y-%m-%d')"
    )
    List<ResRevenueDto> dailySalesList(@Param("companyId")Long companyId,
                                       @Param("startDate")String startDate, @Param("endDate")String endDate);



    //주별 판매 수익금 조회
    @Query(
            value = "select new com.danakga.webservice.orders.dto.response.ResRevenueDto(" +
                    "function('date_format',o.ordersFinishedDate,'%Y%u'),sum(o.ordersPrice * o.ordersQuantity)) " +
                    "from Orders o join o.product p join p.productCompanyId c " +
                    "where c.companyId = :companyId " +
                    "and function('date_format',o.ordersFinishedDate,'%Y-%m-%d') BETWEEN :startDate AND :endDate " +
                    "group by function('date_format',o.ordersFinishedDate,'%Y%u')"
    )
    List<ResRevenueDto> weeklySalesList(@Param("companyId")Long companyId,
                                        @Param("startDate")String startDate, @Param("endDate")String endDate);


    // 월별 판매 수익금 조회
    @Query(
            value = "select new com.danakga.webservice.orders.dto.response.ResRevenueDto(" +
                    "function('date_format',o.ordersFinishedDate,'%Y-%m'),sum(o.ordersPrice * o.ordersQuantity)) " +
                    "from Orders o join o.product p join p.productCompanyId c " +
                    "where c.companyId = :companyId " +
                    "and function('date_format',o.ordersFinishedDate,'%Y-%m') BETWEEN :startDate AND :endDate " +
                    "group by function('date_format',o.ordersFinishedDate,'%Y-%m')"

    )
    List<ResRevenueDto> monthlySalesList(@Param("companyId")Long companyId,
                                         @Param("startDate")String startDate, @Param("endDate")String endDate);

}
