package com.danakga.webservice.qna.site.model;

import com.danakga.webservice.product.model.Product;
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
public class SiteQnA {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "siteQ_id")
    private Long siteQId;

    @Column(name = "siteQ_writer")
    private String siteQWriter;

    @Column(name = "siteQ_type")
    private String siteQType;

    @Column(name = "siteQ_title")
    private String siteQTitle;

    @Column(name = "siteQ_content")
    private String siteQContent;

    //0은 답변x, 1은 답변o
    @Column(name = "siteQ_state")
    private int siteQState;

    @Column(name = "siteQ_deleted")
    private String siteQDeleted;

    @CreationTimestamp
    private LocalDateTime siteQCreated;

    @UpdateTimestamp
    private LocalDateTime siteQModified;

    @ManyToOne
    @JoinColumn(name = "u_id")
    private UserInfo userInfo;

    //답변, 삭제여부 기본 값 설정
    @PrePersist
    public void status() {
        this.siteQState = 0;
        this.siteQDeleted = "N";
    }

    @Builder
    public SiteQnA(Long siteQId, String siteQType, String siteQTitle, String siteQContent, String siteQWriter,
                   int siteQState, String siteQDeleted, LocalDateTime siteQCreated, LocalDateTime siteQModified,
                   UserInfo userInfo) {
        this.siteQId = siteQId;
        this.siteQWriter = siteQWriter;
        this.siteQType = siteQType;
        this.siteQTitle = siteQTitle;
        this.siteQContent = siteQContent;
        this.siteQState = siteQState;
        this.siteQDeleted = siteQDeleted;
        this.siteQCreated = siteQCreated;
        this.siteQModified = siteQModified;
        this.userInfo = userInfo;
    }
}
