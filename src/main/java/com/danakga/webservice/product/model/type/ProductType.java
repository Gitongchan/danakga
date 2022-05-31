package com.danakga.webservice.product.model.type;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@DynamicUpdate
public class ProductType {

    //상품 종류 아이디
    @Id
    @GeneratedValue
    @Column(name="product_type_id")
    private Long productTypeId;

    @Column(name="product_type_name")
    private Long productTypeName;
}

