package com.danakga.webservice.product.model;

import com.danakga.webservice.company.model.CompanyInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@DynamicUpdate
public class Product {

    //상품번호, 상품아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="pd_id")
    private Long productId;

    //사업자 등록 번호
    @ManyToOne
    @JoinColumn(name="pd_com_id")
    private CompanyInfo productCompanyId;

    @Column(name = "pd_type")
    private String productType;

    @Column(name = "pd_sub_type")
    private String productSubType;

    @Column(name = "pd_brand")
    private String productBrand;

    //상품명
    @Column(name = "pd_name")
    private String productName;

    //상품대표사진
    @Column(name = "pd_photo")
    private String productPhoto;

    //상품내용
    @Column(name = "pd_content")
    private String productContent;

    //가격
    @Column(name = "pd_price")
    private String productPrice;

    //재고
    @Column(name = "pd_stock")
    private Integer productStock;

    //상품등록일 (+수정 날짜)
    @Column(name = "pd_upload_date")
    private LocalDateTime productUploadDate;

    //조회수
    @Column(name = "pd_view_cnt")
    private Integer productViewCount;

    //누적구매수
    @Column(name = "pd_order_cnt")
    private Integer productOrderCount;

    @Builder
    public Product(Long productId, CompanyInfo productCompanyId, String productType,
                   String productSubType, String productBrand, String productState,
                   String productName, String productPhoto, String productContent,
                   String productPrice, Integer productStock, LocalDateTime productUploadDate,
                   Integer productViewCount, Integer productOrderCount) {
        this.productId = productId;
        this.productCompanyId = productCompanyId;
        this.productType = productType;
        this.productSubType = productSubType;
        this.productBrand = productBrand;
        this.productState = productState;
        this.productName = productName;
        this.productPhoto = productPhoto;
        this.productContent = productContent;
        this.productPrice = productPrice;
        this.productStock = productStock;
        this.productUploadDate = productUploadDate;
        this.productViewCount = productViewCount;
        this.productOrderCount = productOrderCount;
    }
}
