package com.danakga.webservice.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResBoardWriteDto {

    private Long id;
    private String message;

    public ResBoardWriteDto(Long id) {
        this.id = id;
        this.message = "게시글 작성 성공";
    }
}
