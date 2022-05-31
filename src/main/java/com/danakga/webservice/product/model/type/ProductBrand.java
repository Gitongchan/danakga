package com.danakga.webservice.product.model.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@DynamicUpdate
public class ProductBrand {

    //상품 종류 아이디
    @Id
    @GeneratedValue
    @Column(name="product_brand_id")
    private Long productBrandId;

    //상품 타입 외래키
    @ManyToOne
    @JoinColumn(name="pd_id")
    private ProductType productType;

    @Column(name="product_brand_name")
    private Long productBrandName;
}
