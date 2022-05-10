package com.danakga.webservice.company.dto.reponse;

import com.danakga.webservice.company.model.CompanyInfo;
import com.danakga.webservice.user.model.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResCompanyInfoDto {

    //사업자등록번호
    private Long companyId;

    //회사명
    private String companyName;

    //회사연락처
    private String companyNum;

    //회사 우편번호
    private String companyAdrNum;

    //회원 지번 주소
    private String companyLotAdr;

    //회원 도로명 주소
    private String companyStreetAdr;

    //회사 상세주소
    private String companyDetailAdr;

    //회사 계좌
    private String companyBanknum;

    //회원명
    private String username;



    public ResCompanyInfoDto(CompanyInfo companyInfo) {
        this.username = companyInfo.getUserInfo().getName();
        this.companyId = companyInfo.getCompanyId();
        this.companyName = companyInfo.getCompanyName();
        this.companyNum = companyInfo.getCompanyNum();
        this.companyAdrNum = companyInfo.getCompanyAdrNum();
        this.companyLotAdr = companyInfo.getCompanyLotAdr();
        this.companyStreetAdr = companyInfo.getCompanyStreetAdr();
        this.companyDetailAdr = companyInfo.getCompanyDetailAdr();
        this.companyBanknum = companyInfo.getCompanyBanknum();
    }


}
