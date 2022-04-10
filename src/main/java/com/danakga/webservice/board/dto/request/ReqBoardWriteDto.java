package com.danakga.webservice.board.dto.request;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReqBoardWriteDto {

    // @AllArgsConstructor로 모든 필드의 값을 파라미터로 받는 생성자 생성하여 form값 받아옴
    private Long bd_id;
    private String bd_title;
    private String bd_writer;
    private String bd_content;
}
