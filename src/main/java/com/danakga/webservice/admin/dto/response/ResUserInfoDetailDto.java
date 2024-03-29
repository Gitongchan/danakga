package com.danakga.webservice.admin.dto.response;

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
public class ResUserInfoDetailDto {
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
    private LocalDateTime userDeletedDate;



    public ResUserInfoDetailDto(UserInfo userInfo) {
        this.userid = userInfo.getUserid();
        this.password = userInfo.getPassword();
        this.email = userInfo.getEmail();
        this.name = userInfo.getName();
        this.phone = userInfo.getPhone();
        this.role = userInfo.getRole();
        this.userAdrNum = userInfo.getUserAdrNum();
        this.userStreetAdr = userInfo.getUserStreetAdr();
        this.userLotAdr = userInfo.getUserLotAdr();
        this.userDetailAdr = userInfo.getUserDetailAdr();
        this.userDeletedDate = userInfo.getUserDeletedDate();
    }
}
