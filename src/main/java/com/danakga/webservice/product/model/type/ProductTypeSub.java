package com.danakga.webservice.product.model.type;

import com.danakga.webservice.company.model.CompanyInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@DynamicUpdate
public class ProductTypeSub {

    //상품 서브 종류 아이디
    @Id
    @GeneratedValue
    @Column(name="product_type_sub_id")
    private Long productTypeSubId;

    //상품 타입 외래키
    @ManyToOne
    @JoinColumn(name="pd_id")
    private ProductType productType;

    @Column(name="product_type_sub_name")
    private Long productTypeSubName;


}
