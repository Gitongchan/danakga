package com.danakga.webservice.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResBoardListDto {

    private String bd_writer;
    private String bd_title;
    private LocalDateTime bd_created;
    private int bd_views;
}
