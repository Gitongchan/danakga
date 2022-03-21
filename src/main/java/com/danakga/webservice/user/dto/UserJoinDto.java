package com.danakga.webservice.user.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class UserJoinDto {
    private Long id;
    private String userid;
    private String password;
    private String username;
    private String phone;
    private String email;
    private String role;

    public UserJoinDto(Long id, String userid, String password, String username, String phone, String email, String role) {
        this.id = id;
        this.userid = userid;
        this.password = password;
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.role = role;
    }
}
