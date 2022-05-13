package com.danakga.webservice.user.dto.request;

import com.danakga.webservice.user.model.UserInfo;
import lombok.Getter;
import org.springframework.security.core.userdetails.User;


@Getter
public class UserAdapter extends User {
    private final UserInfo userInfo;

    public UserAdapter(UserInfo userInfo) {
        super(userInfo.getUserid(),userInfo.getPassword(),userInfo.isUserEnabled() , userInfo.isAccountNonExpired(),
                userInfo.isCredentialsNonExpired(), userInfo.isAccountNonExpired(),
                userInfo.getAuthorities());
        this.userInfo = userInfo;
    }

}
