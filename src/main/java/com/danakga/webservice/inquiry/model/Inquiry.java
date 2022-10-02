package com.danakga.webservice.inquiry.model;

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
public class Inquiry {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "in_id")
    private Long inId;

    @Column(name = "in_type")
    private String inType;

    @Column(name = "in_title")
    private String inTitle;

    @Column(name = "in_content")
    private String inContent;

    //0은 답변x, 1은 답변o
    @Column(name = "in_state")
    private int inState;

    @Column(name = "in_deleted")
    private String inDeleted;

    @CreationTimestamp
    private LocalDateTime inCreated;

    @UpdateTimestamp
    private LocalDateTime inModified;

    @ManyToOne
    @JoinColumn(name = "u_id")
    private UserInfo userInfo;

    //답변, 삭제여부 기본 값 설정
    @PrePersist
    public void state() {
        this.inState = 0;
        this.inDeleted = "N";
    }

    @Builder
    public Inquiry(Long inId, String inType, String inTitle, String inContent,
                   int inState, String inDeleted, LocalDateTime inCreated, LocalDateTime inModified, UserInfo userInfo) {
        this.inId = inId;
        this.inType = inType;
        this.inTitle = inTitle;
        this.inContent = inContent;
        this.inState = inState;
        this.inDeleted = inDeleted;
        this.inCreated = inCreated;
        this.inModified = inModified;
        this.userInfo = userInfo;
    }
}
