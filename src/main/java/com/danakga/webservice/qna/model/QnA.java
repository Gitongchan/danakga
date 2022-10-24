package com.danakga.webservice.qna.model;

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
public class QnA {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "q_id")
    private Long qId;
    
    // 가게, 사이트 문의사항 구분 추가하기
    @Column(name = "q_sort")
    private int qSort;

    @Column(name = "q_writer")
    private String qWriter;

    @Column(name = "q_type")
    private String qType;

    @Column(name = "q_title")
    private String qTitle;

    @Column(name = "q_content")
    private String qContent;

    //0은 답변x, 1은 답변o
    @Column(name = "q_state")
    private int qState;

    @Column(name = "q_deleted")
    private String qDeleted;

    @CreationTimestamp
    private LocalDateTime qCreated;

    @UpdateTimestamp
    private LocalDateTime qModified;

    @ManyToOne
    @JoinColumn(name = "u_id")
    private UserInfo userInfo;

    @ManyToOne
    @JoinColumn(name = "p_id")
    private Product product;

    //답변, 삭제여부 기본 값 설정
    @PrePersist
    public void status() {
        this.qState = 0;
        this.qDeleted = "N";
    }

    @Builder
    public QnA(Long qId, String qType, String qTitle, String qContent, String qWriter,
               int qSort, int qState, String qDeleted, LocalDateTime qCreated, LocalDateTime qModified,
               UserInfo userInfo, Product product) {
        this.qId = qId;
        this.qSort = qSort;
        this.qWriter = qWriter;
        this.qType = qType;
        this.qTitle = qTitle;
        this.qContent = qContent;
        this.qState = qState;
        this.qDeleted = qDeleted;
        this.qCreated = qCreated;
        this.qModified = qModified;
        this.userInfo = userInfo;
        this.product = product;
    }
}
