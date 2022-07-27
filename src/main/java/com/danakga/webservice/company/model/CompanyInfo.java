package com.danakga.webservice.company.model;

import com.danakga.webservice.user.model.UserInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@DynamicUpdate
public class CompanyInfo {

    //사업자등록번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="com_id")
    private Long companyId;

    //유저 정보 외래키
    @OneToOne
    @JoinColumn(name="com_userInfo")
    private UserInfo userInfo;

    //회사명
    @Column(name = "com_name")
    private String companyName;

    //회사연락처
    @Column(name = "com_num")
    private String companyNum;

    //회사 우편번호
    @Column(name = "com_adr_num")
    private String companyAdrNum;

    //회사 도로명 주소
    @Column(name = "com_street_adr")
    private String companyStreetAdr;

    //회사 지번 주소
    @Column(name = "com_lot_adr")
    private String companyLotAdr;

    //회사 상세주소
    @Column(name = "com_detail_adr")
    private String companyDetailAdr;

    //회사 은행명
    @Column(name = "com_bank_name")
    private String companyBankName;

    //회사 계좌
    @Column(name = "com_banknum")
    private String companyBanknum;

    //사업자 탈퇴 여부
    @Column(name = "com_enabled")
    private boolean companyEnabled;

    //사업자 탈퇴 날짜
    @Column(name = "com_deleted_date")
    private LocalDateTime companyDeletedDate;

    @Builder
    public CompanyInfo(Long companyId, UserInfo userInfo, String companyName, String companyNum,
                       String companyAdrNum, String companyStreetAdr,String companyLotAdr, String companyDetailAdr,
                       String companyBankName,String companyBanknum, boolean companyEnabled, LocalDateTime companyDeletedDate) {
        this.companyId = companyId;
        this.userInfo = userInfo;
        this.companyName = companyName;
        this.companyNum = companyNum;
        this.companyAdrNum = companyAdrNum;
        this.companyStreetAdr = companyStreetAdr;
        this.companyLotAdr = companyLotAdr;
        this.companyDetailAdr = companyDetailAdr;
        this.companyBankName = companyBankName;
        this.companyBanknum = companyBanknum;
        this.companyEnabled = companyEnabled;
        this.companyDeletedDate = companyDeletedDate;
    }
}
