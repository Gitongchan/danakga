package com.danakga.webservice.orders.repository;

import com.danakga.webservice.orders.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders,Integer>{
}
