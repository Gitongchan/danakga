package com.danakga.webservice.mytool.model;

import com.danakga.webservice.company.model.CompanyInfo;
import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.user.model.UserInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@DynamicUpdate
public class MyToolFolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "my_tool_id")
    private Long id;

    //유저정보
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_info")
    private UserInfo userInfo;

    //폴더명
    @Column(name = "my_tool_folder")
    private String myToolFolder;

    @Builder
    public MyToolFolder(Long id, UserInfo userInfo, String myToolFolder) {
        this.id = id;
        this.userInfo = userInfo;
        this.myToolFolder = myToolFolder;
    }
}
