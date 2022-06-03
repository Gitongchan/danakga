package com.danakga.webservice.product.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResProductDto {

    //등록한 회사명
    private String companyName;

    //상품등록자 아이디
    private String companyUserId;

    //상품번호, 상품아이디
    private Long productId;

    //상품 종류
    private String productType;

    //상품 서브 종류
    private String productSubType;

    //상품 브랜드
    private String productBrand;

    //상품명
    private String productName;

    //상품대표사진
    private String productPhoto;

    //가격
    private String productPrice;

    //재고
    private Integer productStock;

    //상품등록일
    private LocalDateTime productUploadDate;

    //조회수
    private Integer productViewCount;

    //누적구매수
    private Integer productOrderCount;

    //파일
    private List<Map<String,Object>> files;
}
