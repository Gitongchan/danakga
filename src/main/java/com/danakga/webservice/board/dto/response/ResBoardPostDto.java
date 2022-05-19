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
public class ResBoardPostDto {

    private Long bd_id;
    private String bd_writer;
    private String bd_title;
    private String bd_content;
    private LocalDateTime bd_modified;
    private LocalDateTime bd_created;
    private int bd_views;
    private String file_path;
}
