package com.danakga.webservice.orders.service.Impl;

import com.danakga.webservice.exception.CustomException;
import com.danakga.webservice.orders.dto.request.OrdersDto;
import com.danakga.webservice.orders.dto.response.ResOrdersListDto;
import com.danakga.webservice.orders.model.OrderStatus;
import com.danakga.webservice.orders.model.Orders;
import com.danakga.webservice.orders.repository.OrdersRepository;
import com.danakga.webservice.orders.service.OrdersService;
import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.product.repository.ProductRepository;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrdersServiceImpl implements OrdersService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrdersRepository ordersRepository;

    //상품 주문 등록
    @Override
    public Long ordersSave(UserInfo userInfo, Long productId, OrdersDto ordersDto) {
        UserInfo ordersUserInfo = userRepository.findById(userInfo.getId()).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("로그인 사용자를 찾을 수 없습니다")
        );
        Product ordersProduct = productRepository.findByProductId(productId).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("해당 상품을 찾을 수 없습니다")
        );

        Orders orders = ordersRepository.save(
                Orders.builder()
                        .userInfo(ordersUserInfo)
                        .product(ordersProduct)
                        .orderStatus(OrderStatus.READY.getStatus())
                        .ordersDate(LocalDateTime.now())
                        .ordersFinishedDate(null) //배송완료시에 입력됨
                        .ordersPrice(ordersDto.getOrdersPrice())
                        .ordersQuantity(ordersDto.getOrdersQuantity())
                        .ordersTrackingNum(null) //배송완료시에 입력됨
                        .build()
        );

        //제품 수량 변경
        if(ordersProduct.getProductStock() < ordersDto.getOrdersQuantity()){
            return -1L; // 재고 부족
        }else{
            productRepository.updateProductStock(ordersDto.getOrdersQuantity(),ordersProduct.getProductId());
        }

        return orders.getOrdersId();
    }

    //주문 내역
    @Override
    public List<ResOrdersListDto> ordersList(UserInfo userInfo, Pageable pageable, int page, LocalDateTime startTime,LocalDateTime endTime) {
        UserInfo ordersUserInfo = userRepository.findById(userInfo.getId()).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("로그인 사용자를 찾을 수 없습니다")
        );
        pageable = PageRequest.of(page, 10, Sort.by("ordersId").descending());

        Page<Orders> ordersPage = ordersRepository.ordersList(userInfo,pageable,startTime,endTime);
        List<Orders> ordersList = ordersPage.getContent();

        List<ResOrdersListDto> ordersListDto = new ArrayList<>();

        ordersList.forEach(entity->{
                ResOrdersListDto listDto = new ResOrdersListDto();
                listDto.setOrdersId(entity.getOrdersId());
                listDto.setOrdersDate(entity.getOrdersDate());
                listDto.setCompanyName(entity.getProduct().getProductCompanyId().getCompanyName());
                listDto.setProductBrand(entity.getProduct().getProductBrand());
                listDto.setProductName(entity.getProduct().getProductName());
                listDto.setOrdersQuantity(entity.getOrdersQuantity());
                listDto.setOrdersFinishedDate(entity.getOrdersFinishedDate());
                listDto.setOrderStatus(entity.getOrderStatus());
                listDto.setTotalPage(ordersPage.getTotalPages());
                listDto.setTotalElement(ordersPage.getTotalElements());
                ordersListDto.add(listDto);
                }
        );
        return ordersListDto;
    }


}
