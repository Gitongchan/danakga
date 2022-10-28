package com.danakga.webservice.orders.controller;

import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.orders.dto.response.ResRevenueDto;
import com.danakga.webservice.orders.dto.request.OrdersDto;
import com.danakga.webservice.orders.dto.request.StatusDto;
import com.danakga.webservice.orders.dto.response.ResOrdersDto;
import com.danakga.webservice.orders.dto.response.ResOrdersListDto;
import com.danakga.webservice.orders.dto.response.ResSalesDto;
import com.danakga.webservice.orders.dto.response.ResSalesListDto;
import com.danakga.webservice.orders.service.OrdersService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrdersController {
    private final OrdersService ordersService;

    //주문하기
    @PostMapping("/api/user/orders")
    public ResResultDto ordersSave(@LoginUser UserInfo userInfo,@RequestBody List<OrdersDto> ordersDto){
        Long result = ordersService.ordersSave(userInfo,ordersDto);
        return result == -1L ?
                new ResResultDto(result,"재고가 부족한 상품이 포함되어있습니다.") : new ResResultDto(result,"성공적으로 주문되었습니다.");
    }

    //주문내역
    @GetMapping("api/user/orders/list")
    public List<ResOrdersListDto> myOrdersList(@LoginUser UserInfo userInfo, Pageable pageable, int page,
                                               @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                               @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate){

        return ordersService.ordersList(userInfo,pageable,page,startDate,endDate);
    }

    //주문 상세 내역
    @GetMapping("api/user/orders/list/detail/{ordersId}")
    public ResOrdersDto myOrdersDetail(@LoginUser UserInfo userInfo,@PathVariable("ordersId") Long ordersId){
        return ordersService.ordersDetail(userInfo,ordersId);
    }

    
    //판매내역
    @GetMapping("api/manager/sales/list")
    public List<ResSalesListDto> mySalesList(@LoginUser UserInfo userInfo, Pageable pageable, int page,
                                             @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
                                             @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
                                             @RequestParam("ordersStatus") String ordersStatus,
                                             @RequestParam("searchRequirements") String searchRequirements,
                                             @RequestParam("searchWord") String searchWord
    ){
        return ordersService.salesList(userInfo,pageable,page,startTime,endTime,ordersStatus,searchRequirements,searchWord);
    }

    //판매상세내역
    @GetMapping("api/manager/sales/detail/{ordersId}")
    public ResSalesDto mySalesDetail(@LoginUser UserInfo userInfo,@PathVariable("ordersId") Long ordersId){
        return ordersService.salesDetail(userInfo,ordersId);
    }
    
    //사업자 - 판매내역 업데이트
    @PutMapping("api/manager/sales/updateStatus/{item}")
    public ResResultDto updateSalesStatus(@LoginUser UserInfo userInfo, @PathVariable("item") Long ordersId,@RequestBody StatusDto statusDto){
        Long result = ordersService.updateSalesStatus(userInfo,ordersId,statusDto);
        return result == -1L ?
                new ResResultDto(result,"더 이상 변경할 수 없는 상태입니다.") : new ResResultDto(result,"상태변경을 성공했습니다.") ;
    }

    //일반사용자(구매자) - 주문내역 업데이트
    @PutMapping("api/user/orders/updateStatus/{item}")
    public ResResultDto updateOrdersStatus(@LoginUser UserInfo userInfo, @PathVariable("item") Long ordersId,@RequestBody StatusDto statusDto){
        Long result = ordersService.updateOrdersStatus(userInfo,ordersId,statusDto);
        return result == -1L ?
                new ResResultDto(result,"더 이상 변경할 수 없는 상태입니다.") : new ResResultDto(result,"상태변경을 성공했습니다.") ;
    }

    //기간별 수익 (기간별 판매 금액)
    @GetMapping("api/manager/sales/statistics/revenue")
    public  List<ResRevenueDto> revenueDto(@LoginUser UserInfo userInfo, @RequestParam String stateByPeriod,
                                           @RequestParam("startTime") String startDate,
                                           @RequestParam("endTime") String endDate)
    {
        return ordersService.saleRevenue(userInfo,stateByPeriod,startDate,endDate);
    }

}
