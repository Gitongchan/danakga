package com.danakga.webservice.orders.dto.response;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResRevenueDto {

    //날짜
    private String date;
    
    // 기간별 가격
    private long price;

}
