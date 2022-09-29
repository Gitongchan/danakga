package com.danakga.webservice.cart.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResCartDto {

    //장바구니 아이디
    private Long cartId;

    //장바구니에 담은 상품 개수
    private long cartAmount;

    //회사아이디
    private Long companyId;

    //등록한 회사명
    private String companyName;

    //상품 아이디
    private Long productId;

    //상품 이름
    private String productName;

    //상품 대표이미지
    private String productPhoto;

    //상품 종류
    private String productType;

    //상품 서브 종류
    private String productSubType;

    //상품 브랜드
    private String productBrand;

    //가격
    private Integer productPrice;


}
