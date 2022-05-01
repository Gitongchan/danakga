package com.danakga.webservice.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResBoardUpdateDto {

    private Long bd_id;
    private String message;

    public ResBoardUpdateDto(Long bd_id) {
        this.bd_id = bd_id;
        this.message = "게시글 수정 성공";
    }
    
}
