package com.danakga.webservice.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResBoardUpdateDto {

    private Long id;
    private String message;

    public ResBoardUpdateDto(Long id) {
        this.id = id;
        this.message = "게시글 수정 성공";
    }
}
