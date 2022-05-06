package com.danakga.webservice.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResPostDto {

    private Long bd_id;
    private String bd_writer;
    private String bd_title;
    private String bd_content;

}
