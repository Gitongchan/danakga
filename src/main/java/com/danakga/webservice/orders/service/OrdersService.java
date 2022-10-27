package com.danakga.webservice.orders.service;

import com.danakga.webservice.orders.dto.response.ResDailyRevenueDto;
import com.danakga.webservice.orders.dto.request.OrdersDto;
import com.danakga.webservice.orders.dto.request.StatusDto;
import com.danakga.webservice.orders.dto.response.ResOrdersDto;
import com.danakga.webservice.orders.dto.response.ResOrdersListDto;
import com.danakga.webservice.orders.dto.response.ResSalesDto;
import com.danakga.webservice.orders.dto.response.ResSalesListDto;
import com.danakga.webservice.user.model.UserInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

public interface OrdersService {

    //주문하기
    Long ordersSave(UserInfo userInfo, List<OrdersDto> ordersDto);
    
    //주문내역
    List<ResOrdersListDto> ordersList(UserInfo userInfo, Pageable pageable, int page,
                                      LocalDateTime startDate,LocalDateTime endDate);

    //주문상세내역
    ResOrdersDto ordersDetail(UserInfo userInfo, Long ordersId);

    //판매내역
    List<ResSalesListDto> salesList(UserInfo userInfo,Pageable pageable,int page,
                                    LocalDateTime startDate, LocalDateTime endDate,
                                    String ordersStatus,String searchRequirements, String searchWord);

    //판매 상세 내역
    ResSalesDto salesDetail(UserInfo userInfo,Long ordersId);

    //판매내역 상태 업데이트
    Long updateSalesStatus(UserInfo userInfo,Long ordersId,StatusDto statusDto);

    //판매내역 상태 업데이트
    Long updateOrdersStatus(UserInfo userInfo,Long ordersId,StatusDto statusDto);

    //기간별 판매금액 조회 (수익금)
    List<ResDailyRevenueDto> saleRevenue(UserInfo userInfo, String stateByPeriod,
                                         String startDate,
                                         String endDate);

    
}
