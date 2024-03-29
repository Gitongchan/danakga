package com.danakga.webservice.qna.model;

import com.danakga.webservice.company.model.CompanyInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Answer {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "an_id")
    private Long anId;

    /* 관리자 아이디 or 회사명 */
    @Column(name = "an_writer")
    private String anWriter;

    @Column(name = "an_content")
    private String anContent;

    @Column(name = "an_deleted")
    private String anDeleted;

    @Column(name = "an_parentnum")
    private int anParentNum;

    @CreationTimestamp
    private LocalDateTime anCreated;

    @ManyToOne
    @JoinColumn(name = "q_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Qna qna;

    @PrePersist
    public void status() {
        this.anDeleted = "N";
    }

    @Builder
    public Answer(Long anId, String anContent, String anDeleted, LocalDateTime anCreated,
                  String anWriter, int anParentNum, Qna qna) {
        this.anId = anId;
        this.anContent = anContent;
        this.anDeleted = anDeleted;
        this.anCreated = anCreated;
        this.anWriter = anWriter;
        this.anParentNum = anParentNum;
        this.qna = qna;
    }
}
