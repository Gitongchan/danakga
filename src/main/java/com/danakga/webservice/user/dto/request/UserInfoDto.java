package com.danakga.webservice.user.dto.request;

import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.model.UserRole;
import lombok.*;
import org.apache.tomcat.jni.Local;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {
    //일반사용자 Dto

    private Long id;

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    private String userid;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*\\W)(?=\\S+$).{6,12}",
            message = "비밀번호는 영문자와 숫자, 특수기호가 적어도 1개 이상 포함된 6자~12자의 비밀번호여야 합니다.")
    private String password;

    @NotBlank(message = "사용자명은 필수 입력 값입니다.")
    @Size(max = 20, message = "이름의 최대 길이는 20자 입니다.")
    private String name;

    @NotBlank(message = "핸드폰번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^[0-9]+$", message = "핸드폰번호는 숫자만 허용합니다.")
    private String phone;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식에 맞지 않습니다")
    private String email;

    //권한 ( user,manager,admin)
    private UserRole role;

    //회원 우편번호
    private String userAdrNum;

    //회원 지번 주소
    private String userLotAdr;

    //회원 도로명 주소
    private String userStreetAdr;

    //회원 상세 주소
    private String userDetailAdr;

    //회원 탈퇴 여부
    private boolean userEnabled;

    //회원 탈퇴 날짜
    private LocalDateTime userDeletedDate;

}
