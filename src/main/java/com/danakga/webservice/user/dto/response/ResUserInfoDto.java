package com.danakga.webservice.user.dto.response;

import com.danakga.webservice.user.model.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResUserInfoDto {
    private Long id;
    private String userid;
    private String password;
    private String email;
    private String name;
    private String phone;
    private String role;
    private String userAdrNum;
    private String userStreetAdr;
    private String userLotAdr;
    private String userDetailAdr;


    public ResUserInfoDto(UserInfo userInfo) {
        this.id = userInfo.getId();
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
    }
}
