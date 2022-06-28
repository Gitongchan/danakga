package com.danakga.webservice.mytool.model;

import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.user.model.UserInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@DynamicUpdate
public class MyToolDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "my_tool_id")
    private Long id;

    //상품정보
    @ManyToOne
    @JoinColumn(name = "product_info")
    private Product product;

    //내 장비 폴더
    @ManyToOne
    @JoinColumn(name = "my_tool_folder")
    private MyToolFolder myToolFolder;
}
