package com.danakga.webservice.product.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

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
    private Long pf_id;

    //상품 아이디
    @ManyToOne
    @JoinColumn(name = "pd_id")
    private Product product;

    //상품 원본 파일명
    @Column(name="pf_origin")
    private String pf_origin;

    //저장되는 파일명
    @Column(name="pf_savename")
    private String pf_savename;

    //상품 파일 경로
    @Column(name="pf_path")
    private String pf_path;

    @Builder
    public ProductFiles(Long pf_id, Product product, String pf_origin, String pf_savename, String pf_path) {
        this.pf_id = pf_id;
        this.product = product;
        this.pf_origin = pf_origin;
        this.pf_savename = pf_savename;
        this.pf_path = pf_path;
    }
}
