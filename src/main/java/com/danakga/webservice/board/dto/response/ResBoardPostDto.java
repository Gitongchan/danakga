package com.danakga.webservice.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResBoardPostDto {

    //게시글 정보 
    private Long bdId;
    private String bdWriter;
    private String bdTitle;
    private String bdContent;
    private LocalDateTime bdModified;
    private LocalDateTime bdCreated;
    private int bdViews;

    //파일 정보
    private List<Map<String,Object>> files;
    //댓글 정보
    private List<Map<String,Object>> comments;
}
