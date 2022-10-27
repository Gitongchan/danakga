package com.danakga.webservice.qna.site.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class SiteAnswer {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "siteA_id")
    private Long siteAId;

    @Column(name = "siteA_writer")
    private String siteAWriter;

    @Column(name = "siteA_content")
    private String siteAContent;

    @Column(name = "siteA_deleted")
    private String siteADeleted;

    @CreationTimestamp
    private LocalDateTime siteACreated;

    @ManyToOne
    @JoinColumn(name = "siteQ_id")
    private SiteQnA siteQnA;

    @PrePersist
    public void status() {
        this.siteADeleted = "N";
    }

    @Builder
    public SiteAnswer(Long siteAId, String siteAContent, String siteADeleted, LocalDateTime siteACreated,
                      String siteAWriter, SiteQnA siteQnA) {
        this.siteAId = siteAId;
        this.siteAContent = siteAContent;
        this.siteADeleted = siteADeleted;
        this.siteACreated = siteACreated;
        this.siteAWriter = siteAWriter;
        this.siteQnA = siteQnA;
    }
}
