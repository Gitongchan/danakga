package com.danakga.webservice.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResManagerInfoDto {
    private String userid;
    private String email;
    private String name;
    private String phone;
    private boolean userEnabled;
    //사업자등록번호
    private Long companyId;
    //회사명
    private String companyName;
    //회사연락처
    private String companyNum;
}
