package com.danakga.webservice.product.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchDto {
    private String productType;
    private String productSubType;
    private String productBrand;
    private String productName;
    private Integer productStock;  //품절 확인을 위함
}
