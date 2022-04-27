package com.danakga.webservice.company.dto.request;

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
public class CompanyInfoDto {

    //userinfo
    private UserInfo usreInfo;
    //사업자등록번호
    private Long companyId;

    //회사명
    private String companyName;

    //회사연락처
    private String companyNum;

    //회사 우편번호
    private String companyAdrNum;

    //회사 기본주소
    private String companyDefNum;

    //회사 상세주소
    private String companyDetailAdr;

    //회사 계좌
    private String companyBanknum;
    
    //사업자 탈퇴 여부
    private boolean companyEnabled;

    //사업자 탈퇴 날짜
    private LocalDateTime companyDeletedDate;

}
