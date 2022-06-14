package com.danakga.webservice.product.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyProductSearchDto {
    private String productName;
    private Integer productStock;  //품절 확인을 위함
}
