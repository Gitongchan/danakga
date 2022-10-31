package com.danakga.webservice.qna.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Answer {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "a_id")
    private Long aId;

    @Column(name = "a_writer")
    private String aWriter;

    @Column(name = "a_content")
    private String aContent;

    @Column(name = "a_deleted")
    private String aDeleted;

    @CreationTimestamp
    private LocalDateTime aCreated;

    @ManyToOne
    @JoinColumn(name = "q_id")
    private QnA QnA;

    @PrePersist
    public void status() {
        this.aDeleted = "N";
    }

    @Builder
    public Answer(Long aId, String aContent, String aDeleted, LocalDateTime aCreated,
                  String aWriter, QnA QnA) {
        this.aId = aId;
        this.aContent = aContent;
        this.aDeleted = aDeleted;
        this.aCreated = aCreated;
        this.aWriter = aWriter;
        this.QnA = QnA;
    }
}
