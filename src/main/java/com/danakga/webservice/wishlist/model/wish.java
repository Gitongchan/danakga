package com.danakga.webservice.wishlist.model;

import com.danakga.webservice.user.model.UserInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)//무분별한 객체 생성에 대해 한번 더 체크할 수 있는 수단
public class wish {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "wish_id")
        private Long wish_id;

        @Column(name = "pd_id")
        private  pd_id;

        @ManyToOne
        @JoinColumn(name = "u_id")
        private UserInfo userInfo;

        @UpdateTimestamp
        private LocalDateTime wish_date;


        @Builder
        public wish(Long wish_id,Long pd_id, UserInfo u_id, LocalDateTime wish_date) {
            this.wish_id = wish_id;
            this.pd_id = pd_id;
            this.u_id = u_id;
            this.wish_date = wish_date;
        }
    }

}
