package com.danakga.webservice.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResUserInfoDto {
    private String userid;
    private String email;
    private String name;
    private String phone;
    private boolean userEnabled;
    private LocalDateTime userDeletedDate;
}
