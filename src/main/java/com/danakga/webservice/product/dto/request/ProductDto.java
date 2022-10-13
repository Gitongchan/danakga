package com.danakga.webservice.product.dto.request;

import com.danakga.webservice.company.model.CompanyInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    //상품번호, 상품아이디
    private Long productId;

    //사업자 등록 번호
    private CompanyInfo productCompanyId;

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
    
    //상품내용
    private String productContent;
    
    //가격
    private Integer productPrice;
    
    //재고
    private Integer productStock;
    
    //상품등록일
    private LocalDateTime productUploadDate;

    //조회수
    private Integer productViewCount;

    //누적구매수
    private Integer productOrderCount;
}
