package com.danakga.webservice.review.model;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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

    @CreationTimestamp
    private LocalDateTime rCreated;

    @Column(name = "r_deleted")
    private String rDeleted;

    //insert시 기본값으로 r_deleted에 "N"값 적용
    @PrePersist
    public void deleted() {
        this.rDeleted = "N";
    }

    @Builder
    public Review(Long rId, String rContent, double rScore,
                  LocalDateTime rCreated, String rDeleted) {
        this.rId = rId;
        this.rContent = rContent;
        this.rScore = rScore;
        this.rDeleted = rDeleted;
        this.rCreated = rCreated;
    }
}
