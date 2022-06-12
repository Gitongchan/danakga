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
public class ReqCommentDto {

    @NotBlank(message = "내용은 필수로 입력해야 합니다.")
    private String cmContent;
}
