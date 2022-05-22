package com.danakga.webservice.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
    private List<Map<String,Object>> files;
}
