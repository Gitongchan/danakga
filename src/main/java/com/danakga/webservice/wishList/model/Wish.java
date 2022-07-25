package com.danakga.webservice.wishList.model;

import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.user.model.UserInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)//무분별한 객체 생성에 대해 한번 더 체크할 수 있는 수단
public class Wish {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "wish_id")
        private Long wishId;


        @ManyToOne
        @JoinColumn(name = "pd_id")
        private Product productId;

        @ManyToOne
        @JoinColumn(name = "u_id")
        private UserInfo userInfo;

        @Column(name = "wish_date")
        private LocalDateTime wishDate;


        @Builder
        public Wish(Long wishId, Product productId, UserInfo userInfo, LocalDateTime wishDate) {
            this.wishId = wishId;
            this.productId = productId;
            this.userInfo = userInfo;
            this.wishDate = wishDate;
        }
    }


