package com.danakga.webservice.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResFileUploadDto {

    private Long id;
    private String message;

    public ResFileUploadDto(Long id) {
        this.id = id;
        this.message = "파일 업로드 성공";
    }
}
