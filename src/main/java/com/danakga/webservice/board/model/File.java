package com.danakga.webservice.board.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class File {

    @Id @GeneratedValue
    private Long f_id;

    @Column(name="originFileName", nullable=false)
    private String f_originName;

    @Column(name="filename", nullable=false)
    private String f_name;

    @Column(name="filepath", nullable=false)
    private String f_path;

    @ManyToOne
    @JoinColumn(name = "bd_id")
    private Board board;

    @Builder
    public File(Long f_id, String f_originName, String f_name, String f_path, Board board) {
        this.f_id = f_id;
        this.f_originName = f_originName;
        this.f_name = f_name;
        this.f_path = f_path;
        this.board = board;
    }

}
