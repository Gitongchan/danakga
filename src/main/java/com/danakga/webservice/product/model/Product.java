package com.danakga.webservice.product.model;

import com.danakga.webservice.company.model.CompanyInfo;
import lombok.AccessLevel;
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

    //상품종류
    //변경예정
    @Column(name = "pd_type")
    private String productType;

    //브랜드
    //추후 변경 예정
    @Column(name = "pd_brand")
    private String productBrand;

    //상품상태
    @Column(name = "pd_state")
    private String productState;

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

    //상품등록일
    @Column(name = "pd_upload_date")
    private LocalDateTime productUploadDate;

    //조회수
    @Column(name = "pd_view_cnt")
    private Integer productViewCount;

    //누적구매수
    @Column(name = "pd_order_cnt")
    private Integer productOrderCount;




}
