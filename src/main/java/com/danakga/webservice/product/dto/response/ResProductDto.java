package com.danakga.webservice.product.dto.response;

import com.danakga.webservice.company.model.CompanyInfo;
import com.danakga.webservice.product.model.Product;
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

    public ResProductDto(Product product, CompanyInfo companyInfo) {
        this.companyName = companyInfo.getCompanyName();
        this.productId = product.getProductId();
        this.productType = product.getProductType();
        this.productSubType = product.getProductSubType();
        this.productBrand = product.getProductBrand();
        this.productName = product.getProductName();
        this.productPrice = product.getProductPrice();
        this.productStock = product.getProductStock();
        this.productUploadDate = product.getProductUploadDate();
        this.productViewCount = product.getProductViewCount();
        this.productOrderCount = product.getProductOrderCount();
    }
}
