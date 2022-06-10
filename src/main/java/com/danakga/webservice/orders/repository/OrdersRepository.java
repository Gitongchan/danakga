package com.danakga.webservice.orders.repository;

import com.danakga.webservice.orders.model.Orders;
import com.danakga.webservice.user.model.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders,Integer>{
    Optional<Orders> findByUserInfo(UserInfo userinfo);

    @Query(
            value = "select o from Orders o join fetch o.product where o.userInfo = :userInfo",
            countQuery = "select count(o) from Orders o  join fetch o.product where o.userInfo = :userInfo"
    )
    Page<Orders> findAllByUserInfoAndJoinFetch(@Param("userInfo") UserInfo userInfo, Pageable pageable);
}
