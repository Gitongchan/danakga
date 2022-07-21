package com.danakga.webservice.mytool.model;

import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.user.model.UserInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @OnDelete(action = OnDeleteAction.CASCADE) // 상위 폴더가 삭제되면 폴더 목록 데이터도 같이 삭제된다
    private MyToolFolder myToolFolder;

    //내 장비 수량
    @Column(name="my_tool_quantity")
    private int myToolQuantity;

    @Builder
    public MyToolDetail(Long id, Product product, MyToolFolder myToolFolder,int myToolQuantity) {
        this.id = id;
        this.product = product;
        this.myToolFolder = myToolFolder;
        this.myToolQuantity = myToolQuantity;
    }
}
