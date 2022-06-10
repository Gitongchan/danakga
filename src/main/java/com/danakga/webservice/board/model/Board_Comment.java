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
    private String cmComment;

    @Column(name = "cm_deleted")
    private String cmDeleted;

    @CreationTimestamp
    private LocalDateTime cmCreated;

    @UpdateTimestamp
    private LocalDateTime cmModified;

    @ManyToOne
    @JoinColumn(name = "u_id")
    private UserInfo userInfo;

    @ManyToOne
    @JoinColumn(name = "bd_id")
    private Board board;

    @Builder
    public Board_Comment(Long cmId, String cmComment, String cmDeleted, LocalDateTime cmCreated, LocalDateTime cmModified,
                         UserInfo userInfo, Board board) {
        this.cmId = cmId;
        this.cmComment = cmComment;
        this.cmDeleted = cmDeleted;
        this.cmCreated = cmCreated;
        this.cmModified = cmModified;
        this.userInfo = userInfo;
        this.board = board;
    }
}
