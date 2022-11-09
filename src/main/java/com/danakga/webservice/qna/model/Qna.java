package com.danakga.webservice.qna.model;

import com.danakga.webservice.company.model.CompanyInfo;
import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.user.model.UserInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Qna {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qn_id")
    private Long qnId;

    @Column(name = "qn_writer")
    private String qnWriter;

    /* 0 => 사이트, 1 => 가게 */
    @Column(name ="qn_sort")
    private Integer qnSort;

    @Column(name = "qn_type")
    private String qnType;

    @Column(name = "qn_title")
    private String qnTitle;

    @Column(name = "qn_content")
    private String qnContent;

    //0은 답변x, 1은 답변o
    @Column(name = "qn_state")
    private int qnState;

    @Column(name = "qn_deleted")
    private String qnDeleted;

    @CreationTimestamp
    private LocalDateTime qnCreated;

    @UpdateTimestamp
    private LocalDateTime qnModified;

    @ManyToOne
    @JoinColumn(name = "u_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserInfo userInfo;

    @ManyToOne
    @JoinColumn(name = "c_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CompanyInfo companyInfo;

    @ManyToOne
    @JoinColumn(name = "pd_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    //답변, 삭제여부 기본 값 설정
    @PrePersist
    public void status() {
        this.qnState = 0;
        this.qnDeleted = "N";
    }

    @Builder
    public Qna(Long qnId, Integer qnSort, String qnType, String qnTitle, String qnContent, String qnWriter,
               int qnState, String qnDeleted, LocalDateTime qnCreated, LocalDateTime qnModified,
               UserInfo userInfo, CompanyInfo companyInfo, Product product) {
        this.qnId = qnId;
        this.qnSort = qnSort;
        this.qnWriter = qnWriter;
        this.qnType = qnType;
        this.qnTitle = qnTitle;
        this.qnContent = qnContent;
        this.qnState = qnState;
        this.qnDeleted = qnDeleted;
        this.qnCreated = qnCreated;
        this.qnModified = qnModified;
        this.userInfo = userInfo;
        this.companyInfo = companyInfo;
        this.product = product;
    }
}
