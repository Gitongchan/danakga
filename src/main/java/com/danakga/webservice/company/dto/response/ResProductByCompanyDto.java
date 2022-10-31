package com.danakga.webservice.company.dto.response;
import com.danakga.webservice.product.dto.response.ResProductListDto;
import com.danakga.webservice.product.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResProductByCompanyDto {

    //회사명
    private String companyName;

    //회사연락처
    private String companyNum;

    //회원 도로명 주소
    private String companyStreetAdr;

    //회사 상세주소
    private String companyDetailAdr;

    //회사 은행명
    private String companyBankName;

    //회사 계좌
    private String companyBankNum;

    private List<ResProductListDto> productListDto;


}
