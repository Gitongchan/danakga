package com.danakga.webservice.board.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReqBoardDto {

    // @AllArgsConstructor로 모든 필드의 값을 파라미터로 받는 생성자 생성하여 form값 받아옴
    private String bdType;
    @NotBlank(message = "제목은 필수로 입력해야 합니다.")
    private String bdTitle;
    @NotBlank(message = "내용은 필수로 입력해야 합니다.")
    private String bdContent;

}
