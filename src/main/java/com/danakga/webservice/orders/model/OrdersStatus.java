package com.danakga.webservice.orders.model;

public enum OrdersStatus {
    //상품 준비중일때는 사업자는 배송시작,주문거절 / 일반사용자는 주문취소만 가능
    //주문취소,반품신청일때 사업자는 "환불완료" 처리만 가능
    //배송완료면 교환신청,반품신청,구매확정
    //교환신청 일때는 사업자는 "교환상품배송"으로 처리, 운송장번호 같이입력  , 이후 사업자는 배송완료 처리 해주어야한다
    //기본 적인 경우 상품준비중-> 배송시작(운송장번호 입력) -> 배송완료(완료일자 자동입력) -> 구매확정
    //구매확정 일때만 리뷰가능

    READY("상품준비중"), CANCEL("주문취소"), EXCHANGE("교환신청") , RETURN("반품신청") ,CONFIRM("구매확정")
    ,REJECT("주문거절"),START("배송시작"), FINISH("배송완료"), REFUND("환불완료") , REDELIVERY("교환상품배송");

    private final String status;

    OrdersStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }


}
