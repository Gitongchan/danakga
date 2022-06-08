package com.danakga.webservice.orders.model;

import com.danakga.webservice.user.model.UserInfo;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

public enum OrderStatus {
    //주문취소는 상품 준비중일때만
    //주문취소,반품신청일때 사업자는 "환불완료" 처리만 가능
    //교환신청 일때는 사업자는 "교환상품배송"으로 처리
    //기본 적인 경우 상품준비중-> 배송시작(운송장번호 입력) -> 배송완료(완료일자 자동입력) -> 구매확정
    //구매확정 일때만 리뷰가능 
    
    READY("상품준비중"), CANCEL("주문취소"), EXCHANGE("교환신청") , RETURN("반품신청") ,CONFIRM("구매확정")

    ,START("배송시작"), FINISH("배송완료"), REFUND("환불완료") , REDELIVERY("교환상품배송")
    ;

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
