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

    //회사아이디
    private Long companyId;

    //등록한 회사명
    private String companyName;

    //상품번호, 상품아이디
    private Long productId;

    //상품 대표이미지
    private String productPhoto;

    //상품 종류
    private String productType;

    //상품 서브 종류
    private String productSubType;

    //상품 브랜드
    private String productBrand;

    //상품명
    private String productName;

    //가격
    private Integer productPrice;

    //상품내용
    private String productContent;

    //재고
    private Integer productStock;

    //상품등록일
    private LocalDateTime productUploadDate;

    //조회수
    private Integer productViewCount;

    //누적구매수
    private Integer productOrderCount;

    //찜 카운트
    private Long productWishCount;

    // 후기 평균 점수
    private Double productRating;

    //파일
    private List<Map<String,Object>> files;

    public ResProductDto(Product product, CompanyInfo companyInfo,Long productWishCount,double productRating) {
        this.companyId = companyInfo.getCompanyId();
        this.companyName = companyInfo.getCompanyName();
        this.productId = product.getProductId();
        this.productPhoto = product.getProductPhoto();
        this.productType = product.getProductType();
        this.productSubType = product.getProductSubType();
        this.productBrand = product.getProductBrand();
        this.productName = product.getProductName();
        this.productPrice = product.getProductPrice();
        this.productContent = product.getProductContent();
        this.productStock = product.getProductStock();
        this.productUploadDate = product.getProductUploadDate();
        this.productViewCount = product.getProductViewCount();
        this.productOrderCount = product.getProductOrderCount();
        this.productWishCount = productWishCount;
        this.productRating = productRating;
    }
}
