package com.danakga.webservice.board.model;

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
public class Board_Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cm_id")
    private Long cmId;

    @Column(name = "cm_comment")
    private String cmContent;

    @Column(name = "cm_deleted")
    private String cmDeleted;

    @Column(name = "cm_writer")
    private String cmWriter;

    //댓글의 그룹 (처음 시작은 0이고 댓글이 작성될 때 마다 +1씩 해주면 되는데, 가장 큰 group 값 + 1)
    @Column(name = "cm_group")
    private int cmGroup;

    //댓글의 계층 (댓글은 0, 대댓글은 1, 대대댓글은 2 이런식)
    @Column(name = "cm_step")
    private int cmStep;

    //대댓글의 순서
    @Column(name = "cm_depth")
    private int cmDepth;

    //대댓글 갯수
    @Column(name = "cm_answernum")
    private int cmAnswerNum;

    //대댓글의 부모 id값
    @Column(name = "cm_parentnum")
    private int cmParentNum;

    @CreationTimestamp
    private LocalDateTime cmCreated;

    @UpdateTimestamp
    private LocalDateTime cmModified;

    @ManyToOne
    @JoinColumn(name = "bd_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Board board;

    @ManyToOne
    @JoinColumn(name = "u_id")
    private UserInfo userInfo;

    //삭제 여부를 insert 하면 N 값으로 자동 입력
    @PrePersist
    public void deleted() {
        this.cmDeleted = "N";
    }

    @Builder
    public Board_Comment(Long cmId, String cmContent, String cmDeleted, String cmWriter,
                         int cmGroup, int cmStep, int cmDepth, int cmAnswerNum, int cmParentNum,
                         LocalDateTime cmCreated, LocalDateTime cmModified,
                         UserInfo userInfo, Board board) {
        this.cmId = cmId;
        this.cmContent = cmContent;
        this.cmDeleted = cmDeleted;
        this.cmWriter = cmWriter;
        this.cmGroup = cmGroup;
        this.cmStep = cmStep;
        this.cmDepth = cmDepth;
        this.cmAnswerNum = cmAnswerNum;
        this.cmParentNum = cmParentNum;
        this.cmCreated = cmCreated;
        this.cmModified = cmModified;
        this.userInfo = userInfo;
        this.board = board;
    }
}
