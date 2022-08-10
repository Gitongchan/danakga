package com.danakga.webservice.review.model;


import com.danakga.webservice.orders.model.Orders;
import com.danakga.webservice.user.model.UserInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "r_id")
    private Long rId;

    @Column(name = "r_content")
    private String rContent;

    @Column(name = "r_score")
    private double rScore;

    @Column(name = "r_writer")
    private String rWriter;

    @CreationTimestamp
    private LocalDateTime rCreated;

    @UpdateTimestamp
    private LocalDateTime rModified;

    @Column(name = "r_deleted")
    private String rDeleted;

    //회원 정보
    @ManyToOne
    @JoinColumn(name = "u_id")
    private UserInfo userInfo;

    //주문내역
    @OneToOne
    @JoinColumn(name = "o_id")
    private Orders orders;

    //insert시 기본값으로 r_deleted에 "N"값 적용
    @PrePersist
    public void deleted() {
        this.rDeleted = "N";
    }

    @Builder
    public Review(Long rId, String rContent, double rScore, String rWriter,
                  LocalDateTime rCreated, LocalDateTime rModified, String rDeleted,
                  UserInfo userInfo, Orders orders) {
        this.rId = rId;
        this.rContent = rContent;
        this.rScore = rScore;
        this.rWriter = rWriter;
        this.rDeleted = rDeleted;
        this.rCreated = rCreated;
        this.rModified = rModified;
        this.userInfo = userInfo;
        this.orders = orders;
    }
}
