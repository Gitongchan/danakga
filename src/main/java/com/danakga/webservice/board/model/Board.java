package com.danakga.webservice.board.model;

import com.danakga.webservice.board.util.BaseTimeEntity;
import com.danakga.webservice.user.model.UserInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseTimeEntity {

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

    @Column(name = "bd_title")
    private String bd_title;

    @Column(name = "bd_content", columnDefinition = "TEXT")
    private String bd_content;

    @Column(name = "bd_filepath")
    private String bd_filepath;

    //String Y = 게시글 보임, N = 숨겨주기
    @Column(name = "bd_deleted", columnDefinition="VARCHAR(2) default 'Y'")
    private String bd_deleted;

    //한명의 유저는 여러 게시글 작성 가능 board(many), user(one)
//    @ManyToOne
//    @JoinColumn(name = "u_id")
//    private UserInfo userInfo;

    //setter 대신 사용 , 나머지 매개변수들 넣어야함
    @Builder
    public Board(Long bd_id, String bd_type, int bd_views,
                 String bd_writer,String bd_title, String bd_content,
                 String bd_filepath, String bd_deleted) {
        this.bd_id = bd_id;
        this.bd_type = bd_type;
        this.bd_views = bd_views;
        this.bd_writer = bd_writer;
        this.bd_title = bd_title;
        this.bd_content = bd_content;
        this.bd_filepath = bd_filepath;
        this.bd_deleted = bd_deleted;
    }
}
