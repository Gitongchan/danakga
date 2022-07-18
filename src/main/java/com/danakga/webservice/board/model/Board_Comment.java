package com.danakga.webservice.board.model;

import com.danakga.webservice.user.model.UserInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
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

    @CreationTimestamp
    private LocalDateTime cmCreated;

    @UpdateTimestamp
    private LocalDateTime cmModified;


    @ManyToOne
    @JoinColumn(name = "bd_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "u_id")
    private UserInfo userInfo;

    //삭제 여부를 insert시 N 값으로 자동 입력
    @PrePersist
    public void deleted() {
        this.cmDeleted = "N";
    }

    @Builder
    public Board_Comment(Long cmId, String cmContent, String cmDeleted, String cmWriter,LocalDateTime cmCreated, LocalDateTime cmModified,
                         UserInfo userInfo, Board board) {
        this.cmId = cmId;
        this.cmContent = cmContent;
        this.cmDeleted = cmDeleted;
        this.cmWriter = cmWriter;
        this.cmCreated = cmCreated;
        this.cmModified = cmModified;
        this.userInfo = userInfo;
        this.board = board;
    }
}
