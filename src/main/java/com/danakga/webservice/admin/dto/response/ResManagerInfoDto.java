package com.danakga.webservice.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResManagerInfoDto {
    private String userid;
    private String email;
    private String name;
    private String phone;
    private boolean userEnabled;
    private LocalDateTime userDeletedDate;
    //사업자등록번호
    private Long companyId;
    //회사명
    private String companyName;
    //회사연락처
    private String companyNum;
    //사업자 탈퇴 여부
    private boolean companyEnabled;
    //사업자 탈퇴 날짜
    private LocalDateTime companyDeletedDate;
}
