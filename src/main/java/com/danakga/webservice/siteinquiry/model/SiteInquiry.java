package com.danakga.webservice.siteinquiry.model;

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
public class SiteInquiry {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sin_id")
    private Long sinId;

    @Column(name = "sin_type")
    private String sinType;

    @Column(name = "sin_title")
    private String sinTitle;

    @Column(name = "sin_content")
    private String sinContent;

    //0은 답변x, 1은 답변o
    @Column(name = "sin_state")
    private int sinState;

    @Column(name = "sin_deleted")
    private String sinDeleted;

    @CreationTimestamp
    private LocalDateTime sinCreated;

    @UpdateTimestamp
    private LocalDateTime sinModified;

    @ManyToOne
    @JoinColumn(name = "u_id")
    private UserInfo userInfo;

    //답변, 삭제여부 기본 값 설정
    @PrePersist
    public void state() {
        this.sinState = 0;
        this.sinDeleted = "N";
    }

    @Builder
    public SiteInquiry(Long sinId, String sinType, String sinTitle, String sinContent,
                       int sinState, String sinDeleted, LocalDateTime sinCreated, LocalDateTime sinModified, UserInfo userInfo) {
        this.sinId = sinId;
        this.sinType = sinType;
        this.sinTitle = sinTitle;
        this.sinContent = sinContent;
        this.sinState = sinState;
        this.sinDeleted = sinDeleted;
        this.sinCreated = sinCreated;
        this.sinModified = sinModified;
        this.userInfo = userInfo;
    }
}
