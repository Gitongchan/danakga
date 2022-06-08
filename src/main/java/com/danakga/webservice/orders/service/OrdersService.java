package com.danakga.webservice.orders.service;

import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.orders.dto.request.OrdersDto;
import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.user.model.UserInfo;
import org.springframework.stereotype.Service;

public interface OrdersService {
    Long ordersSave(UserInfo userInfo, Long productId, OrdersDto ordersDto);
}
