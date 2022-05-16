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
    private Long bd_id;

    @Column(name= "bd_type")
    private String bd_type;

    @Column(name = "bd_views")
    private int bd_views;

    //사용자 userid 넣어야함
    @Column(name = "bd_writer")
    private String bd_writer;

    @Column(name = "bd_title", nullable = false)
    private String bd_title;

    @Column(name = "bd_content", columnDefinition = "TEXT", nullable = false)
    private String bd_content;

    //String Y = 게시글 삭제, N = 보여줌
    @Column(name = "bd_deleted", nullable = false)
    private String bd_deleted;

    //한명의 유저는 여러 게시글 작성 가능 board(many), user(one)
    @ManyToOne
    @JoinColumn(name = "u_id")
    private UserInfo userInfo;

    @CreationTimestamp
    private LocalDateTime bd_created;

    @UpdateTimestamp
    private LocalDateTime bd_modified;

    //insert시 기본값으로 bd_deleted에 "N"값 적용
    @PrePersist
    public void deleted() {
        this.bd_deleted = "N";
    }

    //setter 대신 사용 , 나머지 매개변수들 넣어야함
    @Builder
    public Board(Long bd_id, String bd_type, int bd_views,
                 String bd_writer,String bd_title,
                 String bd_content, String bd_deleted, UserInfo userInfo, LocalDateTime bd_created, LocalDateTime bd_modified) {
        this.bd_id = bd_id;
        this.bd_type = bd_type;
        this.bd_views = bd_views;
        this.bd_writer = bd_writer;
        this.bd_title = bd_title;
        this.bd_content = bd_content;
        this.bd_deleted = bd_deleted;
        this.userInfo = userInfo;
        this.bd_created = bd_created;
        this.bd_modified = bd_modified;
    }
}
