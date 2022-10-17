package com.danakga.webservice.siteqna.model;

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
    @Column(name = "sin_id")
    private Long sqId;

    @Column(name = "sin_type")
    private String sqType;

    @Column(name = "sin_title")
    private String sqTitle;

    @Column(name = "sin_content")
    private String sqContent;

    //0은 답변x, 1은 답변o
    @Column(name = "sin_state")
    private int sqState;

    @Column(name = "sin_deleted")
    private String sqDeleted;

    @CreationTimestamp
    private LocalDateTime sqCreated;

    @UpdateTimestamp
    private LocalDateTime sqModified;

    @ManyToOne
    @JoinColumn(name = "u_id")
    private UserInfo userInfo;

    //답변, 삭제여부 기본 값 설정
    @PrePersist
    public void state() {
        this.sqState = 0;
        this.sqDeleted = "N";
    }

    @Builder
    public SiteQnA(Long sqId, String sqType, String sqTitle, String sqContent,
                   int sqState, String sqDeleted, LocalDateTime sqCreated, LocalDateTime sqModified, UserInfo userInfo) {
        this.sqId = sqId;
        this.sqType = sqType;
        this.sqTitle = sqTitle;
        this.sqContent = sqContent;
        this.sqState = sqState;
        this.sqDeleted = sqDeleted;
        this.sqCreated = sqCreated;
        this.sqModified = sqModified;
        this.userInfo = userInfo;
    }
}
