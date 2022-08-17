package com.danakga.webservice.review.model;


import com.danakga.webservice.orders.model.Orders;
import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.user.model.UserInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "r_id")
    private Long reId;

    @Column(name = "r_content")
    private String reContent;

    @Column(name = "r_score")
    private int reScore;

    @Column(name = "r_writer")
    private String reWriter;

    @CreationTimestamp
    private LocalDateTime reCreated;

    @UpdateTimestamp
    private LocalDateTime reModified;

    @Column(name = "r_deleted")
    private String reDeleted;

    //회원 정보
    @ManyToOne
    @JoinColumn(name = "u_id")
    private UserInfo userInfo;

    //주문 내역
    @OneToOne
    @JoinColumn(name = "o_id")
    private Orders orders;

    @ManyToOne
    @JoinColumn(name = "p_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    //insert시 기본값으로 re_deleted에 "N"값 적용
    @PrePersist
    public void deleted() {
        this.reDeleted = "N";
    }

    @Builder
    public Review(Long reId, String reContent, int reScore, String reWriter,
                  LocalDateTime reCreated, LocalDateTime reModified, String reDeleted,
                  UserInfo userInfo, Orders orders, Product product) {
        this.reId = reId;
        this.reContent = reContent;
        this.reScore = reScore;
        this.reWriter = reWriter;
        this.reDeleted = reDeleted;
        this.reCreated = reCreated;
        this.reModified = reModified;
        this.userInfo = userInfo;
        this.orders = orders;
        this.product = product;
    }
}
