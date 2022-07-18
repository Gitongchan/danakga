package com.danakga.webservice.board.model;

import com.danakga.webservice.user.model.UserInfo;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

    //id auto_increment 사용
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bd_id")
    private Long bdId;

    @Column(name= "bd_type")
    private String bdType;

    @Column(name = "bd_views")
    private int bdViews;

    //사용자 userid 넣어야함
    @Column(name = "bd_writer")
    private String bdWriter;

    @Column(name = "bd_title")
    private String bdTitle;

    @Column(name = "bd_content", columnDefinition = "TEXT")
    private String bdContent;

    //String Y = 게시글 삭제, N = 보여줌
    @Column(name = "bd_deleted")
    private String bdDeleted;

    //한명의 유저는 여러 게시글 작성 가능 board(many), user(one)
    @ManyToOne
    @JoinColumn(name = "u_id")
    private UserInfo userInfo;

    @CreationTimestamp
    private LocalDateTime bdCreated;

    @UpdateTimestamp
    private LocalDateTime bdModified;

    //insert시 기본값으로 bd_deleted에 "N"값 적용
    @PrePersist
    public void deleted() {
        this.bdDeleted = "N";
    }

    //setter 대신 사용 , 나머지 매개변수들 넣어야함
    @Builder
    public Board(Long bdId, String bdType, int bdViews,
                 String bdWriter,String bdTitle,
                 String bdContent, String bdDeleted, UserInfo userInfo, LocalDateTime bdCreated, LocalDateTime bdModified) {
        this.bdId = bdId;
        this.bdType = bdType;
        this.bdViews = bdViews;
        this.bdWriter = bdWriter;
        this.bdTitle = bdTitle;
        this.bdContent = bdContent;
        this.bdDeleted = bdDeleted;
        this.userInfo = userInfo;
        this.bdCreated = bdCreated;
        this.bdModified = bdModified;
    }
}
