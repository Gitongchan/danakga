package com.danakga.webservice.board.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Files {

    @Id @GeneratedValue
    private Long f_id;

    @Column(name="f_origin")
    private String f_origin;

    @Column(name="f_name")
    private String f_name;

    @Column(name="f_path")
    private String f_path;

    @ManyToOne
    @JoinColumn(name = "bd_id")
    private Board board;

    @Builder
    public Files(Long f_id, String f_origin, String f_name, String f_path, Board board) {
        this.f_id = f_id;
        this.f_origin = f_origin;
        this.f_name = f_name;
        this.f_path = f_path;
        this.board = board;
    }

}
