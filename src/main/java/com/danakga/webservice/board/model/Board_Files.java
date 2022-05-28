package com.danakga.webservice.board.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board_Files {

    @Id @GeneratedValue
    @Column(name = "f_id")
    private Long fId;

    @Column(name="f_origin")
    private String fOrigin;

    @Column(name="f_savename")
    private String fSaveName;

    @Column(name="f_path")
    private String fPath;

    @ManyToOne
    @JoinColumn(name = "bd_id")
    private Board board;

    @Builder
    public Board_Files(Long fId, String fOrigin, String fSaveName, String fPath, Board board) {
        this.fId = fId;
        this.fOrigin = fOrigin;
        this.fSaveName = fSaveName;
        this.fPath = fPath;
        this.board = board;
    }

}
