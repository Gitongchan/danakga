package com.danakga.webservice.product.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@DynamicUpdate
public class ProductFiles {
    
    //상품 파일 아이디
    @Id
    @GeneratedValue
    @Column(name="pf_id")
    private Long pfId;

    //상품 아이디
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "pd_id")
    private Product product;

    //상품 원본 파일명
    @Column(name="pf_origin")
    private String pfOrigin;

    //저장되는 파일명
    @Column(name="pf_savename")
    private String pfSaveName;

    //상품 파일 경로
    @Column(name="pf_path")
    private String pfPath;

    @Builder
    public ProductFiles(Long pfId, Product product, String pfOrigin, String pfSaveName, String pfPath) {
        this.pfId = pfId;
        this.product = product;
        this.pfOrigin = pfOrigin;
        this.pfSaveName = pfSaveName;
        this.pfPath = pfPath;
    }
}
