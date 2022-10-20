package com.danakga.webservice.qna.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class Answer {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "a_id")
    private Long aId;

    @Column(name = "a_content")
    private String aContent;

    @Column(name = "a_deleted")
    private String aDeleted;

    @CreationTimestamp
    private LocalDateTime aCreated;

    @ManyToOne
    @JoinColumn(name = "q_id")
    private QnA qna;

    @Builder
    public Answer(Long aId, String aContent, String aDeleted, LocalDateTime aCreated, QnA qna) {
        this.aId = aId;
        this.aContent = aContent;
        this.aDeleted = aDeleted;
        this.aCreated = aCreated;
        this.qna = qna;
    }
}
