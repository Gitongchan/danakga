package com.danakga.webservice.user.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class UserInfo implements UserDetails {
    //공통부분
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "u_id")
    private Long id;

    @Column(name = "u_userid")
    private String userid;

    @Column(name = "u_password")
    private String password;

    @Column(name = "u_name")
    private String name;

    @Column(name = "u_phone")
    private String phone;

    @Column(name = "u_email")
    private String email;
    
    //권한
    @Column(name = "u_role")
    private String role;

    //회원 우편번호 
    @Column(name = "u_adr_num")
    private String userAdrNum;

    //회원 기본주소
    @Column(name = "u_def_adr")
    private String userDefAdr;

    //회원 상세 주소
    @Column(name = "u_detail_adr")
    private String userDetailAdr;

    /*                          --사업자 부분--                          */
    /*                    사업자 등록을 하면 추가되는 컬럼                  */
    //사업자등록번호
    @Column(name = "u_com_id")
    private String companyId;

    //회사명
    @Column(name = "u_com_name")
    private String companyName;

    //회사연락처
    @Column(name = "u_com_num")
    private String companyNum;

    //회사 우편번호
    @Column(name = "u_com_adr_num")
    private String companyAdrNum;

    //회사 기본주소
    @Column(name = "u_com_def_adr")
    private String companyDefNum;

    //회사 상세주소
    @Column(name = "u_com_detail_adr")
    private String companyDetailAdr;

    //회사 계좌
    @Column(name = "u_com_banknum")
    private String companyBanknum;

    /**                            탈퇴 관련                            **/

    //회원 탈퇴 여부
    @Column(name = "u_Deleted")
    private boolean userDeleted;

    //회원 탈퇴 날짜
    @Column(name = "u_deleted_date")
    @UpdateTimestamp
    private LocalDateTime userDeletedDate;

    //사업자 탈퇴 여부
    @Column(name = "u_com_deleted")
    private boolean companyDeleted;

    //사업자 탈퇴 날짜
    @Column(name = "u_com_deleted_date")
    private LocalDateTime companyDeltedDate;

    @Builder
    public UserInfo(Long id, String userid, String password, String name, String phone, String email,
                    String role, String userAdrNum, String userDefAdr, String userDetailAdr,
                    String companyId,
                    String companyName, String companyNum, String companyAdrNum,
                    String companyDefNum, String companyDetailAdr, String companyBanknum,
                    boolean userDeleted,LocalDateTime userDeletedDate,boolean companyDeleted,LocalDateTime companyDeltedDate) {
        this.id = id;
        this.userid = userid;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.role = role;
        this.userAdrNum = userAdrNum;
        this.userDefAdr = userDefAdr;
        this.userDetailAdr = userDetailAdr;
        this.companyId = companyId;
        this.companyName = companyName;
        this.companyNum = companyNum;
        this.companyAdrNum = companyAdrNum;
        this.companyDefNum = companyDefNum;
        this.companyDetailAdr = companyDetailAdr;
        this.companyBanknum = companyBanknum;
        this.userDeleted = userDeleted;
        this.userDeletedDate = userDeletedDate;
        this.companyDeleted = companyDeleted;
        this.companyDeltedDate = companyDeltedDate;
    }



    // 사용자의 권한을 콜렉션 형태로 반환
    // 단, 클래스 자료형은 GrantedAuthority를 구현해야함
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> roles = new HashSet<>();
        for (String role : role.split(",")) {
            roles.add(new SimpleGrantedAuthority(role));
        }
        return roles;
    }
    // 계정 사용 가능 여부 반환
    @Override
    public boolean isEnabled() {
        return userDeleted; // true -> 사용 가능
    }

    // 사용자의 id를 반환 (unique한 값)
    @Override
    public String getUsername() {
        return userid;
    }

    // 사용자의 password를 반환
    @Override
    public String getPassword() {
        return password;
    }

    // 계정 만료 여부 반환
    @Override
    public boolean isAccountNonExpired() {
        // 만료되었는지 확인하는 로직
        return true; // true -> 만료되지 않았음
    }

    // 계정 잠금 여부 반환
    @Override
    public boolean isAccountNonLocked() {
        // 계정 잠금되었는지 확인하는 로직
        return true; // true -> 잠금되지 않았음
    }

    // 패스워드의 만료 여부 반환
    @Override
    public boolean isCredentialsNonExpired() {
        // 패스워드가 만료되었는지 확인하는 로직
        return true; // true -> 만료되지 않았음
    }

}
