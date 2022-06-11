package com.danakga.webservice.orders.service;

import com.danakga.webservice.orders.dto.request.OrdersDto;
import com.danakga.webservice.orders.dto.request.StatusDto;
import com.danakga.webservice.orders.dto.response.ResOrdersListDto;
import com.danakga.webservice.orders.dto.response.ResSalesListDto;
import com.danakga.webservice.user.model.UserInfo;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface OrdersService {

    //주문하기
    Long ordersSave(UserInfo userInfo, Long productId, OrdersDto ordersDto);
    
    //주문내역
    List<ResOrdersListDto> ordersList(UserInfo userInfo, Pageable pageable, int page,
                                      LocalDateTime startTime,LocalDateTime endTime);

    //판매내역
    List<ResSalesListDto> salesList(UserInfo userInfo,Pageable pageable,int page,
                                    LocalDateTime startTime, LocalDateTime endTime);

    //판매내역 상태 업데이트
    Long updateSalesStatus(UserInfo userInfo,Long ordersId,StatusDto statusDto);

    
}
