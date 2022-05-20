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
    private Long f_id;

    @Column(name="f_origin")
    private String f_origin;

    @Column(name="f_savename")
    private String f_savename;

    @Column(name="f_path")
    private String f_path;

    @ManyToOne
    @JoinColumn(name = "bd_id")
    private Board board;

    @Builder
    public Board_Files(Long f_id, String f_origin, String f_savename, String f_path, Board board) {
        this.f_id = f_id;
        this.f_origin = f_origin;
        this.f_savename = f_savename;
        this.f_path = f_path;
        this.board = board;
    }

}
