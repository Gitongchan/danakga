package com.danakga.webservice.admin.dto.response;

import com.danakga.webservice.company.model.CompanyInfo;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ResManagerInfoDetailDto {
    private String userid;
    private String password;
    private String email;
    private String name;
    private String phone;
    private UserRole role;
    private String userAdrNum;
    private String userStreetAdr;
    private String userLotAdr;
    private String userDetailAdr;
    private boolean userEnabled;
    private LocalDateTime userDeletedDate;

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

    //사업자 탈퇴 여부
    private boolean companyEnabled;

    //사업자 탈퇴 날짜
    private LocalDateTime companyDeletedDate;




    public ResManagerInfoDetailDto(CompanyInfo companyInfo) {
        this.userid = companyInfo.getUserInfo().getUserid();
        this.password = companyInfo.getUserInfo().getPassword();
        this.email = companyInfo.getUserInfo().getEmail();
        this.name = companyInfo.getUserInfo().getName();
        this.phone = companyInfo.getUserInfo().getPhone();
        this.role = companyInfo.getUserInfo().getRole();
        this.userAdrNum = companyInfo.getUserInfo().getUserAdrNum();
        this.userStreetAdr = companyInfo.getUserInfo().getUserStreetAdr();
        this.userLotAdr = companyInfo.getUserInfo().getUserLotAdr();
        this.userDetailAdr = companyInfo.getUserInfo().getUserDetailAdr();
        this.userEnabled = companyInfo.getUserInfo().isUserEnabled();
        this.userDeletedDate = companyInfo.getUserInfo().getUserDeletedDate();
        

        //회사 정보
        this.companyId = companyInfo.getCompanyId();
        this.companyName = companyInfo.getCompanyName();
        this.companyNum = companyInfo.getCompanyNum();
        this.companyAdrNum = companyInfo.getCompanyAdrNum();
        this.companyLotAdr = companyInfo.getCompanyLotAdr();
        this.companyStreetAdr = companyInfo.getCompanyStreetAdr();
        this.companyDetailAdr = companyInfo.getCompanyDetailAdr();
        this.companyBanknum = companyInfo.getCompanyBanknum();
        this.companyEnabled = companyInfo.isCompanyEnabled();
        this.companyDeletedDate = companyInfo.getCompanyDeletedDate();
    }
}
