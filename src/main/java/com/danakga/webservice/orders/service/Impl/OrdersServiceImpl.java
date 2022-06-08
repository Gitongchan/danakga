package com.danakga.webservice.orders.service.Impl;

import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.exception.CustomException;
import com.danakga.webservice.orders.dto.request.OrdersDto;
import com.danakga.webservice.orders.repository.OrdersRepository;
import com.danakga.webservice.orders.service.OrdersService;
import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.product.repository.ProductRepository;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrdersServiceImpl implements OrdersService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrdersRepository ordersRepository;
    
    @Override
    public Long ordersSave(UserInfo userInfo, Long productId, OrdersDto ordersDto) {
        UserInfo ordersUserInfo = userRepository.findById(userInfo.getId()).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("로그인 사용자를 찾을 수 없습니다")
        );
        Product product = productRepository.findByProductId(productId).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("해당 상품을 찾을 수 없습니다")
        );





        return null;
    }
}
