package com.danakga.webservice.board.model;

import com.danakga.webservice.user.model.UserInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

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

    @Column(name = "bd_number")
    private int bd_number;

    @Column(name = "bd_views")
    private int bd_views;

    //사용자 id 넣어야함
    @Column(name = "bd_writer")
    private String bd_writer;

    @Column(name = "bd_title")
    private String bd_title;

    @Column(name = "bd_content", columnDefinition = "TEXT")
    private String bd_content;

    @Column(name = "bd_filepath")
    private String bd_filepath;

    //String Y = 게시글 보임, N = 숨겨주기
    @Column(name = "bd_deleted", columnDefinition="VARCHAR(2) default Y")
    private String bd_deleted;

    //한명의 유저는 여러 게시글 작성 가능 board(many), user
    //jpa 연관관계로 외래키나 1:다 1:1 이런 테이블 구조 잡아야하는듯
    //한쪽만 정의된 것을 단방향이라고 하고 양방향도 있지만 가이드에서도 지향하지 않음
    //양방향으로 하면 코드가 두 배로 불어난다고 하고 거의 양방향으로 할 일이 없다고도 함.
    @ManyToOne
    @JoinColumn(name = "u_id")
    private UserInfo userInfo;

    //setter 대신 사용 , 나머지 매개변수들 넣어야함
    @Builder
    public Board(Long bd_id, String bd_type, int bd_number, int bd_views,
                 String bd_writer,String bd_title, String bd_content,
                 String bd_filepath, String bd_deleted) {
        this.bd_id = bd_id;
        this.bd_type = bd_type;
        this.bd_number = bd_number;
        this.bd_views = bd_views;
        this.bd_writer = bd_writer;
        this.bd_title = bd_title;
        this.bd_content = bd_content;
        this.bd_filepath = bd_filepath;
        this.bd_deleted = bd_deleted;
    }
}
