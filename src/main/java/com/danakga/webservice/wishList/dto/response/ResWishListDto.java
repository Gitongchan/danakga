package com.danakga.webservice.wishList.dto.response;

import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.user.model.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResWishListDto {

    //위시리스트 아이디
    private Long wishId;

    //가게 이름
    private String companyName;

    //상품 브랜드
    private String productBrand;

    //상품명
    private String productName;

    //상품 가격
    private String productPrice;

    //위시리스트 등록 날짜
    private LocalDateTime wishDate;

    private int totalPage;

    private long totalElement;
}
