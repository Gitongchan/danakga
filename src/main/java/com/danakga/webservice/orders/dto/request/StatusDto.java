package com.danakga.webservice.orders.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusDto {

    //운송장 번호
    private String ordersTrackingNum;

    //주문 상태
    private String ordersStatus;

}
