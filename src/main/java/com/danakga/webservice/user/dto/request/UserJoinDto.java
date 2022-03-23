package com.danakga.webservice.user.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserJoinDto {
    
    private Long id;
    
    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    private String userid;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-z])(?=.*\\W)(?=\\S+$).{6,12}",
            message = "비밀번호는 영문자와 숫자, 특수기호가 적어도 1개 이상 포함된 6자~12자의 비밀번호여야 합니다.")
    private String password;

    @NotBlank(message = "사용자명은 필수 입력 값입니다.")
    private String username;

    @NotBlank(message = "핸드폰번호는 필수 입력 값입니다.")
    private String phone;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email;

    private String role;


}
