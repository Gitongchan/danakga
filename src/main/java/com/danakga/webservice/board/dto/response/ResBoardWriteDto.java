package com.danakga.webservice.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResBoardWriteDto {

    private Long bd_id;
    private String message;

    public ResBoardWriteDto(Long bd_id) {
        this.bd_id = bd_id;
        this.message = "게시글 작성 성공";
    }
}
