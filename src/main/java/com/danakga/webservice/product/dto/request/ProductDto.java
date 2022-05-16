package com.danakga.webservice.product.dto.request;

import com.danakga.webservice.company.model.CompanyInfo;

import javax.persistence.Column;
import java.time.LocalDateTime;

public class ProductDto {

    //상품번호, 상품아이디
    private Long productId;

    //사업자 등록 번호
    private CompanyInfo productCompanyId;

    //상품종류
    //추후에 변경예정
    private String productType;

    //브랜드
    //추후 변경예정
    private String productBrand;

    //상품상태
    private String productState;

    //상품명
    private String productName;
    
    //상품대표사진
    private String productPhoto;
    
    //상품내용
    private String productContent;
    
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
}
