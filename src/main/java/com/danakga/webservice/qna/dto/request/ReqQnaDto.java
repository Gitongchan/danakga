package com.danakga.webservice.qna.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ReqQnaDto {

    private String qnaType;

    private String qnaTitle;

    @NotBlank(message = "내용은 필수로 입력해야 합니다.")
    private String qnaContent;

    private Integer qnaSort;

}
