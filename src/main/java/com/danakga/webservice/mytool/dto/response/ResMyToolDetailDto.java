package com.danakga.webservice.mytool.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResMyToolDetailDto {

    //내 장비 폴더 아이디
    private Long myToolFolderId;

    //내 장비 아이디
    private Long myToolId;

    //등록된 상품 아이디
    private Long myToolProductId;

    //상품 종류
    private String myToolProductType;

    //상품 서브 종류
    private String myToolProductSubType;

    //상품 브랜드
    private String myToolProductBrand;
    
    //상품명
    private String myToolProductName;

    //가격
    private String myToolProductPrice;

    //수량
    private int myToolQuantity;
}
