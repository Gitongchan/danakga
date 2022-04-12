package com.danakga.webservice.board.dto.request;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
public class ReqBoardWriteDto {

    // @AllArgsConstructor로 모든 필드의 값을 파라미터로 받는 생성자 생성하여 form값 받아옴
    private Long bd_id;
    private String bd_type;
    private int bd_views;
    private String bd_writer;
    private String bd_title;
    private String bd_content;
    private String bd_filepath;
    private String bd_deleted;

    public ReqBoardWriteDto() {

    }
}
