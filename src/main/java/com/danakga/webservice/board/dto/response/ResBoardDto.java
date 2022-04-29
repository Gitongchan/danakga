package com.danakga.webservice.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResBoardDto {

    //inner 클래스 사용해서 공용으로 사용하기 ResBoardDto.inner클래스명
    private Long id;
    private String message;

    public ResBoardDto(Long id) {
        this.id = id;
        this.message = "게시글 작성 성공";
    }

    public static class ResFileUploadDto {
        private Long id;
        private String message;

        public ResFileUploadDto(Long id) {
            this.id = id;
            this.message = "게시글 작성 성공";
        }
    }
    public static class ResBoardUpdateDto {
        private Long id;
        private String message;

        public ResBoardUpdateDto(Long id) {
            this.id = id;
            this.message = "게시글 작성 성공";
        }
    }
}
