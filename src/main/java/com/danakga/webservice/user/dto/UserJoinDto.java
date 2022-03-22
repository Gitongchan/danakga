package com.danakga.webservice.user.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class UserJoinDto {
    
    private Long id;
    
    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    private String userid;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password;

    @NotBlank(message = "사용자 이름은 필수 입력 값입니다.")
    private String username;

    @NotBlank(message = "핸드폰번호는 필수 입력 값입니다.")
    private String phone;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
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
