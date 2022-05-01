package com.danakga.webservice.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResFileUploadDto {

    private Long f_id;
    private String message;

    public ResFileUploadDto(Long f_id) {
        this.f_id = f_id;
        this.message = "파일 업로드 성공";
    }
}
